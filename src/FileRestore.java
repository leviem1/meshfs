import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * The FileRestore class is responsible for restoring files
 * when corrupted when nodes go offline.
 *
 * @author Aaron Duran
 * @version 1.0.0
 */

class FileRestore {

    private FileRestore() {
    }

    /**
     * This method is used to redistribute all of the files that were on a computer.
     * The method uses redundancies to repair and updates the catalog file respectively.
     *
     * @param MACAddress the file path of the file that is to be distributed
     */

    static void restoreAllFilesFromComputer(String MACAddress) {
        JSONObject manifest = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");
        JSONArray filesToRestore = (JSONArray) ((JSONObject) manifest.get(MACAddress)).get("RepoContents");
        manifest.remove(MACAddress);

        try {
            JSONUtils.writeJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json", manifest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Thread> childThreads = new ArrayList<>();
        for (Object fileNameObj : filesToRestore) {
            String fileName = fileNameObj.toString();
            if (!(fileName.equals(".auth") || fileName.equals(".catalog.json") || fileName.equals(".manifest.json"))) {
                //thread the restorations
                Thread child = new Thread(
                        () -> restorePartialFile(fileName, MACAddress));
                childThreads.add(child);
            }
        }

        //restore all files simultaneously
        for (Thread child : childThreads) {
            child.start();
        }

        //wait for all copies to finish
        for (Thread child : childThreads) {
            if (child.isAlive()) {
                try {
                    child.join();
                } catch (InterruptedException ignored) {
                }
            }
        }

        try {
            JSONUtils.writeJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json", manifest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to find all references in the catalog to the specified file.
     *
     * @param catalog          the JSONObject of the catalog
     * @param alphanumericName the unique name of the file
     * @return returns a list of strings of virtual filePaths
     */

    static List<String> findFileReferencesInCatalog(JSONObject catalog, String alphanumericName) {
        return fileReferenceFinderRecursive((JSONObject) catalog.get("root"), alphanumericName, "root");
    }

    /**
     * This method is used to remove the corrupted flag from all the specified files.
     *
     * @param catalogReferences the list of strings of virtual filePaths of the specified files.
     */

    static void unCorruptFilesInCatalog(List<String> catalogReferences) {
        for (String reference : catalogReferences) {
            JSONUtils.renameItem(reference, reference.substring(reference.lastIndexOf("/") + 1, reference.lastIndexOf(" (corrupted)")));
        }
    }

    /**
     * This method is used to find all files that are supposed to be on a computer according to the catalog.
     *
     * @param MACAddress the MAC Address of the specified computer
     * @return returns a list of strings of alphanumeric file names
     */

    static List<String> getFilesOnComputerFromCatalog(String MACAddress) {
        JSONObject catalog = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        if (catalog.toString().contains(MACAddress)) {
            return computerReferenceFinderRecursive(catalog, MACAddress);
        }
        return new ArrayList<>();
    }

    @SuppressWarnings("unchecked")

    private static void restorePartialFile(String alphanumericFileName, String oldComputerMAC) {
        JSONObject manifest = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");
        String manifestString = manifest.toString();
        String pullIP = null;
        JSONObject catalog = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        List<String> catalogReferences = findFileReferencesInCatalog(catalog, alphanumericFileName.substring(0, alphanumericFileName.lastIndexOf("_")));
        int portNumber = Integer.parseInt(MeshFS.properties.getProperty("portNumber"));

        String wholeFileName = alphanumericFileName.substring(0, alphanumericFileName.indexOf("_")) + "_w";
        JSONObject fileInfo = JSONUtils.getItemContents(catalog, catalogReferences.get(0));
        int stripeNum = -1;
        if (alphanumericFileName.contains("_s")) {
            stripeNum = Integer.parseInt(alphanumericFileName.substring(alphanumericFileName.indexOf("_") + 2));
        }

        LinkedHashMap<String, Long> storageMap = JSONUtils.createStorageMap(manifest);
        for (Object key : fileInfo.keySet()) {
            if (key.toString().equals("whole") || key.toString().contains("stripe_")) {
                JSONArray stripeLocations = (JSONArray) fileInfo.get(key);
                for (Object computer : stripeLocations) {
                    storageMap.remove(computer.toString());
                }
            }
        }

        String destinationCompIP = null;
        String destinationCompMAC = null;
        for (Object macAddress : storageMap.keySet()) {
            String ipAddress = (((JSONObject) manifest.get(macAddress)).get("IP")).toString();
            if (FileClient.ping(ipAddress, portNumber) > -1 && storageMap.get(macAddress.toString()) > FileUtils.getSize(MeshFS.properties.getProperty("repository") + alphanumericFileName)) {
                destinationCompIP = ipAddress;
                destinationCompMAC = macAddress.toString();
                break;
            }
        }

        if (destinationCompIP == null){
            try {
                JSONUtils.pullFile(catalog, catalogReferences.get(0), "test", "test", false);
            } catch (IOException | MalformedRequestException | PullRequestException | FileTransferException e) {
                corruptFilesInCatalog(catalogReferences);
            }
            return;
        }

        if (manifestString.contains(alphanumericFileName)) {
            pullIP = findComputerWithFile(alphanumericFileName);
            try {
                FileClient.receiveFile(pullIP, portNumber, alphanumericFileName, MeshFS.properties.getProperty("repository") + alphanumericFileName);
            } catch (IOException | MalformedRequestException | FileTransferException e) {
                pullIP = null;
            }
        }

        int numberOfStripes = 0;
        for (Object key : fileInfo.keySet()) {
            if (key.toString().contains("_")) {
                numberOfStripes++;
            }
        }
        if (pullIP == null && stripeNum == -1) {
            try {
                JSONUtils.pullFile(catalog, catalogReferences.get(0), MeshFS.properties.getProperty("repository"), alphanumericFileName, true);
                pullIP = "success";
            } catch (IOException | MalformedRequestException | PullRequestException | FileTransferException ignored) {
            }
        }


        if (pullIP == null && stripeNum != -1 && manifestString.contains(wholeFileName)) {
            pullIP = findComputerWithFile(wholeFileName);
            try {
                FileClient.receiveFile(pullIP, portNumber, wholeFileName, MeshFS.properties.getProperty("repository") + wholeFileName);


                long fileSize = Long.parseLong(fileInfo.get("fileSize").toString());
                long sizeOfStripe = ((fileSize / (numberOfStripes)) + 1);
                //write stripe

                if (stripeNum == numberOfStripes) {
                    FileUtils.writeStripe(
                            MeshFS.properties.getProperty("repository") + wholeFileName,
                            MeshFS.properties.getProperty("repository") + alphanumericFileName,
                            (sizeOfStripe * stripeNum),
                            sizeOfStripe - ((sizeOfStripe * numberOfStripes) - fileSize));
                } else {
                    FileUtils.writeStripe(
                            MeshFS.properties.getProperty("repository") + wholeFileName,
                            MeshFS.properties.getProperty("repository") + alphanumericFileName,
                            (sizeOfStripe * stripeNum),
                            sizeOfStripe);
                }

                //delete whole file
                FileUtils.removeFile(MeshFS.properties.getProperty("repository") + wholeFileName);


            } catch (IOException | MalformedRequestException | FileTransferException e) {
                pullIP = null;
            }
        }

        if (pullIP == null) {
            corruptFilesInCatalog(catalogReferences);
            return;
        }

        try {
            FileClient.sendFile(
                    destinationCompIP,
                    portNumber,
                    MeshFS.properties.getProperty("repository")
                            + File.separator
                            + alphanumericFileName);
            FileClient.receiveReport(
                    destinationCompIP,
                    portNumber);
        } catch (IOException | MalformedRequestException e) {
            corruptFilesInCatalog(catalogReferences);
            return;
        }

        for (String referenceLocation : catalogReferences) {
            String[] folders = referenceLocation.split("/");
            JSONObject item = catalog;
            for (String folder : folders) {
                item = (JSONObject) item.get(folder);
            }
            if (stripeNum == -1) {
                ((JSONArray) item.get("whole")).remove(oldComputerMAC);
                ((JSONArray) item.get("whole")).add(destinationCompMAC);
            } else {
                ((JSONArray) item.get("stripe_" + stripeNum)).remove(oldComputerMAC);
                ((JSONArray) item.get("stripe_" + stripeNum)).add(destinationCompMAC);
            }
        }
        try {
            JSONUtils.writeJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json", catalog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String findComputerWithFile(String filename) {
        JSONObject manifest = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");
        for (Object key : manifest.keySet()) {
            JSONObject computer = (JSONObject) manifest.get(key);
            if (computer.toString().contains(filename)) {
                String IP = computer.get("IP").toString();
                try {
                    if (FileClient.doesFileExist(IP, Integer.parseInt(MeshFS.properties.getProperty("portNumber")), filename)) {
                        return IP;
                    }
                } catch (MalformedRequestException e) {
                    e.printStackTrace();
                } catch (IOException ignored){}
            }
        }
        return null;
    }

    private static List<String> fileReferenceFinderRecursive(JSONObject folder, String alphanumericName, String folderLocation) {
        List<String> references = new ArrayList<>();
        LinkedHashMap<String, String> folderMap = JSONUtils.getMapOfFolderContents(folder, null);
        for (String item : folderMap.keySet()) {
            if (folderMap.get(item).equals("directory")) {
                references.addAll(fileReferenceFinderRecursive((JSONObject) folder.get(item), alphanumericName, folderLocation + "/" + item));
            } else if (folderMap.get(item).equals("file") && ((JSONObject) folder.get(item)).get("alphanumericName").toString().equals(alphanumericName)) {
                references.add(folderLocation + "/" + item);
            }
        }
        return references;
    }

    private static void corruptFilesInCatalog(List<String> catalogReferences) {
        for (String reference : catalogReferences) {
            JSONUtils.renameItem(reference, reference.substring(reference.lastIndexOf("/") + 1) + " (corrupted)");
        }
    }

    private static List<String> computerReferenceFinderRecursive(JSONObject folder, String MACAddress) {
        List<String> fileNames = new ArrayList<>();
        LinkedHashMap<String, String> folderMap = JSONUtils.getMapOfFolderContents(folder, null);
        if (folder.toString().contains(MACAddress)) {
            for (String item : folderMap.keySet()) {
                if (folderMap.get(item).equals("directory")) {
                    fileNames.addAll(computerReferenceFinderRecursive((JSONObject) folder.get(item), MACAddress));
                } else if (folderMap.get(item).equals("file")) {
                    JSONObject fileInfo = ((JSONObject) folder.get(item));
                    for (Object key : fileInfo.keySet()) {
                        if (fileInfo.get(key).toString().contains(MACAddress)) {
                            if (key.toString().equals("whole")) {
                                fileNames.add(((JSONObject) folder.get(item)).get("alphanumericName").toString() + "_w");
                            } else {
                                fileNames.add(((JSONObject) folder.get(item)).get("alphanumericName").toString() + "_s" + key.toString().substring(key.toString().indexOf("_s") + 1));
                            }
                        }
                    }
                }
            }
        }
        return fileNames;
    }
}

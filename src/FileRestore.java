import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by Aaron Duran on 5/22/2017.
 */
public class FileRestore {

    private FileRestore(){}

    static void restoreAllFilesFromComputer(String MACAddress){
        JSONObject manifest = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");
        JSONArray filesToRestore = (JSONArray) ((JSONObject) manifest.get(MACAddress)).get("RepoContents");
        manifest.remove(MACAddress);

        try {
            JSONUtils.writeJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json", manifest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Thread> childThreads = new ArrayList<>();
        for (Object fileNameObj : filesToRestore){
            String fileName = fileNameObj.toString();
            if (!(fileName.equals(".auth") || fileName.equals(".catalog.json") || fileName.equals(".manifest.json"))){
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

    private static void restorePartialFile(String alphanumericFileName, String oldComputerMAC){
        JSONObject manifest = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");
        String manifestString = manifest.toString();
        String pullIP = null;
        JSONObject catalog = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        List<String> catalogReferences = findFileReferencesInCatalog(catalog, alphanumericFileName.substring(0,alphanumericFileName.lastIndexOf("_")));
        int portNumber = Integer.parseInt(MeshFS.properties.getProperty("portNumber"));

        if (manifestString.contains(alphanumericFileName)){
            //find and pull the stripe
            pullIP = findComputerWithFile(alphanumericFileName);
            try {
                FileClient.receiveFile(pullIP,portNumber, alphanumericFileName, MeshFS.properties.getProperty("repository") + File.separator + alphanumericFileName);
            } catch (IOException | MalformedRequestException | FileTransferException e) {
                pullIP = null;
            }
        }

        String wholeFileName = alphanumericFileName.substring(0,alphanumericFileName.indexOf("_")) + "_w";
        JSONObject fileInfo = JSONUtils.getItemContents(catalog, catalogReferences.get(0));
        int stripeNum = -1;
        if (alphanumericFileName.contains("-")) {
            stripeNum = Integer.parseInt(alphanumericFileName.substring(alphanumericFileName.indexOf("_") + 2));
        }
        if (pullIP == null && !alphanumericFileName.contains("_w") && manifestString.contains(wholeFileName)){
            //find, pull, and split a whole
            pullIP = findComputerWithFile(wholeFileName);
            try {
                FileClient.receiveFile(pullIP,portNumber, wholeFileName, MeshFS.properties.getProperty("repository") + File.separator + wholeFileName);

                //reference catalog to find number of stripes
                int numberOfStripes = 0;
                for (Object key : fileInfo.keySet()){
                    if (key.toString().contains("_s")){
                        numberOfStripes++;
                    }
                }
                long fileSize = Long.parseLong(fileInfo.get("fileSize").toString());
                long sizeOfStripe = ((fileSize / (numberOfStripes)) + 1);
                //write stripe

                if (stripeNum == numberOfStripes){
                    FileUtils.writeStripe(
                            MeshFS.properties.getProperty("repository") + File.separator + wholeFileName,
                            MeshFS.properties.getProperty("repository") + File.separator + alphanumericFileName,
                            (sizeOfStripe * stripeNum),
                            sizeOfStripe - ((sizeOfStripe * numberOfStripes) - fileSize));
                } else {
                    FileUtils.writeStripe(
                            MeshFS.properties.getProperty("repository") + File.separator + wholeFileName,
                            MeshFS.properties.getProperty("repository") + File.separator + alphanumericFileName,
                            (sizeOfStripe * stripeNum),
                            sizeOfStripe);
                }
                
                //delete whole file
                FileUtils.removeFile(MeshFS.properties.getProperty("repository") + File.separator + wholeFileName);


            } catch (IOException | MalformedRequestException | FileTransferException e) {
                pullIP = null;
            }
        }

        if (pullIP == null){
            //change catalog to read "corrupted"
            corruptFilesInCatalog(catalogReferences);
            return;
        }
        
        //determine where the file needs to be sent
        
        LinkedHashMap<String,Long> storageMap = JSONUtils.createStorageMap(manifest);
        for (Object key : fileInfo.keySet()){
            if (key.toString().equals("whole") || key.toString().contains("stripe_")){
                JSONArray stripeLocations = (JSONArray) fileInfo.get(key);
                for (Object computer : stripeLocations){
                    storageMap.remove(computer.toString());
                }
            }
        }

        String destinationCompIP = null;
        String destinationCompMAC = null;
        for (Object macAddress : storageMap.keySet()){
            String ipAddress = (((JSONObject) manifest.get(macAddress)).get("IP")).toString();
            if (FileClient.ping(ipAddress, portNumber) > -1 && storageMap.get(macAddress.toString()) > FileUtils.getSize(MeshFS.properties.getProperty("repository") + File.separator + alphanumericFileName)){
                destinationCompIP = ipAddress;
                destinationCompMAC = macAddress.toString();
                break;
            }
        }


        //send the file

        try {
            FileClient.sendFile(
                    destinationCompIP,
                    portNumber,
                    MeshFS.properties.getProperty("repository")
                            + File.separator
                            + alphanumericFileName,
                    MeshFS.properties.getProperty("remoteUUID"));
            FileClient.receiveReport(
                    (((JSONObject) manifest.get(destinationCompIP)).get("IP")).toString(),
                    portNumber);
        } catch (IOException | MalformedRequestException e) {
            //update to say corrupted
            corruptFilesInCatalog(catalogReferences);
            return;
        }
        
        //change the catalog

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
        return;
    }

    static String findComputerWithFile (String filename){
        JSONObject manifest = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");
        for (Object key : manifest.keySet()) {
            JSONObject computer = (JSONObject) manifest.get(key);
            if (computer.toString().contains(filename)){
                String IP = computer.get("IP").toString();
                if (FileClient.ping(IP, Integer.parseInt(MeshFS.properties.getProperty("portNumber"))) > -1){
                    return IP;
                }
            }
        }
        return null;
    }

    private static List<String> findFileReferencesInCatalog(JSONObject catalog, String alphanumericName){
        return fileReferenceFinderRecursive((JSONObject) catalog.get("root"), alphanumericName, "root");
    }

    private static List<String> fileReferenceFinderRecursive(JSONObject folder, String alphanumericName, String folderLocation){
        List<String> references = new ArrayList<>();
        LinkedHashMap<String, String> folderMap = JSONUtils.getMapOfFolderContents(folder, null);
        for (String item : folderMap.keySet()){
            if (folderMap.get(item).equals("directory")){
                references.addAll(fileReferenceFinderRecursive((JSONObject) folder.get(item), alphanumericName, folderLocation + "/" + item));
            } else if (folderMap.get(item).equals("file") && ((JSONObject) folder.get(item)).get("alphanumericName").toString().equals(alphanumericName)){
                references.add(folderLocation + "/" + item);
            }
        }
        return references;
    }

    private static void corruptFilesInCatalog(List<String> catalogReferences){
        for (String reference : catalogReferences){
            try {
                JSONUtils.renameItem(reference, reference.substring(reference.lastIndexOf("/")) + " (Corrupted)");
            } catch (IOException | MalformedRequestException e) {
                e.printStackTrace();
            }
        }
    }

    static List<String> getFilesOnComputerFromCatalog(String MACAddress){
        JSONObject catalog = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        if (catalog.toString().contains(MACAddress)) {
            return computerReferenceFinderRecursive(catalog, MACAddress);
        }
        return new ArrayList<>();
    }

    private static List<String> computerReferenceFinderRecursive(JSONObject folder, String MACAddress){
        List<String> fileNames = new ArrayList<>();
        LinkedHashMap<String, String> folderMap = JSONUtils.getMapOfFolderContents(folder, null);
        for (String item : folderMap.keySet()){

            if (folder.toString().contains(MACAddress)){
                if (folderMap.get(item).equals("directory")){
                    fileNames.addAll(computerReferenceFinderRecursive((JSONObject) folder.get(item), MACAddress));
                } else if (folderMap.get(item).equals("file")){
                    JSONObject fileInfo = ((JSONObject) folder.get(item));
                    for (Object key : fileInfo.keySet()){
                        if (fileInfo.get(key).toString().contains(MACAddress)){
                            if (key.toString().equals("whole")){
                                fileNames.add(((JSONObject) folder.get(item)).get("alphanumericName").toString() + "_w");
                            } else {
                                fileNames.add(((JSONObject) folder.get(item)).get("alphanumericName").toString() + "_" + key.toString().substring(key.toString().indexOf("_") + 1));
                            }
                        }
                    }
                }
            }
        }
        return fileNames;
    }
}

import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * The DISTAL class distributes a file across the linked
 * computers with a variable number of whole copies,
 * stripes, and striped copies.
 *
 * @author Aaron Duran
 * @version 1.0.0
 */

class DISTAL {

    /**
     * This method is used determines which load balance which
     * computers will receive a file and its stripes. The method
     * also sends the file to the appropriate computers and
     * updates the catalog file.
     *
     * @param uploadFilePath    the file path of the file
     * @param filePathInCatalog where the file is to be put in the catalog
     */

    static void distributor(String uploadFilePath, String filePathInCatalog) throws IOException, MalformedRequestException {

        filePathInCatalog = JSONUtils.catalogStringFixer(filePathInCatalog);

        int numOfStripes = Integer.parseInt(MeshFS.properties.getProperty("numStripes"));
        int numOfStripedCopies = Integer.parseInt(MeshFS.properties.getProperty("numStripeCopy"));
        int numOfWholeCopies = Integer.parseInt(MeshFS.properties.getProperty("numWholeCopy"));
        uploadFilePath = MeshFS.properties.getProperty("repository") + uploadFilePath;
        String manifestFileLocation = MeshFS.properties.getProperty("repository") + ".manifest.json";
        JSONObject manifestFile = JSONUtils.getJSONObject(manifestFileLocation);
        String catalogFileLocation = MeshFS.properties.getProperty("repository") + ".catalog.json";

        String newName = incrementName();

        JSONUtils.renameItem(filePathInCatalog + "/" + uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1) + " (uploading)",
                uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1) + " (distributing)");

        String fileName = uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1);
        long sizeOfFile = FileUtils.getSize(uploadFilePath);
        long sizeOfStripe;

        LinkedHashMap<String, Long> sortedCompStorageMap = JSONUtils.createStorageMap(manifestFile);

        if (sizeOfFile <= 4096L) {
            numOfWholeCopies += numOfStripedCopies;
            numOfStripes = 0;
            numOfStripedCopies = 0;
        }

        List<String> computersForWholes = new ArrayList<>();

        while (true) {
            int numOfComputersUsed = sortedCompStorageMap.size();

            if (numOfComputersUsed <= numOfStripedCopies + numOfWholeCopies) {
                numOfWholeCopies = numOfComputersUsed;
                numOfStripes = 0;
                numOfStripedCopies = 0;
            }

            if (numOfStripes == 1) {
                numOfWholeCopies += numOfStripedCopies;
                numOfStripes = 0;
                numOfStripedCopies = 0;
            }

            if (numOfComputersUsed < (numOfWholeCopies + (numOfStripedCopies * numOfStripes))) {
                numOfStripes = ((numOfComputersUsed - numOfWholeCopies) / numOfStripedCopies);
            }

            try {
                sizeOfStripe = ((sizeOfFile / numOfStripes) + 1);
            } catch (Exception e) {
                sizeOfStripe = 0L;
            }

            for (String macAddress : sortedCompStorageMap.keySet()) {
                if (computersForWholes.size() == numOfWholeCopies) {
                    break;
                }
                if ((!computersForWholes.contains(macAddress)) && sortedCompStorageMap.get(macAddress) >= sizeOfFile) {
                    computersForWholes.add(macAddress);
                }
            }

            for (String macAddress : computersForWholes) {
                sortedCompStorageMap.remove(macAddress);
            }

            boolean finalComputerCount = true;

            if (sizeOfStripe != 0L) {
                List<String> removedIPs = new ArrayList<>();

                for (String macAddress : sortedCompStorageMap.keySet()) {
                    if (sortedCompStorageMap.get(macAddress) < sizeOfStripe) {
                        removedIPs.add(macAddress);
                        finalComputerCount = false;
                    }
                }

                for (String IP : removedIPs) {
                    sortedCompStorageMap.remove(IP);
                }

            }

            if (finalComputerCount) {
                break;
            }
        }

        if (numOfStripedCopies == 0 && numOfWholeCopies == 0) {
            JSONUtils.renameItem(filePathInCatalog + "/" + uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1) + " (distributing)", uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1) + " (distribution failed)");
            return;
        }

        List<String> computersForStripes = new ArrayList<>();

        for (String macAddress : sortedCompStorageMap.keySet()) {
            if (computersForStripes.size() == numOfStripedCopies * numOfStripes) {
                break;
            }
            computersForStripes.add(macAddress);
        }

        List<List<String>> stripes = new ArrayList<>();

        stripes.add(computersForWholes);

        if (numOfStripes != 0) {

            for (int copy = 0; copy < numOfStripes; copy++) {
                stripes.add(new ArrayList<>());
            }

            for (int copy = 0; copy < numOfStripedCopies; copy++) {
                for (int currentStripe = 0; currentStripe < numOfStripes; currentStripe++) {
                    stripes
                            .get(currentStripe + 1)
                            .add(computersForStripes.get((copy * numOfStripes) + currentStripe));
                }
            }
        }

        sendFiles(stripes, uploadFilePath, sizeOfFile, newName);

        JSONUtils.deleteItem(filePathInCatalog + "/" + uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1) + " (distributing)", false);
        JSONUtils.addFileToCatalog(
                stripes,
                filePathInCatalog,
                fileName,
                catalogFileLocation,
                newName,
                sizeOfFile);
    }

    @SuppressWarnings("unchecked")
    private static String incrementName() throws IOException {
        JSONObject catalog = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        String name = catalog.get("currentName").toString();

        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int incrementReverseIndex = 1;
        StringBuilder toAdd = new StringBuilder();

        while (true) {
            try {
                char lastChar = name.charAt(name.length() - incrementReverseIndex);
                toAdd.append(alphabet.charAt(alphabet.indexOf(lastChar) + 1));
                break;
            } catch (IndexOutOfBoundsException ioobe) {
                toAdd.append(alphabet.charAt(0));
                incrementReverseIndex++;
            }
        }
        StringBuilder newName = new StringBuilder(name.substring(0, name.length() - (toAdd.length())));
        for (int reverseIndex = (toAdd.length() - 1); reverseIndex >= 0; reverseIndex--) {
            newName.append(toAdd.charAt(reverseIndex));
        }
        catalog.replace("currentName", newName.toString());
        JSONUtils.writeJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json", catalog);
        return name;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void sendFiles(
            List<List<String>> stripes, String sourceFileLocationOld, long fileSize, String outName) {
        List<Thread> parentThreads = new ArrayList<>();

        long sizeOfStripe;
        try {
            sizeOfStripe = ((fileSize / (stripes.size() - 1) + 1));
        } catch (Exception e) {
            sizeOfStripe = 0L;
        }

        final String sourceFileLocation =
                sourceFileLocationOld.substring(0, sourceFileLocationOld.lastIndexOf(File.separator) + 1)
                        + outName
                        + "_w";

        new File(sourceFileLocationOld).renameTo(new File(sourceFileLocation));

        JSONObject manifestFile =
                JSONUtils.getJSONObject(
                        MeshFS.properties.getProperty("repository") + ".manifest.json");

        for (int stripe = -1; stripe < (stripes.size() - 1); stripe++) {
            parentThreads.add(
                    new Thread(
                            new sendFilesThreading(
                                    sizeOfStripe,
                                    fileSize,
                                    sourceFileLocation,
                                    manifestFile,
                                    stripes,
                                    stripe,
                                    outName)));
        }
        for (Thread parent : parentThreads) {
            parent.start();
        }

        for (Thread parent : parentThreads) {
            if (parent.isAlive()) {
                try {
                    parent.join();
                } catch (InterruptedException ignored) {
                }
            }
        }

        FileUtils.removeFile(sourceFileLocation);
    }
}

/**
 * The sendFilesThreading class writes stripes of a file and
 * sends the whole and the stripes to the specified computers.
 *
 * @author Aaron Duran
 * @version 1.0.0
 */

class sendFilesThreading implements Runnable {
    private final long sizeOfStripe;
    private final long fileSize;
    private final String sourceFileLocation;
    private final JSONObject manifestFile;
    private final List<List<String>> stripes;
    private final int stripe;
    private final String outName;

    /**
     * This is used to set all of the class variables.
     *
     * @param sizeOfStripe       the size of each stripe in bytes
     * @param fileSize           the size of the full file in bytes
     * @param sourceFileLocation the file path of the file  on the master computer
     * @param manifestFile       the JSONObject of the the manifest file
     * @param stripes            the list of lists of strings that store where the file and stripes should be send
     * @param stripe             what stripe is to be sent, -1 sends the whole
     * @param outName            the alphanumeric name of the file
     */

    sendFilesThreading(
            long sizeOfStripe,
            long fileSize,
            String sourceFileLocation,
            JSONObject manifestFile,
            List<List<String>> stripes,
            int stripe,
            String outName) {
        this.sizeOfStripe = sizeOfStripe;
        this.fileSize = fileSize;
        this.sourceFileLocation = sourceFileLocation;
        this.manifestFile = manifestFile;
        this.stripes = stripes;
        this.stripe = stripe;
        this.outName = outName;
    }

    private void writeSendStripe() throws IOException {
        List<Thread> childThreads = new ArrayList<>();

        if (stripe == -1) {
            for (String computerToReceive : stripes.get(stripe + 1)) {
                Thread child =
                        new Thread(
                                () -> {
                                    try {
                                        try {
                                            FileClient.sendFile(
                                                    (((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(),
                                                    Integer.parseInt(MeshFS.properties.getProperty("portNumber")),
                                                    MeshFS.properties.getProperty("repository")
                                                            + File.separator
                                                            + outName
                                                            + "_w");
                                        } catch (MalformedRequestException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            FileClient.receiveReport(
                                                    (((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(),
                                                    Integer.parseInt(MeshFS.properties.getProperty("portNumber")));
                                        } catch (MalformedRequestException e) {
                                            e.printStackTrace();
                                        }
                                    } catch (IOException ioe) {
                                        ioe.printStackTrace();
                                    }
                                });

                childThreads.add(child);
            }
        } else {
            if (stripe == stripes.size() - 2) {
                FileUtils.writeStripe(
                        sourceFileLocation,
                        MeshFS.properties.getProperty("repository") + outName + "_s" + stripe,
                        (sizeOfStripe * stripe),
                        sizeOfStripe - ((sizeOfStripe * (stripes.size() - 1)) - fileSize));

            } else {
                FileUtils.writeStripe(
                        sourceFileLocation,
                        MeshFS.properties.getProperty("repository") + outName + "_s" + stripe,
                        (sizeOfStripe * stripe),
                        sizeOfStripe);
            }
            for (String computerToReceive : stripes.get(stripe + 1)) {
                Thread child =
                        new Thread(
                                () -> {
                                    try {
                                        FileClient.sendFile(
                                                (((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(),
                                                Integer.parseInt(MeshFS.properties.getProperty("portNumber")),
                                                MeshFS.properties.getProperty("repository")
                                                        + File.separator
                                                        + outName
                                                        + "_s"
                                                        + stripe);
                                        FileClient.receiveReport(
                                                (((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(),
                                                Integer.parseInt(MeshFS.properties.getProperty("portNumber")));
                                    } catch (IOException | MalformedRequestException ioe) {
                                        ioe.printStackTrace();
                                    }
                                });
                childThreads.add(child);
            }
        }

        for (Thread child : childThreads) {
            child.start();
        }

        for (Thread child : childThreads) {
            if (child.isAlive()) {
                try {
                    child.join();
                } catch (InterruptedException ignored) {
                }
            }
        }
        FileUtils.removeFile(
                MeshFS.properties.getProperty("repository") + outName + "_s" + stripe);
    }

    /**
     * This is used to start writing and sending the stripes
     */

    public void run() {
        try {
            writeSendStripe();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}

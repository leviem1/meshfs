/**
 * Created by Aaron Duran on 10/13/16.
 */
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

class DISTAL {

    private static LinkedHashMap<String, Long> valueSorter(LinkedHashMap<String,Long> storageMap){

        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap();
        sortedMap.put("temp", 99999999999990L);

        for (String key : storageMap.keySet()){
            Long storageAmount = storageMap.get(key);
            List<String> moreStorage = new ArrayList<>();
            boolean isBroken = false;

            for (String sortedKey : sortedMap.keySet()){
                if (storageAmount >= sortedMap.get(sortedKey)){
                    LinkedHashMap<String,Long>  reorderStorageMap = (LinkedHashMap<String,Long>) sortedMap.clone();
                    sortedMap.clear();
                    for (String reorderKey : reorderStorageMap.keySet()){
                        if (reorderKey.equals(sortedKey)){
                            sortedMap.put(key,storageAmount);
                        }
                        sortedMap.put(reorderKey, reorderStorageMap.get(reorderKey));
                    }
                    isBroken = true;
                    break;
                }
                moreStorage.add(sortedKey);
            }

            if (!isBroken){
                sortedMap.put(key,storageMap.get(key));
            }
        }
        sortedMap.remove("temp");
        return sortedMap;
    }

    static void distributor(String uploadFilePath, String filePathInCatalog){
        String userAccount;
        try {
            userAccount = filePathInCatalog.substring(0,filePathInCatalog.indexOf("/"));
        }
        catch (Exception e){
            userAccount = filePathInCatalog;
        }

        int numOfStripes = Integer.valueOf(MeshFS.properties.getProperty("numStripes"));
        int numOfStripedCopies = Integer.valueOf(MeshFS.properties.getProperty("numStripeCopy"));
        int numOfWholeCopies = Integer.valueOf(MeshFS.properties.getProperty("numWholeCopy"));
        long minFreeSpace = Integer.valueOf(MeshFS.properties.getProperty("minSpace")) * 1073741824;
        uploadFilePath = MeshFS.properties.getProperty("repository") + uploadFilePath;
        String manifestFileLocation = MeshFS.properties.getProperty("repository")+".manifest.json";
        JSONObject manifestFile = JSONManipulator.getJSONObject(manifestFileLocation);
        String catalogFileLocation = MeshFS.properties.getProperty("repository")+".catalog.json";
        JSONManipulator.addToIndex(userAccount, uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator)+1) + " (distributing)", catalogFileLocation, userAccount, true);

        try {
             String fileName = uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1);
             long sizeOfFile = FileUtils.getSize(uploadFilePath);
             //long sizeOfFile = 5000000L;
             long sizeOfStripe = ((sizeOfFile / numOfStripes) + 1);

             LinkedHashMap<String, Long> compStorageMap = JSONManipulator.createStorageMap(manifestFile);
             LinkedHashMap<String, Long> sortedCompStorageMap = valueSorter(compStorageMap); //sort the compStorageMap by descending available storage

            for (int storage = 0; storage < sortedCompStorageMap.size(); storage++){ // account for the desired amount of free space
                 String macAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[storage]);
                 sortedCompStorageMap.replace(macAddress, sortedCompStorageMap.get(String.valueOf(macAddress)) - minFreeSpace);
            }
             /*//uncomment me for dynamic resigning of numStripes by number of computers that are on
             int numOfComputersUsed = sortedCompStorageMap.size();
             if (numOfComputersUsed < (numOfWholeCopies + (numOfStripedCopies * numOfStripes))) {
                 numOfStripes = ((numOfComputersUsed - numOfWholeCopies) / numOfStripedCopies);
             }
             //*/
             //create a unique filename for the uploaded file
             List<String> computersForWholes = new ArrayList<>();
             JSONObject jsonObj = JSONManipulator.getJSONObject(catalogFileLocation);
             String currentName = jsonObj.get("currentName").toString();
             String newName = incrementName(currentName);

             //determine which computers can hold the full file
            int stopOfWholes = (-1);
            for (int computerNumW = 0; computerNumW < numOfWholeCopies; computerNumW++) {
                String macAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumW]);
                if (sortedCompStorageMap.get(macAddress) >= sizeOfFile) {
                     computersForWholes.add(macAddress);
                }
                else {
                    break;
                }
                stopOfWholes = computerNumW;
            }

            int lastResortComp = 0;
            int lapNum = 1;
            List<String> computersForStripes = new ArrayList<>();
            for (int computerNumS = stopOfWholes+1; computerNumS < ((numOfStripes * numOfStripedCopies) + stopOfWholes+1); computerNumS++) {
                String macAddress;

                try{
                    macAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumS]);
                } catch (Exception e){
                    macAddress = "none";
                }

                if ((!macAddress.equals("none") && (sortedCompStorageMap.get(macAddress) - (sizeOfStripe * lapNum)) >= sizeOfStripe)) {
                    computersForStripes.add(macAddress);
                } else if (computerNumS != 0){
                    String macAddressNew;
                    long availableStorage;

                    try {
                        macAddressNew = String.valueOf(sortedCompStorageMap.keySet().toArray()[lastResortComp]);
                        availableStorage = (sortedCompStorageMap.get(macAddressNew) - (sizeOfStripe * lapNum));

                        if (lastResortComp <= stopOfWholes){
                            availableStorage -= sizeOfFile;
                        }
                        lastResortComp++;
                    } catch (Exception e){
                        availableStorage = 0L;
                        macAddressNew = "none";
                    }


                    if (availableStorage >= sizeOfStripe) {
                        computersForStripes.add(macAddressNew);
                    }
                    else {
                        lapNum++;
                        lastResortComp = 0;

                        while (lastResortComp < sortedCompStorageMap.size()){
                            macAddressNew = String.valueOf(sortedCompStorageMap.keySet().toArray()[lastResortComp]);
                            availableStorage = (sortedCompStorageMap.get(macAddressNew) - (sizeOfStripe * lapNum));
                            if (lastResortComp <= stopOfWholes){
                                availableStorage -= sizeOfFile;
                            }

                            lastResortComp++;

                            if (availableStorage >= sizeOfStripe) {
                                computersForStripes.add(macAddressNew);
                                break;
                            }
                        }
                        if (lastResortComp >= sortedCompStorageMap.size()){
                            break;
                        }
                    }
                }
                else{
                    System.out.println("There is no storage space that is available for this file.");
                    break;
                }
            }

            List<List<String>> stripes = new ArrayList<>();
            stripes.add(computersForWholes);

            boolean allowStripes = false;
            for (String computer : computersForStripes){
                if (!computer.equals(computersForStripes.get(0))){
                    allowStripes = true;
                    break;
                }
            }

             if (allowStripes){
                 for (int copy = 0; copy < numOfStripes; copy++) {
                     stripes.add(new ArrayList<>());
                 }
                 for (int copy = 0; copy < numOfStripedCopies; copy++) {
                     for (int currentStripe = 0; currentStripe < numOfStripes; currentStripe++){
                         try{
                             int test = 0;

                             while (true) {
                                 test++;
                                 if (test >= (computersForStripes.size() - (copy * numOfStripes) + currentStripe)){
                                     break;
                                 }

                                 String computerToReceive = computersForStripes.get((copy * numOfStripes) + currentStripe);

                                 if (stripes.get(currentStripe + 1).size() > 0){
                                     boolean isNotBroken = true;
                                     for (String tempMac : stripes.get(currentStripe + 1)) {
                                         if (tempMac.equals(computerToReceive)) {
                                             computersForStripes.add(tempMac);
                                             computersForStripes.remove((copy * numOfStripes) + currentStripe);
                                             isNotBroken = false;
                                             break;
                                         }
                                     }
                                     if (isNotBroken){
                                         stripes.get(currentStripe + 1).add(computerToReceive);
                                         break;
                                     }
                                 } else {
                                     stripes.get(currentStripe + 1).add(computerToReceive);
                                     break;
                                 }
                             }
                         } catch (Exception e) {
                             System.out.println("You are out of Storage!");
                         }
                     }
                 }
             }
            sendFiles(stripes,uploadFilePath,sizeOfFile,newName);
            String itemLocation = userAccount + "/" + uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator)+1) + " (distributing)";
            JSONManipulator.writeJSONObject(catalogFileLocation, JSONManipulator.removeItem(jsonObj, itemLocation));
            JSONManipulator.addToIndex(stripes, filePathInCatalog, fileName, catalogFileLocation, newName, userAccount, sizeOfFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String incrementName(String name){
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int incrementReverseIndex = 1;
        String toAdd = "";


        while(true) {
            try {
                char lastChar = name.charAt(name.length()-incrementReverseIndex);
                toAdd += alphabet.charAt(alphabet.indexOf(lastChar) + 1);
                break;
            } catch (IndexOutOfBoundsException iobe) {
                toAdd += alphabet.charAt(0);
                incrementReverseIndex++;
            }
        }
        String newName = name.substring(0,name.length()-(toAdd.length()));
        for (int reverseIndex = (toAdd.length()-1); reverseIndex >= 0; reverseIndex--) {
            newName += toAdd.charAt(reverseIndex);
        }
        return newName;
    }

    private static void sendFiles (List<List<String>> stripes, String sourceFileLocationOld, long fileSize, String outName){
        List<Thread> parentThreads = new ArrayList<>();
        long sizeOfStripe = ((fileSize / (stripes.size() -1) + 1));
        new File(sourceFileLocationOld).renameTo(new File(sourceFileLocationOld.substring(0,sourceFileLocationOld.lastIndexOf(File.separator)+1) + outName + "_w"));
        final String sourceFileLocation = sourceFileLocationOld.substring(0, sourceFileLocationOld.lastIndexOf(File.separator) + 1) + outName + "_w";
        JSONObject manifestFile = JSONManipulator.getJSONObject(MeshFS.properties.getProperty("repository")+".manifest.json");

        for (int stripe = -1; stripe < (stripes.size()-1); stripe++){
            parentThreads.add(new Thread(new sendFilesTreading(sizeOfStripe, fileSize, sourceFileLocation, manifestFile, stripes, stripe, outName)));
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

class sendFilesTreading implements Runnable{
    private long sizeOfStripe;
    private long fileSize;
    private String sourceFileLocation;
    private JSONObject manifestFile;
    private List<List<String>> stripes;
    private int stripe;
    private String outName;

    public sendFilesTreading(long sizeOfStripe, long fileSize, String sourceFileLocation, JSONObject manifestFile, List<List<String>> stripes, int stripe, String outName){
        this.sizeOfStripe = sizeOfStripe;
        this.fileSize = fileSize;
        this.sourceFileLocation = sourceFileLocation;
        this.manifestFile = manifestFile;
        this.stripes = stripes;
        this.stripe = stripe;
        this.outName = outName;
    }

    public void writeSendStripe() throws IOException{
        List<Thread> childThreads = new ArrayList<>();

        if (stripe == -1){
            for (String computerToReceive : stripes.get(stripe+1)) {
                Thread child = new Thread(() -> {
                    try {
                        FileClient.sendFile((((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.getProperty("portNumber")), MeshFS.properties.getProperty("repository") + File.separator + outName + "_w");
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                });

                childThreads.add(child);
            }
        }
        else if (stripe == stripes.size()-2) {

            FileUtils.writeStripe(sourceFileLocation, MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe, (sizeOfStripe * stripe), sizeOfStripe - ((sizeOfStripe * (stripes.size() - 1)) - fileSize));
            for (String computerToReceive : stripes.get(stripe+1)){
                Thread child = new Thread(() -> {
                    try {
                        FileClient.sendFile((((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.getProperty("portNumber")), MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                });
                childThreads.add(child);
            }
        }
        else {
            FileUtils.writeStripe(sourceFileLocation, MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe, (sizeOfStripe * stripe), sizeOfStripe);
            for (String computerToReceive : stripes.get(stripe + 1)) {
                Thread child = new Thread(() -> {
                    try {
                        FileClient.sendFile((((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.getProperty("portNumber")), MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe);
                    } catch (IOException ioe) {
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
        FileUtils.removeFile(MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe);

    }

    public void run() {
        try {
            writeSendStripe();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
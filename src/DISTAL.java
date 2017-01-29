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

        //put something in the map to compare against
        sortedMap.put("temp", 99999999999999L);

        for (String key : storageMap.keySet()){
            Long storageAmount = storageMap.get(key);
            List<String> moreStorage = new ArrayList<>();
            boolean isBroken = false;

            for (String sortedKey : sortedMap.keySet()){
                if (storageAmount >= sortedMap.get(sortedKey)){
                    //reorder the map when a storage value is larger than one that is already in the map
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
        //remove the temporary key, value combination
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

        //import properties
        int numOfStripes = Integer.valueOf(MeshFS.properties.getProperty("numStripes"));
        int numOfStripedCopies = Integer.valueOf(MeshFS.properties.getProperty("numStripeCopy"));
        int numOfWholeCopies = Integer.valueOf(MeshFS.properties.getProperty("numWholeCopy"));
        long minFreeSpace = Integer.valueOf(MeshFS.properties.getProperty("minSpace")) * 1073741824;
        uploadFilePath = MeshFS.properties.getProperty("repository") + uploadFilePath;
        String manifestFileLocation = MeshFS.properties.getProperty("repository")+".manifest.json";
        JSONObject manifestFile = JSONManipulator.getJSONObject(manifestFileLocation);
        String catalogFileLocation = MeshFS.properties.getProperty("repository")+".catalog.json";

        //make the JTree show that the file is being distributed
        JSONManipulator.addToIndex(userAccount, uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator)+1) + " (distributing)", catalogFileLocation, userAccount, true);

        try {
            String fileName = uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1);
            long sizeOfFile = FileUtils.getSize(uploadFilePath);
            long sizeOfStripe = ((sizeOfFile / numOfStripes) + 1);

            //create a map of the amount of available storage on each computer
            LinkedHashMap<String, Long> compStorageMap = JSONManipulator.createStorageMap(manifestFile);

            //sort the compStorageMap by descending available storage
            LinkedHashMap<String, Long> sortedCompStorageMap = valueSorter(compStorageMap);

            for (int storage = 0; storage < sortedCompStorageMap.size(); storage++){
                // account for the desired amount of free space
                String macAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[storage]);
                sortedCompStorageMap.replace(macAddress, sortedCompStorageMap.get(String.valueOf(macAddress)) - minFreeSpace);
            }

            int numOfComputersUsed = sortedCompStorageMap.size();

            //don't use stripes if a file is less than 4096 byte
            if (sizeOfFile <= 4096L){
                numOfWholeCopies += numOfStripedCopies;
                numOfStripes = 0;
                numOfStripedCopies = 0;
            }

            //use stripes only when the number of computers available exceeds the number of requested redundancies
            if(numOfComputersUsed <= numOfStripedCopies + numOfWholeCopies){
                numOfWholeCopies = numOfComputersUsed;
                numOfStripes = 0;
                numOfStripedCopies = 0;
            }

            //dynamic resigning of number of Wholes by number of computers that are on
            if (numOfComputersUsed < numOfWholeCopies){
                numOfWholeCopies = numOfComputersUsed;
            }

            //dynamic resigning of number of Stripes by number of computers that are on
            if (numOfComputersUsed < (numOfWholeCopies + (numOfStripedCopies * numOfStripes))) {
                numOfStripes = ((numOfComputersUsed - numOfWholeCopies) / numOfStripedCopies);
            }



            //don't use stripes if there is only one stripe
            if(numOfStripes == 1){
                numOfWholeCopies += numOfStripedCopies;
                numOfStripes = 0;
                numOfStripedCopies = 0;
            }

            System.out.println("comps " + numOfComputersUsed);
            System.out.println("whole " + numOfWholeCopies);
            System.out.println("stripe copies " + numOfStripedCopies);
            System.out.println("stripe " + numOfStripes);

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

            //determine which computers can hold a striped file
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

                    //find a computer that can hold the stripe, even if one computer gets two stripes
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

                    //iterate over all the computers again to find a place for the stripe, if a computer has not already been found
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
                            //no more computers have the storage for a stripe
                            break;
                        }
                    }
                }
                else{
                    System.out.println("There is no storage space that is available for this file.");
                    break;
                }
            }


            //create the list that will be used to distribute the wholes and stripes
            List<List<String>> stripes = new ArrayList<>();

            //frist list is for the computers that will receive wholes
            stripes.add(computersForWholes);

            //if only computer is available to hold stripes, then do not use stripes.
            boolean allowStripes = false;
            for (String computer : computersForStripes){
                if (!computer.equals(computersForStripes.get(0))){
                    allowStripes = true;
                    break;
                }
            }


            if (allowStripes){
                //create a list for each stripe
                for (int copy = 0; copy < numOfStripes; copy++) {
                    stripes.add(new ArrayList<>());
                }

                //balancing the number of computers that each stripe is sent to.
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

                                //ensure that no one computer gets two or more of the same stripe
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

            //send the files to the respective computers
            sendFiles(stripes,uploadFilePath,sizeOfFile,newName);

            //update the JSON file in order to update the JTree
            JSONManipulator.writeJSONObject(catalogFileLocation, JSONManipulator.removeItem(jsonObj, userAccount + "/" + uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator)+1) + " (distributing)"));
            JSONManipulator.addToIndex(stripes, filePathInCatalog, fileName, catalogFileLocation, newName, userAccount, sizeOfFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String incrementName(String name){
        //creates a new unique filename
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int incrementReverseIndex = 1;
        String toAdd = "";

        while(true) {
            try {
                //advance to the next character.
                char lastChar = name.charAt(name.length()-incrementReverseIndex);
                toAdd += alphabet.charAt(alphabet.indexOf(lastChar) + 1);
                break;
            } catch (IndexOutOfBoundsException iobe) {
                //if have reached the end of the alphabet, loop back to the beginning and use the next character
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

        long sizeOfStripe;
        try{
            sizeOfStripe = ((fileSize / (stripes.size() - 1) + 1));
        }catch(Exception e){
            sizeOfStripe = 0L;
        }
        //rename the original file to what the distributed whole file will be

        new File(sourceFileLocationOld).renameTo(new File(sourceFileLocationOld.substring(0,sourceFileLocationOld.lastIndexOf(File.separator)+1) + outName + "_w"));
        final String sourceFileLocation = sourceFileLocationOld.substring(0, sourceFileLocationOld.lastIndexOf(File.separator) + 1) + outName + "_w";
        JSONObject manifestFile = JSONManipulator.getJSONObject(MeshFS.properties.getProperty("repository")+".manifest.json");

        //create a new thread for each stripe, so all stripes are written simultaneously
        for (int stripe = -1; stripe < (stripes.size()-1); stripe++){
            parentThreads.add(new Thread(new sendFilesTreading(sizeOfStripe, fileSize, sourceFileLocation, manifestFile, stripes, stripe, outName)));
        }
        for (Thread parent : parentThreads) {
            parent.start();
        }

        //wait for all the treads to close
        for (Thread parent : parentThreads) {
            if (parent.isAlive()) {
                try {
                    parent.join();
                } catch (InterruptedException ignored) {
                }
            }
        }

        //delete the original file
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
            //send the wholes
            for (String computerToReceive : stripes.get(stripe+1)) {
                Thread child = new Thread(() -> {
                    try {
                        FileClient.sendFile((((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.getProperty("portNumber")), MeshFS.properties.getProperty("repository") + File.separator + outName + "_w");
                        FileClient.receiveReport((((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.getProperty("portNumber")));
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                });

                childThreads.add(child);
            }
        }
        else if (stripe == stripes.size()-2) {
            //send the last stripe, taking into account that he the last stripe to usually smaller than other stripes.
            FileUtils.writeStripe(sourceFileLocation, MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe, (sizeOfStripe * stripe), sizeOfStripe - ((sizeOfStripe * (stripes.size() - 1)) - fileSize));
            for (String computerToReceive : stripes.get(stripe + 1)){
                Thread child = new Thread(() -> {
                    try {
                        FileClient.sendFile((((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.getProperty("portNumber")), MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe);
                        FileClient.receiveReport((((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.getProperty("portNumber")));
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                });
                childThreads.add(child);
            }
        }
        else {
            //send all other stripes
            FileUtils.writeStripe(sourceFileLocation, MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe, (sizeOfStripe * stripe), sizeOfStripe);
            for (String computerToReceive : stripes.get(stripe + 1)) {
                Thread child = new Thread(() -> {
                    try {
                        FileClient.sendFile((((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.getProperty("portNumber")), MeshFS.properties.getProperty("repository") + File.separator + outName + "_s" + stripe);
                        FileClient.receiveReport((((JSONObject) manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.getProperty("portNumber")));
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                });

                childThreads.add(child);
            }
        }

        //send all copies of stripe simultaneously
        for (Thread child : childThreads) {
            child.start();
        }

        //wait for all copies to finish sending before deleting the stripe on the master
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
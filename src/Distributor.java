/**
 * Created by Aaron Duran on 10/13/16.
 */
import org.json.simple.JSONObject;

import java.io.File;
import java.util.*;

public class Distributor {

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

    public static void distributor(String uploadFilePath, String filePathInCatalog){
        String userAccount;
        try {
            userAccount = filePathInCatalog.substring(0,filePathInCatalog.indexOf("/"));
        }
        catch (Exception e){
            userAccount = filePathInCatalog;
        }
        int numOfStripes = Integer.valueOf(MeshFS.properties.get("numStripes").toString());
        int numOfStripedCopies = Integer.valueOf(MeshFS.properties.get("numStripeCopy").toString());
        int numOfWholeCopies = Integer.valueOf(MeshFS.properties.get("numWholeCopy").toString());
        long minFreeSpace = Integer.valueOf(MeshFS.properties.get("minSpace").toString());
        uploadFilePath = MeshFS.properties.get("repository").toString() + uploadFilePath;
        String manifestFileLocation = "manifest.json";
        String DestinationRepoLocation = MeshFS.properties.get("repository").toString();
        JSONObject manifestFile = JSONManipulator.getJSONObject(manifestFileLocation);
        String catalogFileLocation = MeshFS.properties.get("repository").toString()+".catalog.json";
        try {
             String fileName = uploadFilePath.substring(uploadFilePath.lastIndexOf(File.separator) + 1);
             long sizeOfFile = FileUtils.getSize(uploadFilePath);
             //long sizeOfFile = 5000000L;
             long sizeOfStripe = ((sizeOfFile / numOfStripes) + 1);

             LinkedHashMap<String, Long> compStorageMap = JSONManipulator.createStorageMap(manifestFile);
             compStorageMap.put("0.0.0.0",0L);
             LinkedHashMap<String, Long> sortedCompStorageMap = valueSorter(compStorageMap); //sort the compStorageMap by descending available storage
             for (int storage = 0; storage < sortedCompStorageMap.size(); storage++){ // account for the desired amount of free space
                 String macAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[storage]);
                 sortedCompStorageMap.replace(macAddress, sortedCompStorageMap.get(String.valueOf(macAddress)) - minFreeSpace);
             }
             /* //uncomment me for dynamic resigning of numStripes by number of computers that are on
             int numOfComputersUsed = sortedCompStorageMap.size();
             if (numOfComputersUsed < (numOfWholeCopies + (numOfStripedCopies * numOfStripes))) {
                 numOfStripes = ((numOfComputersUsed - numOfWholeCopies) / numOfStripedCopies);
             }
             */
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
             int lapNum = 0;
             List<String> computersForStripes = new ArrayList<>();
             for (int computerNumS = stopOfWholes+1; computerNumS < ((numOfStripes * numOfStripedCopies) + stopOfWholes+1); computerNumS++) {
                 String macAddress;
                 try{
                     macAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumS]);
                 }
                 catch (Exception e){
                     macAddress = "0.0.0.0";
                 }
                 if ((sortedCompStorageMap.get(macAddress) - (sizeOfStripe * lapNum)) >= sizeOfStripe) {
                     computersForStripes.add(macAddress);
                 }
                 else if (computerNumS != 0){
                     macAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[lastResortComp]);
                     long availableStorage = (sortedCompStorageMap.get(macAddress) - (sizeOfStripe * lapNum));
                     if (lastResortComp <= stopOfWholes){
                         availableStorage -= sizeOfFile;
                     }

                     lastResortComp++;

                     if (availableStorage >= sizeOfStripe) {
                         computersForStripes.add(macAddress);
                     }
                     else {
                         lapNum++;
                         lastResortComp = 0;

                         while (lastResortComp < sortedCompStorageMap.size()){
                             macAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[lastResortComp]);
                             availableStorage = (sortedCompStorageMap.get(macAddress) - (sizeOfStripe * lapNum));
                             if (lastResortComp <= stopOfWholes){
                                 availableStorage -= sizeOfFile;
                             }
                             lastResortComp++;
                             if (availableStorage >= sizeOfStripe) {
                                 computersForStripes.add(macAddress);
                                 break;
                             }
                         }
                         if (lastResortComp >= sortedCompStorageMap.size()){
                             break;
                         }
                     }
                 }
                 else{
                     System.out.println("the is no storage space that is available for this file.");
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

             for (String computerToReceive : computersForWholes) {
                 FileUtils.writeStripe(uploadFilePath, DestinationRepoLocation + File.separator + newName + "_w", 0L, sizeOfFile);
                 FileClient.sendFile((((JSONObject)manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.get("portNumber").toString()), DestinationRepoLocation + File.separator + newName + "_w");
                 FileUtils.removeFile(DestinationRepoLocation + File.separator + newName + "_w");
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
                                         long startByte = (sizeOfStripe * currentStripe);
                                         if (currentStripe == numOfStripes-1){
                                             FileUtils.writeStripe(uploadFilePath, DestinationRepoLocation + File.separator + newName + "_s" + currentStripe, startByte, sizeOfStripe - ((sizeOfStripe * numOfStripes) - sizeOfFile ));
                                         }
                                         else {
                                             FileUtils.writeStripe(uploadFilePath, DestinationRepoLocation + File.separator + newName + "_s" + currentStripe, startByte, sizeOfStripe);
                                         }
                                         FileClient.sendFile((((JSONObject)manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.get("portNumber").toString()), DestinationRepoLocation + File.separator + newName + "_s" + currentStripe);
                                         FileUtils.removeFile(DestinationRepoLocation + File.separator + newName + "_s" + currentStripe);
                                         break;
                                     }
                                 }
                                 else {
                                     stripes.get(currentStripe + 1).add(computerToReceive);
                                     long startByte = (sizeOfStripe * currentStripe);
                                     if (currentStripe == numOfStripes-1){
                                         FileUtils.writeStripe(uploadFilePath, DestinationRepoLocation + File.separator + newName + "_s" + currentStripe, startByte, sizeOfStripe - ((sizeOfStripe * numOfStripes) - sizeOfFile ));
                                     }
                                     else {
                                         FileUtils.writeStripe(uploadFilePath, DestinationRepoLocation + File.separator + newName + "_s" + currentStripe, startByte, sizeOfStripe);
                                     }
                                     FileClient.sendFile((((JSONObject)manifestFile.get(computerToReceive)).get("IP")).toString(), Integer.valueOf(MeshFS.properties.get("portNumber").toString()), DestinationRepoLocation + File.separator + newName + "_s" + currentStripe);
                                     FileUtils.removeFile(DestinationRepoLocation + File.separator + newName + "_s" + currentStripe);
                                     break;
                                 }
                             }
                         }
                         catch (Exception e) {
                             e.printStackTrace();
                             System.out.println("You are out of Storage!");
                         }
                     }
                 }
             }
             FileUtils.removeFile(uploadFilePath);
             JSONManipulator.addToIndex(stripes,filePathInCatalog, fileName, catalogFileLocation, newName, userAccount);
         }
         catch (Exception e) {
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
}
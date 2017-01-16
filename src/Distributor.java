/**
 * Created by Aaron Duran on 10/13/16.
 */
import org.json.simple.JSONObject;

import java.util.*;

public class Distributor {

    private int numOfStripes;
    private int numOfWholeCopies;
    private int numOfStripedCopies;
    private long minFreeSpace = 0L; // 5GB is 5368709120L

    Distributor(int numOfStripes, int numOfWholeCopies, int numOfStripedCopies){
        this.numOfStripes = numOfStripes;
        this.numOfWholeCopies = numOfWholeCopies;
        this.numOfStripedCopies = numOfStripedCopies;
    }

    private LinkedHashMap<String, Long> valueSorter(LinkedHashMap<String,Long> storageMap){
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

    public void distributor(LinkedHashMap<String, Long> compStorageMap, String filePath, String filePathInCatalog, String JSONFilePath, String DestinationRepoLocation){
         try {
             String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
             DestinationRepoLocation += fileName;
             //FileReader reader = new FileReader(filePath);
             //long sizeOfFile = reader.getSize();
             long sizeOfFile = 5000000000L;
             //reader.closeFile();
             long sizeOfStripe = ((sizeOfFile / numOfStripes) + 1);

             compStorageMap.put("0.0.0.0",0L);
             LinkedHashMap<String, Long> sortedCompStorageMap = valueSorter(compStorageMap);
             for (int storage = 0; storage < sortedCompStorageMap.size(); storage++){
                 String ipAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[storage]);
                 sortedCompStorageMap.replace(ipAddress, sortedCompStorageMap.get(String.valueOf(ipAddress)) - minFreeSpace);
             }
             /* //uncomment me for dynamic resigning of numStripes by number of computers that are on
             int numOfComputersUsed = sortedCompStorageMap.size();
             if (numOfComputersUsed < (numOfWholeCopies + (numOfStripedCopies * numOfStripes))) {
                 numOfStripes = ((numOfComputersUsed - numOfWholeCopies) / numOfStripedCopies);
             }
             */
             int stopOfWholes = (-1);

             List<String> computersForWholes = new ArrayList<>();
             JSONObject jsonObj = JSONManipulator.getJSONObject(JSONFilePath);
             String currentName = jsonObj.get("currentName").toString();
             String newName = incrementName(currentName);
             for (int computerNumW = 0; computerNumW < numOfWholeCopies; computerNumW++) {
                 String ipAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumW]);
                 if (sortedCompStorageMap.get(ipAddress) >= sizeOfFile) {
                     computersForWholes.add(ipAddress);
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
                 String ipAddress;
                 try{
                     ipAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumS]);
                 }
                 catch (Exception e){
                     ipAddress = "0.0.0.0";
                 }
                 if ((sortedCompStorageMap.get(ipAddress) - (sizeOfStripe * lapNum)) >= sizeOfStripe) {
                     computersForStripes.add(ipAddress);
                 }
                 else if (computerNumS != 0){
                     ipAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[lastResortComp]);
                     long availableStorage = (sortedCompStorageMap.get(ipAddress) - (sizeOfStripe * lapNum));
                     if (lastResortComp <= stopOfWholes){
                         availableStorage -= sizeOfFile;
                     }

                     lastResortComp++;

                     if (availableStorage >= sizeOfStripe) {
                         computersForStripes.add(ipAddress);
                     }
                     else {
                         lapNum++;
                         lastResortComp = 0;

                         while (lastResortComp < sortedCompStorageMap.size()){
                             ipAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[lastResortComp]);
                             availableStorage = (sortedCompStorageMap.get(ipAddress) - (sizeOfStripe * lapNum));
                             if (lastResortComp <= stopOfWholes){
                                 availableStorage -= sizeOfFile;
                             }

                             if (availableStorage >= sizeOfStripe) {
                                 computersForStripes.add(ipAddress);
                                 break;
                             }
                             lastResortComp++;
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

             for (String item : computersForWholes) {

                 String computerToReceive = item;
                 //FileUtils.writeStripe(computerToReceive, filePath, DestinationRepoLocation, newName + "_w", 0L, sizeOfFile);
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
                                     for (String tempIp : stripes.get(currentStripe + 1)) {
                                         if (tempIp.equals(computerToReceive)) {
                                             computersForStripes.add(tempIp);
                                             computersForStripes.remove((copy * numOfStripes) + currentStripe);
                                             isNotBroken = false;
                                             break;
                                         }
                                     }
                                     if (isNotBroken){
                                         stripes.get(currentStripe + 1).add(computerToReceive);
                                         long startByte = (sizeOfStripe * currentStripe);
                                         //FileReader.writeStripe(computerToReceive, filePath, DestinationRepoLocation, newName + "_s" + currentStripe, startByte, sizeOfStripe);
                                         break;
                                     }
                                 }
                                 else {
                                     stripes.get(currentStripe + 1).add(computerToReceive);
                                     long startByte = (sizeOfStripe * currentStripe);
                                     long stopByte = (startByte + (sizeOfStripe));
                                     //FileReader.writeStripe(computerToReceive, filePath, DestinationRepoLocation, newName + "_s" + currentStripe, startByte, stopByte);
                                     break;
                                 }
                             }
                         }
                         catch (Exception e) {
                             System.out.println("You are out of Storage!");
                         }
                     }
                 }
             }

             JSONManipulator.addToIndex(stripes,filePathInCatalog, fileName, JSONFilePath, newName);
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
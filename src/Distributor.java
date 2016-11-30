/**
 * Created by Aaron Duran on 10/13/16.
 */
import java.io.File;
import java.util.*;

public class Distributor {

    private int numOfStripes;
    private int numOfWholeCopies;
    private int numOfStripedCopies;
    private long minFreeSpace = 0L; // 5GB is 5368709120L

    public Distributor(int numOfStripes, int numOfWholeCopies, int numOfStripedCopies){
        this.numOfStripes = numOfStripes;
        this.numOfWholeCopies = numOfWholeCopies;
        this.numOfStripedCopies = numOfStripedCopies;

    }

    public static Map sortMapByValues(Map unsortedMap){
        Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);

        return sortedMap;
    }

    public void distributor(Map compStorageMap, String filePath, String destinationFilePath, String JSONFilePath){

         try {
             //FileReader reader = new FileReader(filePath);
             //long sizeOfFile = reader.getSize();
             long sizeOfFile = 5000000000L;
             //reader.closeFile();
             long sizeOfStripe = ((sizeOfFile / numOfStripes) + 1);

             compStorageMap.put("0.0.0.0",0L);
             Map<String, Long> sortedCompStorageMap = sortMapByValues(compStorageMap);
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


             int stopOfWholes = (0-1);

             List<String> computersForWholes = new ArrayList<>();
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
                 //FileReader.writeStripe(computerToReceive, filePath, 0, sizeOfFile - 1);
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
                                         //long startByte = (sizeOfStripe * currentStripe);
                                         //long stopByte = (startByte + (sizeOfStripe - 1));
                                         //FileReader.writeStripe(computerToReceive, filePath, startByte, stopByte);
                                         break;
                                     }
                                 }
                                 else {
                                     stripes.get(currentStripe + 1).add(computerToReceive);
                                     //long startByte = (sizeOfStripe * currentStripe);
                                     //long stopByte = (startByte + (sizeOfStripe - 1));
                                     //FileReader.writeStripe(computerToReceive, filePath, startByte, stopByte);
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

             String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
             JSONPreWriter.addToIndex(fileName, stripes, destinationFilePath, JSONFilePath);
         }
         catch (Exception e) {
             e.printStackTrace();
         }

    }
}

class ValueComparator implements Comparator {
    Map map;

    public ValueComparator(Map map) {
        this.map = map;
    }

    public int compare(Object keyA, Object keyB) {
        Comparable valueA = (Comparable) map.get(keyA);
        Comparable valueB = (Comparable) map.get(keyB);
        return valueB.compareTo(valueA);

    }
}

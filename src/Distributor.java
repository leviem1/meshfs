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

    public static int isThereStorage(Map<String, Long> allCompStorage, long neededSize, long usedStorage){
        int compNum = (0-1);
        for (int comp = 0; ((comp < allCompStorage.size()) || (compNum != (0-1))); comp++){
            if (allCompStorage.get(String.valueOf(allCompStorage.keySet().toArray()[comp])) >= (neededSize + usedStorage)){
                compNum = comp;
                break;

            }
        }
        return (compNum);
    }

    public void distributor(Map compStorageMap, String filePath){

         try {
             FileReader reader = new FileReader(filePath);
             long sizeOfFile = reader.getSize();
             //long sizeOfFile = 1000000000L;
             reader.closeFile();
             long sizeOfStripe = ((sizeOfFile / numOfStripes) + 1);

             Map<String, Long> sortedCompStorageMap = sortMapByValues(compStorageMap);
             int numOfComputersUsed = sortedCompStorageMap.size();

             if (numOfComputersUsed < (numOfWholeCopies + (numOfStripedCopies * numOfStripes))) {
                 numOfStripes = ((numOfComputersUsed - numOfWholeCopies) / numOfStripedCopies);
             }


             int stopOfWholes = (0-1);

             List<String> computersForWholes = new ArrayList<>();
             for (int computerNumW = 0; computerNumW < numOfWholeCopies; computerNumW++) {
                 String ipAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumW]);
                 if ((sortedCompStorageMap.get(ipAddress) - minFreeSpace) >= sizeOfFile) {
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
                 String ipAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumS]);
                 if (((sortedCompStorageMap.get(ipAddress) - minFreeSpace) - (sizeOfStripe * lapNum)) >= sizeOfStripe) {
                     computersForStripes.add(ipAddress);
                 }
                 else if (computerNumS != 0){
                     ipAddress = String.valueOf(sortedCompStorageMap.keySet().toArray()[lastResortComp]);
                     long availableStorage = ((sortedCompStorageMap.get(ipAddress) - minFreeSpace) - (sizeOfStripe * lapNum));
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
                             availableStorage = ((sortedCompStorageMap.get(ipAddress) - minFreeSpace) - (sizeOfStripe * lapNum));
                             if (lastResortComp <= stopOfWholes){
                                 availableStorage -= sizeOfFile;
                             }

                             lastResortComp++;
                             System.out.println(lastResortComp);

                             if (availableStorage >= sizeOfStripe) {
                                 computersForStripes.add(ipAddress);
                                 System.out.println(computersForStripes);
                                 break;
                             }

                         }
                     }
                 }
                 else{
                     System.out.println("the is no storage space that is available for this file.");
                     break;
                 }
             }


             List<List> stripes = new ArrayList<>();
             stripes.add(computersForWholes);

             for (int copy = 0; copy < numOfStripes; copy++) {
                 stripes.add(new ArrayList<String>());
             }

             for (int item = 0; item < computersForWholes.size(); item++) {

                 String computerToReceive = computersForWholes.get(item);
                 //FileReader.writeStripe(computerToReceive, filePath, 0, sizeOfFile - 1);
             }

             for (int copy = 0; copy < numOfStripedCopies; copy++) {
                 for (int currentStripe = 0; currentStripe < numOfStripes; currentStripe++){
                     long startByte = (sizeOfStripe * currentStripe);
                     long stopByte = (startByte + (sizeOfStripe - 1));
                     try{
                         String computerToReceive = computersForStripes.get((copy * numOfStripes) + currentStripe);
                         //FileReader.writeStripe(computerToReceive, filePath, startByte, stopByte);
                         stripes.get(currentStripe + 1).add(computerToReceive);
                     }
                     catch (Exception e) {
                         e.printStackTrace();
                     }
                 }

             }
             String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
             JSONPreWriter.addToIndex(fileName, stripes);
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

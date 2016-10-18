/**
 * Created by Aaron Duran on 10/13/16.
 */
import java.io.File;
import java.util.*;

public class Distributor {

    int numOfStripes = 6;
    int numOfWholeCopies = 2;
    int numOfStripedCopies = 2;

    public static Map sortMapByValues(Map unsortedMap){
        Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    public void distributor(Map compStorageMap, File file){

        int numOfComputersUsed = compStorageMap.size();

        if (numOfComputersUsed < (numOfWholeCopies + (numOfStripedCopies * numOfStripes))){
            numOfStripes = ((numOfComputersUsed - numOfWholeCopies) / numOfStripedCopies);
        }

        Map<String, Long> sortedCompStorageMap = sortMapByValues(compStorageMap);

        List<String> computersForWholes = new ArrayList<>();
        for (int computerNumW = 0; computerNumW < numOfWholeCopies; computerNumW++) {
            computersForWholes.add(String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumW]));
        }

        List<String> computersForStripes = new ArrayList<>();
        for (int computerNumS = numOfWholeCopies; computerNumS < ((numOfStripes*numOfStripedCopies)+numOfWholeCopies); computerNumS++) {
            computersForStripes.add(String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumS]));
        }



        long sizeOfFile = fileReader.getSize();
        long sizeOfStripe = ((int) (sizeOfFile / numOfStripes) + 1); // is the int big enough to handle this or does it only perform the operation as int

        for (int item = 0; item < computersForWholes.size(); item++){
            String computerToReceive = computersForWholes.get(item);
            fileReader.writeStripe(computerToReceive, file, 0, sizeOfFile - 1);
        }
        for (int currentStipe = 0; currentStipe < numOfStripes; currentStipe++){
            long startByte = (sizeOfStripe * currentStipe);
            long stopByte = (startByte + (sizeOfStripe - 1));
            for (int item = 0; item < numOfStripedCopies; item++){
                String computerToReceive = computersForStripes.get((currentStipe * numOfStripedCopies) + item);
                fileReader.writeStripe(computerToReceive, file, startByte, stopByte);
            }

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
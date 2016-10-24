/**
 * Created by Aaron Duran on 10/13/16.
 */
import java.io.File;
import java.util.*;

public class Distributor {

    private int numOfStripes;
    private int numOfWholeCopies;
    private int numOfStripedCopies;

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

    public void distributor(Map compStorageMap, String filePath){

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

        List<List> stripes = new ArrayList<>();

        try {
            FileReader reader = new FileReader(filePath);
            //JSONPreWriter write = new JSONPreWriter();
            long sizeOfFile = reader.getSize();
            long sizeOfStripe = ((int) (sizeOfFile / numOfStripes) + 1); // is the int big enough to handle this or does it only perform the operation as int

            for (int item = 0; item < computersForWholes.size(); item++){
                String computerToReceive = computersForWholes.get(item);
                //FileReader.writeStripe(computerToReceive, filePath, 0, sizeOfFile - 1);
            }
            for (int currentStripe = 0; currentStripe < numOfStripes; currentStripe++){
                long startByte = (sizeOfStripe * currentStripe);
                long stopByte = (startByte + (sizeOfStripe - 1));
                List<String> nextStripe = new ArrayList<>();
                for (int item = 0; item < numOfStripedCopies; item++){
                    String computerToReceive = computersForStripes.get((currentStripe * numOfStripedCopies) + item);
                    //FileReader.writeStripe(computerToReceive, filePath, startByte, stopByte);e
                    nextStripe.add(computerToReceive);
                }
                stripes.add(nextStripe);
            }
            //write.addFile(computersForWholes,stripes);

        }
        catch (Exception e){
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

import java.io.File;
//import java.lang.reflect.Array;
import java.util.*;

/**
 * Created by Aaron Duran on 10/13/16.
 */
public class Distributor {

    int numOfStripes;
    int numOfWholeCopies = 2;
    int numOfStripedCopies = 2;

    public static Map sortMapByValues(Map unsortedMap){
        Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    public void distributor(Map compStorageMap, File file){

        int numOfComputersUsed = compStorageMap.size();

        if (numOfComputersUsed < (numOfWholeCopies + (numOfStripedCopies * 6))){
            numOfStripes = ((numOfComputersUsed - numOfWholeCopies) / numOfStripedCopies);
        }
        else {
            numOfStripes = 6;
        }

        Map<String, Long> sortedCompStorageMap = sortMapByValues(compStorageMap);

        String[] computersForWholes = new String[numOfWholeCopies];
        for (int computerNumW = 0; computerNumW < numOfWholeCopies; computerNumW++) {
            computersForWholes[computerNumW] = String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumW]);
        }

        String[] computersForStripes = new String[(numOfStripes*numOfStripedCopies)];
        for (int computerNumS = numOfWholeCopies; computerNumS < ((numOfStripes*numOfStripedCopies)+numOfWholeCopies); computerNumS++) {
            computersForStripes[computerNumS] = String.valueOf(sortedCompStorageMap.keySet().toArray()[computerNumS]);
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
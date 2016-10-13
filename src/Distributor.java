import java.io.FileInputStream;
import java.util.*;

/**
 * Created by Aaron Duran on 10/13/16.
 */
public class Distributor {

    int numOfComputers;
    int numOfStripes;


    public static Map sortMapByValues(Map unsortedMap){
        Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    public void distributor(Map compStorageMap, FileInputStream file){

        numOfComputers = compStorageMap.size();

        if (numOfComputers <= 13){
            numOfStripes = ((numOfComputers - 1) / 2 );
        }
        else {
            numOfStripes = 6;
        }

        Map<String, Integer> sortedCompStorageMap = sortMapByValues(compStorageMap);
        String whole = String.valueOf(sortedCompStorageMap.keySet().toArray()[0]);
        System.out.println(whole);
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
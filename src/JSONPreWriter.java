import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by Aaron Duran on 10/25/16.
 */
public class JSONPreWriter {
    public static void addToIndex(String fileName, List<List<String>> stripes, String destinationFilePath) {
        JSONObject objParent = new JSONObject();
        JSONObject objChild1 = new JSONObject();
        JSONObject objChild2 = new JSONObject();
        JSONObject objChild3 = new JSONObject();
        JSONArray ipArray = new JSONArray();

        for (int stripe = 0; stripe < stripes.size(); stripe++){
            for (int copy = 0; copy < (stripes.get(stripe)).size(); copy++){
                ipArray.add(stripes.get(stripe).get(copy));
            }
            if (stripe == 0){
                objChild3.put("whole", ipArray.clone());
            }
            else{
                objChild3.put("stripe" + String.valueOf(stripe), ipArray.clone());
            }
            ipArray.clear();

        }

        objChild3.put("group", "all");
        objChild3.put("type", "file");
        objChild2.put(fileName, objChild3);




        objChild2.put("type", "directory");
        objChild1.put("videos", objChild2);


        objParent.put("root", objChild1);
        System.out.println(objParent);
        String shortName = fileName.substring(0, fileName.lastIndexOf("."));
        try{
            JSONWriter.writeJSONObject("/Users/aronduran/Desktop/" + shortName + ".json", objParent);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

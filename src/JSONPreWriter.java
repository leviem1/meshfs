import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Created by aronduran on 10/25/16.
 */
public class JSONPreWriter {
    public static void addToIndex(String fileName, List<List> stripes) {
        //JSONObject obj = JSONReader.getJSONObject("/Users/markhedrick/Desktop/test.json");
        JSONObject objParent = new JSONObject();
        JSONObject objChild1 = new JSONObject();
        JSONObject objChild2 = new JSONObject();
        JSONObject objChild3 = new JSONObject();
        JSONArray array1Child3 = new JSONArray();
        JSONArray array2Child3 = new JSONArray();
        JSONArray array3Child3 = new JSONArray();
        JSONArray array4Child3 = new JSONArray();


        JSONArray ipArray = new JSONArray();

        for (int stripe = 0; stripe < stripes.size(); stripe++){
            for (int copy = 0; copy < (stripes.get(stripe)).size(); copy++){
                ipArray.add(stripes.get(stripe).get(copy));
            }
            if (stripe == 0){
                objChild3.put("whole", ipArray);
            }
            else{
                objChild3.put("stripe" + String.valueOf(stripe), ipArray);
            }
            ipArray.clear();
        }

        objChild3.put("group", "all");
        objChild3.put("type", "file");
        objChild2.put(fileName, objChild3);
        objChild2.put("type", "directory");
        objChild1.put("videos", objChild2);
        objParent.put("root", objChild1);

        try{
            JSONWriter.writeJSONObject("/Users/aronduran/Desktop/" + fileName, objParent);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

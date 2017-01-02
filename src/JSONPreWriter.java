import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron Duran on 10/25/16.
 */
public class JSONPreWriter {
    public static void addToIndex(List<List<String>> stripes, String itemFilePath, String fileName, String JSONFilePath, String alphanumericName) {
        addToIndex(stripes,itemFilePath, fileName, JSONFilePath, alphanumericName, "all");
    }

    public static void addToIndex(List<List<String>> stripes, String itemLocation, String fileName, String JSONFilePath, String alphanumericName, String group) {

        JSONObject jsonFile = JSONReader.getJSONObject(JSONFilePath);
        jsonFile.replace("currentName", alphanumericName);


        JSONObject objChild = new JSONObject();
        JSONArray ipArray = new JSONArray();



        for (int stripe = 0; stripe < stripes.size(); stripe++){
            for (int copy = 0; copy < (stripes.get(stripe)).size(); copy++){
                ipArray.add(stripes.get(stripe).get(copy));
            }
            if (stripe == 0){
                objChild.put("whole", ipArray.clone());
            }
            else{
                objChild.put("stripe" + String.valueOf(stripe), ipArray.clone());
            }
            ipArray.clear();

        }

        objChild.put("group", group);
        objChild.put("type", "file");
        objChild.put("fileName", alphanumericName);

        jsonFile = JSONReader.putItemInFolder(jsonFile, itemLocation, fileName,objChild);


        try{
            JSONWriter.writeJSONObject(JSONFilePath, jsonFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

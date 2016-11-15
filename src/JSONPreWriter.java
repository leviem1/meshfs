import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron Duran on 10/25/16.
 */
public class JSONPreWriter {
    public static void addToIndex(String fileName, List<List<String>> stripes, String destinationFilePath, String JSONFilePath) {

        JSONObject jsonFile = JSONReader.getJSONObject(JSONFilePath);



        JSONObject objParent = new JSONObject();
        JSONObject objChild1 = new JSONObject();
        JSONObject objChild2 = new JSONObject();
        JSONObject objChild3 = new JSONObject();
        JSONArray ipArray = new JSONArray();
        /*
        List<String> filePath = new ArrayList<>();
        int startName = 0;
        int stopName;
        while (true){
            try{
                stopName = destinationFilePath.indexOf("/");
                filePath.add(destinationFilePath.substring(startName, stopName));
                startName = stopName+1;
            }
            catch(Exception e){
                filePath.add(destinationFilePath.substring(startName));
                break;
            }
        }
        int startSearch = 0;
        int JSONIndex;
        */
        /*
        for (String part:filePath) {
            try{

            }
            catch(Exception e){

            }


        }
        */


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

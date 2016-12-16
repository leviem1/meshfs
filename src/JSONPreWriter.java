import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron Duran on 10/25/16.
 */
public class JSONPreWriter {
    public static void addToIndex(List<List<String>> stripes, String itemFilePath, String JSONFilePath, String alphanumericName) {
        addToIndex(stripes,itemFilePath, JSONFilePath, alphanumericName, "all");
    }

    public static void addToIndex(List<List<String>> stripes, String itemLocation, String JSONFilePath, String alphanumericName,String group) {

        JSONObject jsonFile = JSONReader.getJSONObject(JSONFilePath);
        jsonFile.replace("currentName", alphanumericName);


        //JSONObject objParent = new JSONObject();
        //JSONObject objChild1 = new JSONObject();
        //JSONObject objChild2 = new JSONObject();
        JSONObject objChild3 = new JSONObject();
        JSONArray ipArray = new JSONArray();

        String fileName = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        String[] folders = itemLocation.substring(0,itemLocation.lastIndexOf("/")).split("/");
        JSONObject folderToRead = jsonFile;
        JSONObject folderToReadNew;
        for (String folder : folders) {
            folderToReadNew = (JSONObject) folderToRead.get(folder);
            if (folderToReadNew == null){
                JSONObject folderCreator = new JSONObject();
                folderCreator.put("type", "directory");
                folderToRead.put(folder, folderCreator);
                folderToRead = (JSONObject) folderToRead.get(folder);
            }
            else{
                folderToRead = folderToReadNew;
            }


        }


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

        objChild3.put("group", group);
        objChild3.put("type", "file");
        objChild3.put("fileName", alphanumericName);
        folderToRead.put(fileName, objChild3);



/*
        objChild2.put("type", "directory");
        objChild1.put("videos", objChild2);


        objParent.put("root", objChild1);
        */
        String shortName = fileName.substring(0, fileName.lastIndexOf("."));
        try{
            JSONWriter.writeJSONObject(JSONFilePath, jsonFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

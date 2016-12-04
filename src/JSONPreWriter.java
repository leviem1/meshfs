import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aaron Duran on 10/25/16.
 */
public class JSONPreWriter {
    public static void addToIndex(List<List<String>> stripes, String itemFilePath, String JSONFilePath) {
        addToIndex(stripes,itemFilePath, JSONFilePath, "all");
    }

    public static void addToIndex(List<List<String>> stripes, String itemLocation, String JSONFilePath, String group) {

        JSONObject jsonFile = JSONReader.getJSONObject(JSONFilePath);



        JSONObject objParent = new JSONObject();
        JSONObject objChild1 = new JSONObject();
        JSONObject objChild2 = new JSONObject();
        JSONObject objChild3 = new JSONObject();
        JSONArray ipArray = new JSONArray();

        /*List<String> filePath = new ArrayList<>();
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
        }*/
        String fileName = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        String[] folders = itemLocation.substring(0,itemLocation.lastIndexOf("/")).split("/");
        JSONObject folderToRead = jsonFile;
        JSONObject folderToReadNew;
        for (String folder : folders) {
            System.out.println(folderToRead);
            System.out.println(folder);
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

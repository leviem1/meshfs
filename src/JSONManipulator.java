import org.json.simple.parser.JSONParser;
import org.json.simple.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Levi Muniz on 10/19/16.
 */
public class JSONManipulator {

    public static JSONObject getJSONObject(String filePath) {
        JSONParser reader = new JSONParser();
        JSONObject jsonObject = null;
        try {
            Object obj = reader.parse(new FileReader(filePath));
            jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONArray getJSONArray(String filePath) {
        JSONParser reader = new JSONParser();
        JSONArray jsonArray = null;
        try {
            Object obj = reader.parse(new FileReader(filePath));

            jsonArray = (JSONArray) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public static Map<String,String> getMapOfFolderContents(JSONObject jsonObject, String folderLocation){
        String[] Tree = folderLocation.split("/");
        JSONObject folderToRead = jsonObject;
        for (String folder : Tree) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        Map<String,String> contents = new HashMap<>();
        for (Object key : folderToRead.keySet()) {
            String keyStr = key.toString();
            try{
                String type = (((JSONObject) folderToRead.get(keyStr)).get("type")).toString();
                contents.put(keyStr,type);
            }
            catch (Exception e){}
        }
        return contents;
    }

    public static JSONObject removeItem(JSONObject jsonObject, String itemLocation){
        String item = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        String[] folders = itemLocation.substring(0,itemLocation.lastIndexOf("/")).split("/");
        JSONObject folderToRead = jsonObject;

        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        try {
            folderToRead.remove(item);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject putItemInFolder(JSONObject jsonObject, String itemDestinationLocation, String fileName, JSONObject itemContents){
        String[] folders = itemDestinationLocation.split("/");
        JSONObject folderToRead = jsonObject;
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
        folderToRead.put(fileName, itemContents);
        return jsonObject;
    }

    public static JSONObject getItemContents(JSONObject jsonObject, String itemLocation){
        String[] folders = itemLocation.split("/");
        JSONObject folderToRead = jsonObject;
        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        return folderToRead;
    }

    public static JSONObject copyFile(JSONObject jsonObject, String itemLocation, String destinationLocation, boolean showDate){
        JSONObject itemContents = getItemContents(jsonObject,itemLocation);
        String fileName = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        DateFormat df = new SimpleDateFormat("h:mm a");
        Date dateObj = new Date();
        if(showDate){
            jsonObject = putItemInFolder(jsonObject, destinationLocation, fileName+" ("+ df.format(dateObj)+")", itemContents);
        }else{
            jsonObject = putItemInFolder(jsonObject, destinationLocation, fileName, itemContents);
        }

        return jsonObject;
    }

    public static JSONObject addFolder(JSONObject jsonObject, String jsonPath, String directoryName){
        JSONObject type = new JSONObject();
        type.put("type", "directory");
        jsonObject = putItemInFolder(jsonObject, jsonPath, directoryName, type);
        return jsonObject;
    }

    public static JSONObject moveFile(JSONObject jsonObject, String itemLocation, String destinationLocation){
        jsonObject = copyFile(jsonObject, itemLocation, destinationLocation, false);
        jsonObject = removeItem(jsonObject, itemLocation);
        return jsonObject;
    }

    public static void addToIndex(List<List<String>> stripes, String itemFilePath, String fileName, String JSONFilePath, String alphanumericName) {
        addToIndex(stripes,itemFilePath, fileName, JSONFilePath, alphanumericName, "all");
    }

    public static void addToIndex(List<List<String>> stripes, String itemLocation, String fileName, String JSONFilePath, String alphanumericName, String group) {

        JSONObject jsonFile = JSONManipulator.getJSONObject(JSONFilePath);
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

        jsonFile = JSONManipulator.putItemInFolder(jsonFile, itemLocation, fileName,objChild);


        try{
            writeJSONObject(JSONFilePath, jsonFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void addToIndex(String itemLocation, String fileName, String JSONFilePath) {

        JSONObject jsonFile = JSONManipulator.getJSONObject(JSONFilePath);

        JSONObject objChild = new JSONObject();


        objChild.put("type", "file");
        objChild.put("fileName", fileName);

        jsonFile = JSONManipulator.putItemInFolder(jsonFile, itemLocation, fileName,objChild);


        try{
            writeJSONObject(JSONFilePath, jsonFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void writeJSONObject(String filePath, JSONObject obj) throws IOException {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(obj.toJSONString());
        }
    }

    public static void pullFile(JSONObject jsonObject, String itemLocation, String compInfoJSONFilelocation){
        String itemName = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        String[] folders = itemLocation.split("/");
        JSONObject itemToRead = jsonObject;
        for (String folder : folders) {
            itemToRead = (JSONObject) itemToRead.get(folder);
        }
        JSONObject fileInfo = (JSONObject) itemToRead.clone();
        fileInfo.remove("type");
        String fileName = fileInfo.get("fileName").toString();
        fileInfo.remove("fileName");
        String group = fileInfo.get("group").toString();
        fileInfo.remove("group");

        for (Object stripe: fileInfo.keySet() ){
        }


        JSONObject compInfoFile = getJSONObject(compInfoJSONFilelocation);



        itemToRead.keySet();

    }

}

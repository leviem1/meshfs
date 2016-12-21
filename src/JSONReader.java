import org.json.simple.parser.JSONParser;
import org.json.simple.*;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Levi Muniz on 10/19/16.
 */
public class JSONReader {

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
            System.out.println("fail");
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject putItemInFolder(JSONObject jsonObject, String itemDestinationLocation, JSONObject itemContents){
        String fileName = itemDestinationLocation.substring(itemDestinationLocation.lastIndexOf("/")+1);
        String[] folders = itemDestinationLocation.substring(0,itemDestinationLocation.lastIndexOf("/")).split("/");
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

    public static JSONObject copyFile(JSONObject jsonObject, String itemLocation, String destinationLocation){
        JSONObject itemContents = getItemContents(jsonObject,itemLocation);
        jsonObject = putItemInFolder(jsonObject, destinationLocation,itemContents);
        return jsonObject;
    }

    public static JSONObject moveFile(JSONObject jsonObject, String itemLocation, String destinationLocation){
        jsonObject = copyFile(jsonObject, itemLocation, destinationLocation);
        jsonObject = removeItem(jsonObject, itemLocation);
        return jsonObject;
    }




    /*
    public static void pullFile(JSONObject jsonObject, String itemLocation, String compInfoJSONFilelocation){
        String itemName = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        String[] folders = itemLocation.split("/");
        JSONObject itemToRead = jsonObject;
        for (String folder : folders) {
            itemToRead = (JSONObject) itemToRead.get(folder);
        }

        String fileName = itemToRead.get("fileName").toString();
        JSONObject compInfoFile = getJSONObject(compInfoJSONFilelocation);


        itemToRead.keySet();

    }
    */
}

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
        String[] test = itemLocation.substring(0,itemLocation.lastIndexOf("/")).split("/");
        JSONObject folderToRead = jsonObject;
        for (String folder : test) {
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


}

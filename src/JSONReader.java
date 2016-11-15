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


    public static HashMap<String,String> getMapOfFolderContents(JSONObject jsonObject, String folderLocation){
        String[] Tree = folderLocation.split("/");
        JSONObject folderToRead = jsonObject;
        HashMap<String,String> contents = null;
        for (String folder : Tree) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        for (Object key : folderToRead.keySet()) {
            String keyStr = (String) key;
            String type = (String) ((JSONObject) folderToRead.get(keyStr)).get("type");
            contents.put(keyStr,type);
        }
        return contents;
    }



}

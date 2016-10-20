import org.json.simple.parser.JSONParser;
import org.json.simple.*;
import java.io.FileReader;

/**
 * Created by Levi Muniz on 10/19/16.
 */
public class JSONReader {

    public static JSONObject getJSONArray(String filePath) {
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

}

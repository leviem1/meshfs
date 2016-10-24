import org.json.simple.JSONObject;
import java.io.*;

/**
 * Created by Mark Hedrick on 10/23/16.
 */
public class JSONWriter {
    public static void writeJSONObject(String filePath, JSONObject obj) throws IOException {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(obj.toJSONString());
        }
    }
}

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;

/**
 * Created by markhedrick on 11/7/16.
 */
public class JSONManipulator {

    public JSONManipulator(String filepath) throws Exception{
        Gson gson = new Gson();

// 1. JSON to Java object, read it from a file.
        Staff staff = gson.fromJson(new FileReader(filepath), Staff.class);

// 2. JSON to Java object, read it from a Json String.
        String jsonInString = "{'name' : 'mkyong'}";
        Staff staff = gson.fromJson(jsonInString, Staff.class);

// JSON to JsonElement, convert to String later.
        JsonElement json = gson.fromJson(new FileReader("D:\\file.json"), JsonElement.class);
        String result = gson.toJson(json);
    }
}

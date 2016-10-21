/**
 * Created by Levi Muniz on 10/3/16.
 */
import org.json.simple.*;
import java.util.*;


public class MeshFS {

    public static void main(String[] args) {
        //Welcome.run();
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MeshFS");

        Reporting reporter = new Reporting();
        Map<String, Long> hostStorage = new HashMap<>();

        hostStorage.put(reporter.getIpAddress(), reporter.getSystemStorage());
        WelcomeWindow.run();

        JSONObject obj = JSONReader.getJSONObject("/Users/muniz/Desktop/test.json");

        System.out.println(obj.get("root"));
        //System.out.println(array.size());
    }
}

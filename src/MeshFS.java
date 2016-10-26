/**
 * Created by Levi Muniz on 10/3/16.
 */
import org.json.simple.*;

import java.io.IOException;
import java.util.*;


public class MeshFS {

    public static void main(String[] args) {

        boolean isMaster = false;

        if (args.length > 0) {
            if (args[0].equals("--master")) {
                isMaster = true;
            }
        }

        System.out.println(isMaster);

        //Welcome.run();
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MeshFS");

        Reporting reporter = new Reporting();
        Map<String, Long> hostStorage = new HashMap<>();

        /*
        hostStorage.put("10.15.20.1", 800000000000L);
        hostStorage.put("10.15.20.2", 700000000000L);
        hostStorage.put("10.15.20.3", 600000000000L);
        hostStorage.put("10.15.20.4", 900000000000L);
        hostStorage.put("10.15.20.5", 500000000000L);
        hostStorage.put("10.15.20.6", 10000000000L);
        hostStorage.put("10.15.20.7", 100000000000L);
        hostStorage.put("10.15.20.8", 1000000000000L);
        hostStorage.put("10.15.20.9", 750000000000L);
        hostStorage.put("10.15.20.10", 650000000000L);
        hostStorage.put("10.15.20.11", 980000000000L);
        hostStorage.put("10.15.20.12", 540000000000L);
        hostStorage.put("10.15.20.13", 430000000000L);
        hostStorage.put("10.15.20.14", 880000000000L);
        hostStorage.put("10.15.20.15", 110000000000L);
        hostStorage.put("10.15.20.16", 120000000000L);
        */


        hostStorage.put(reporter.getIpAddress(), reporter.getSystemStorage());
        Distributor test = new Distributor(6,2,2);
        test.distributor(hostStorage, "/Users/aronduran/Desktop/pigskin.mp4");
        WelcomeWindow.run();

        /*
        //JSONObject obj = JSONReader.getJSONObject("/Users/markhedrick/Desktop/test.json");
        JSONObject objParent = new JSONObject();
        JSONObject objChild1 = new JSONObject();
        JSONObject objChild2 = new JSONObject();
        JSONObject objChild3 = new JSONObject();
        JSONArray array1Child3 = new JSONArray();
        JSONArray array2Child3 = new JSONArray();
        JSONArray array3Child3 = new JSONArray();
        JSONArray array4Child3 = new JSONArray();

        array1Child3.add("10.15.20.1");
        array1Child3.add("10.15.20.7");
        array2Child3.add("10.15.20.4");
        array2Child3.add("10.15.20.10");
        array3Child3.add("10.15.20.2");
        array3Child3.add("10.15.20.8");
        array4Child3.add("10.15.20.11");
        array4Child3.add("10.15.20.15");

        objChild3.put("stripe1", array1Child3);
        objChild3.put("stripe2", array2Child3);
        objChild3.put("stripe3", array3Child3);
        objChild3.put("whole", array4Child3);

        objChild3.put("group", "all");
        objChild3.put("type", "file");
        objChild2.put("pigskin.mp4", objChild3);
        objChild2.put("type", "directory");
        objChild1.put("videos", objChild2);
        objParent.put("root", objChild1);
        /*
        try{
            JSONWriter.writeJSONObject("/Users/markhedrick/Desktop/test12345.json", objParent);
        }catch(IOException e){
            e.printStackTrace();
        }
        */


        //System.out.println(obj.get("root"));
        //System.out.println(array.size());
    }
}

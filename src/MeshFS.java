/**
 * Created by Levi Muniz on 10/3/16.
 */
import java.util.HashMap;
import java.util.Map;


public class MeshFS {

    public static void main(String[] args) {
        //Welcome.run();
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MeshFS");

        Reporting reporter = new Reporting();
        Map<String,Long> hostStorage = new HashMap<>();

        hostStorage.put(reporter.getIpAddress(),reporter.getSystemStorage());
        WelcomeWindow.run();

        System.out.println(reporter.getUptime());
        System.out.println(reporter.getSystemOS());

        //ClientDownloader demo = new ClientDownloader();
        //demo.downloadData("Marks-Mac-mini.local", 13267, "/Users/markhedrick/Desktop/test.rdp");
        System.out.println(reporter.getMacAddress());
    }
}

/**
 * Created by Levi Muniz on 10/3/16.
 */

//import com.apple.eawt.Application;
import org.json.simple.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class MeshFS {

    public static Properties properties;
    public static FileServer fileServer;
    public static boolean nogui = false;
    public static boolean isMaster;

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack" , "true");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MeshFS");
        Runtime.getRuntime().addShutdownHook(new Thread(new onQuit()));
        properties = ConfigParser.loadProperties();
        new CliParser(args, properties);
        List<List> possibleIP = Reporting.getIpAddress();
        if (properties.getProperty("masterIP").equals("127.0.0.1")) {
            isMaster = true;
        } else {
            for (List iFace : possibleIP) {
                if (iFace.get(1).equals(properties.getProperty("masterIP"))) {
                    isMaster = true;
                    break;
                } else {
                    isMaster = false;
                }
            }
        }
        if (nogui) {
            File repo = new File(properties.getProperty("repository"));
            File catalog = new File(properties.getProperty("repository")+".catalog.json");
            if (!repo.exists()) {
                repo.mkdirs();
            }
            if(!catalog.exists()){
                JSONObject newCatalog = new JSONObject();
                JSONObject root = new JSONObject();
                root.put("type", "directory");
                newCatalog.put("currentName", "0000000000000000");
                newCatalog.put("root", root);
                try {
                    JSONManipulator.writeJSONObject(properties.getProperty("repository")+".catalog.json", newCatalog);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                fileServer = new FileServer();
                fileServer.startServer(Integer.valueOf(properties.getProperty("portNumber")), Integer.valueOf(properties.getProperty("serverThreads")), Integer.valueOf(properties.getProperty("timeout")) * 1000);
            } catch (IOException e) {
                boolean serverStarted = false;
                for (List iFace : possibleIP) {
                    if (FileClient.ping(iFace.get(1).toString(), Integer.parseInt(properties.getProperty("portNumber")))) {
                        serverStarted = true;
                        break;
                    }
                }
                if (!serverStarted) {
                    e.printStackTrace();
                    System.out.println("Error: Server start failure");
                    System.exit(1);
                } else {
                    System.out.println("Server already started!");
                    System.exit(2);
                }
            }
            System.setProperty("java.awt.headless", "true");
        } else {
            if(Reporting.getSystemOS().toLowerCase().contains("mac")){
                //Application.getApplication().setDockIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
            }
            GreetingsWindow.run("client");
        }
    }
}

class onQuit implements Runnable {
    public void run() {
        if (MeshFS.nogui) {
            MeshFS.fileServer.stopServer();
        }
    }
}
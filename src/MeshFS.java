/**
 * Created by Levi Muniz on 10/3/16.
 */

import org.json.simple.JSONObject;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class MeshFS {

    public static Properties properties;
    public static FileServer fileServer;
    public static boolean nogui = false;
    public static boolean isMaster = false;
    public static  boolean configure = false;


    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
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
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(!(isMaster)){
                        if(FileClient.ping(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("port")))) {
                            try {
                                FileClient.sendReport(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("port")));
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }

                    }
                }
            };
            java.util.Timer timer = new java.util.Timer();
            timer.scheduleAtFixedRate(timerTask, 0, 60000);

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
                com.apple.eawt.Application.getApplication().setDockIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
            }
            File properties = new File(".config.properties");
            if (!properties.exists() || configure) {
                GreetingsWindow.run(true);
            } else {
                GreetingsWindow.run(false);
            }

        }
    }
    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
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
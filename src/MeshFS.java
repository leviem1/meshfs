/**
 * Created by Levi Muniz on 10/3/16.
 */

import org.json.simple.JSONObject;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MeshFS {
    static Properties properties;
    static FileServer fileServer;
    static boolean nogui = false;
    static boolean configure = false;
    static boolean isMaster = false;
    static List<String> possibleIP;



    public static void main(String[] args) {
        new CliParser(args);

        properties = ConfigParser.loadProperties();

        Runtime.getRuntime().addShutdownHook(new Thread(new onQuit()));

        if (nogui){
            serverPreStartup();
            fileServerStartup();
            if (isMaster){
                masterSetup();
            }else{
                slaveSetup();

            }

        }else{
            guiStartup();
        }
    }

    static void serverPreStartup(){
        System.setProperty("java.awt.headless", "true");

        possibleIP = Reporting.getIpAddresses();
        if (properties.getProperty("masterIP").equals("127.0.0.1")) {
            isMaster = true;
        } else {
            for (String iFace : possibleIP) {
                if (iFace.equals(properties.getProperty("masterIP"))) {
                    isMaster = true;
                    break;
                } else {
                    isMaster = false;
                }
            }
        }
    }

    static void fileServerStartup(){
        File repo = new File(properties.getProperty("repository"));

        if (!repo.exists()) {
            if (!repo.mkdirs()) System.exit(1);
        }

        try {
            fileServer = new FileServer();
            fileServer.startServer(Integer.valueOf(properties.getProperty("portNumber")), Integer.valueOf(properties.getProperty("serverThreads")), Integer.valueOf(properties.getProperty("timeout")) * 1000);
        } catch (IOException e) {
            boolean serverStarted = false;

            for (String iFace : possibleIP) {
                if (FileClient.ping(iFace, Integer.parseInt(properties.getProperty("portNumber")))) {
                    serverStarted = true;
                    break;
                }
            }

            if (!serverStarted) {
                e.printStackTrace();
                System.out.println("Error: Server start failure");
                System.exit(2);
            } else {
                System.out.println("Server already started!");
                System.exit(3);
            }
        }
    }

    static void masterSetup(){
        File catalog = new File(properties.getProperty("repository")+".catalog.json");

        if(!catalog.exists()){
            JSONObject newCatalog = new JSONObject();
            newCatalog.put("currentName", "0000000000000000");
            try {
                JSONManipulator.writeJSONObject(properties.getProperty("repository")+".catalog.json", newCatalog);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void slaveSetup(){
        TimerTask scheduledReporting = new TimerTask() {
            @Override
            public void run() {
                if (FileClient.ping(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("portNumber")))) {
                    try {
                        FileClient.sendReport(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("portNumber")));
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        };
        java.util.Timer scheduledReportingTimer = new java.util.Timer();
        scheduledReportingTimer.scheduleAtFixedRate(scheduledReporting, 0, 30000);
    }

    static void guiStartup(){
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MeshFS");

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | UnsupportedLookAndFeelException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        if(Reporting.getSystemOS().toLowerCase().contains("mac")){
            com.apple.eawt.Application.getApplication().setDockIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
        }

        if(new File(".config.properties").exists()){
            GreetingsWindow.run(true, null);
        }else{
            GreetingsWindow.run(false, null);
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
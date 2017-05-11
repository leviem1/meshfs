import org.json.simple.JSONObject;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

class MeshFS {
    static Properties properties;
    static FileServer fileServer;
    static boolean nogui = false;
    static boolean configure = false;
    static boolean isMaster = false;
    static MulticastServer multicastServer;

    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack" , "true");
        //check if we already have a config file
        configure = !new File(".config.properties").exists();
        //load and check for properties
        properties = ConfigParser.loadProperties();
        CliParser cliParser = new CliParser(args);
        Runtime.getRuntime().addShutdownHook(new Thread(new onQuit()));
        multicastServer = new MulticastServer();
        try {
            multicastServer.startServer(properties.getProperty("multicastGroup"), Integer.parseInt(properties.getProperty("multicastPort")));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        if (nogui) {
            System.setProperty("java.awt.headless", "true");

            List<String> possibleIP = Reporting.getIpAddresses();
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

            if (isMaster) {
                if (configure && !cliParser.cmd.hasOption("adduser")) {
                    System.out.println("Starting Interactive Authentication Configurator");
                    cliParser.addUser();
                }
                File manifestFile = new File(MeshFS.properties.getProperty("repository") + ".manifest.json");
                manifestFile.delete();
                File catalog = new File(properties.getProperty("repository") + ".catalog.json");

                TimerTask discoveryBroadcast = new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            MulticastClient.notifyClients(properties.getProperty("multicastGroup"), Integer.parseInt(properties.getProperty("multicastPort")));
                        } catch (IOException ignored) {
                        }
                    }
                };

                Timer discoveryBroadcastTimer = new Timer();
                discoveryBroadcastTimer.scheduleAtFixedRate(discoveryBroadcast, 0, 3000);

                if (!catalog.exists()) {
                    JSONObject newCatalog = new JSONObject();
                    JSONObject folder = new JSONObject();
                    JSONObject newRoot = new JSONObject();

                    newCatalog.put("currentName", "0000000000000000");
                    folder.put("type", "directory");
                    newRoot.put("Users", folder);
                    newRoot.put("Shared", folder);
                    newCatalog.put("root", newRoot);

                    JSONObject fileInfo = new JSONObject();
                    newCatalog.put("fileInfo", fileInfo);
                    try {
                        JSONManipulator.writeJSONObject(
                                properties.getProperty("repository") + ".catalog.json", newCatalog);
                    } catch (IOException ignored) {
                    }
                }

                TimerTask manifestCheck =
                        new TimerTask() {
                            @Override
                            public void run() {
                                Long currentTimeStamp = new Date().getTime();
                                if (!(new File(MeshFS.properties.getProperty("repository") + ".manifest.json")
                                        .exists())) {
                                    return;
                                }
                                JSONObject manifest =
                                        JSONManipulator.getJSONObject(
                                                MeshFS.properties.getProperty("repository") + ".manifest.json");
                                JSONObject newManifest = manifest;
                                for (Object computer : manifest.keySet()) {
                                    Long nodeTimeStamp =
                                            (Long) ((JSONObject) manifest.get(computer)).get("checkInTimestamp");
                                    if (currentTimeStamp > nodeTimeStamp + 32000) {
                                        newManifest = JSONManipulator.removeItem(newManifest, computer.toString());
                                        System.out.println(computer.toString() + " was removed from the manifest");
                                        break;
                                    }
                                }
                                try {
                                    JSONManipulator.writeJSONObject(
                                            MeshFS.properties.getProperty("repository") + ".manifest.json", newManifest);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                Timer manifestTimer = new Timer();
                manifestTimer.scheduleAtFixedRate(manifestCheck, 0, 1000);
            } else {
                TimerTask scheduledReporting =
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (FileClient.ping(
                                        properties.getProperty("masterIP"),
                                        Integer.parseInt(properties.getProperty("portNumber"))) > -1) {
                                    try {
                                        FileClient.sendReport(
                                                properties.getProperty("masterIP"),
                                                Integer.parseInt(properties.getProperty("portNumber")));
                                        FileClient.receiveFile(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("portNumber")), ".manifest");
                                        FileClient.receiveFile(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("portNumber")), ".catalog");
                                    } catch (IOException | MalformedRequestException ioe) {
                                        ioe.printStackTrace();
                                    }
                                }
                            }
                        };
                java.util.Timer scheduledReportingTimer = new java.util.Timer();
                scheduledReportingTimer.scheduleAtFixedRate(scheduledReporting, 0, 30000);
            }

            File repo = new File(properties.getProperty("repository"));

            if (!repo.exists()) {
                if (!repo.mkdirs()) System.exit(1);
            }

            try {
                fileServer = new FileServer();
                fileServer.startServer(
                        Integer.parseInt(properties.getProperty("portNumber")),
                        Integer.parseInt(properties.getProperty("serverThreads")),
                        Integer.parseInt(properties.getProperty("timeout")) * 1000);
            } catch (IOException e) {
                boolean serverStarted = false;

                for (String iFace : possibleIP) {
                    if (FileClient.ping(iFace, Integer.parseInt(properties.getProperty("portNumber"))) > -1) {
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

        } else {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("com.apple.mrj.application.apple.menu.about.name", "MeshFS");

            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException
                    | UnsupportedLookAndFeelException
                    | IllegalAccessException
                    | InstantiationException e) {
                e.printStackTrace();
            }

            if (Reporting.getSystemOS().toLowerCase().contains("mac")) {
                com.apple.eawt.Application.getApplication()
                        .setDockIconImage(new ImageIcon(MeshFS.class.getResource("app_icon.png")).getImage());
            }

            if (configure) {
                GreetingsWindow.run(true, null);
            } else {
                GreetingsWindow.run(false, null);
            }
        }
    }
}

class onQuit implements Runnable {
    public void run() {
        try {
            if (MeshFS.nogui) {
                MeshFS.fileServer.stopServer();
            }
        } catch (NullPointerException ignored) {
        }

        try {
            MeshFS.multicastServer.stopServer();
        } catch (NullPointerException ignored) {
        }

        DriveAPI.unauthorize();

    }
}

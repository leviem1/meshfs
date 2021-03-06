import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * This is the main class of MeshFS and starts all services
 * for each run mode of MeshFS
 *
 * @author Aaron Duran, Mark Hedrick, and Levi Muniz
 * @version 1.0.0
 */

class MeshFS {
    final static int[] numFailedConn = {-1};
    static Properties properties;
    static FileServer fileServer;
    static String masterMAC;
    static boolean nogui = false;
    static boolean configure = false;
    static MulticastServer multicastServer;
    static final Timer nodePanicTimer = new Timer();
    static final Timer scheduledReportingTimer = new Timer();
    static boolean isMaster = false;
    static String userUUID = null;
    private static int activeWindows = 0;
    private static final Timer manifestTimer = new Timer();
    private static final Timer discoveryBroadcastTimer = new Timer();

    /**
     * This method is responsible for determining the run mode
     * of the session and starting the respective services.
     *
     * @param args Command line arguments
     */
    @SuppressWarnings("unchecked")

    public static void main(String[] args) {
        System.setProperty("java.net.preferIPv4Stack", "true");
        configure = !new File(".config.properties").exists();
        properties = ConfigParser.loadProperties();
        CliParser cliParser = new CliParser(args);
        Runtime.getRuntime().addShutdownHook(new Thread(new onQuit()));
        multicastServer = new MulticastServer();
        try {
            multicastServer.startServer(properties.getProperty("multicastGroup"), Integer.parseInt(properties.getProperty("multicastPort")), Integer.parseInt(properties.getProperty("serverThreads")));
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

            File repo = new File(properties.getProperty("repository"));

            if (!repo.exists() && !(repo.mkdirs())) System.exit(1);

            if (isMaster) {
                if (((configure && !cliParser.cmd.hasOption("adduser"))) || !new File(MeshFS.properties.getProperty("repository") + ".auth").exists()) {
                    System.out.println("Starting Initial Authentication Generator");
                    cliParser.addUser("admin");
                }

                startAsMaster();
            } else {

                if (FileClient.ping(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("portNumber"))) > -1) {
                    try {
                        masterMAC = FileClient.receiveReportAsJSON(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("portNumber"))).get(0).toString();
                    } catch (IOException | MalformedRequestException e) {
                        e.printStackTrace();
                    }
                }

                TimerTask scheduledReporting =
                        new TimerTask() {
                            @Override
                            public void run() {

                                if (FileClient.ping(
                                        properties.getProperty("masterIP"),
                                        Integer.parseInt(properties.getProperty("portNumber"))) > -1) {
                                    try {
                                        FileClient.receiveFile(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("portNumber")), ".auth");
                                        FileClient.receiveFile(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("portNumber")), ".manifest.json");
                                        FileClient.receiveFile(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("portNumber")), ".catalog.json");
                                    } catch (IOException | MalformedRequestException | FileTransferException ioe) {
                                        ioe.printStackTrace();
                                    }
                                    if (numFailedConn[0] < 0 || numFailedConn[0] > 1) {
                                        List<String> filesToRestore = new ArrayList<>();
                                        numFailedConn[0] = 0;


                                        try {
                                            filesToRestore = FileClient.getNodeIntendedFiles(properties.getProperty("masterIP"), Integer.parseInt(properties.getProperty("portNumber")), Reporting.getMacAddress());
                                        } catch (MalformedRequestException | IOException e) {
                                            e.printStackTrace();
                                        }
                                        String currentFiles = Reporting.getRepositoryContents();
                                        List<String> filesToRemove = Arrays.asList(currentFiles.substring(1, currentFiles.length() - 1).split(", "));

                                        for (String fileName : filesToRestore) {
                                            if (filesToRemove.contains(fileName)) {
                                                filesToRemove.remove(fileName);
                                            }
                                        }

                                        for (String file : filesToRemove) {
                                            if (!file.equals(".catalog.json") && !file.equals(".auth") && !file.equals(".manifest.json")) {
                                                FileUtils.removeFile(MeshFS.properties.getProperty("repository") + file);
                                            }
                                        }
                                        if (!filesToRestore.get(0).equals("")) {
                                            for (String fileName : filesToRestore) {
                                                if (new File(MeshFS.properties.getProperty("repository") + fileName).exists()) {
                                                    try {
                                                        List<String> catalogReferences = FileRestore.findFileReferencesInCatalog(JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json"), fileName.substring(0, fileName.indexOf("_")));
                                                        JSONUtils.pullFile(JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json"), catalogReferences.get(0), "test", "test.txt", false);
                                                        FileRestore.unCorruptFilesInCatalog(catalogReferences);
                                                    } catch (PullRequestException | IOException | FileTransferException | MalformedRequestException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        }

                                    }
                                    try {
                                        FileClient.sendReport(
                                                properties.getProperty("masterIP"),
                                                Integer.parseInt(properties.getProperty("portNumber")));
                                    } catch (IOException | MalformedRequestException e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    numFailedConn[0]++;

                                }
                            }
                        };

                TimerTask nodePanic =
                        new TimerTask() {
                            @Override
                            public void run() {
                                if (numFailedConn[0] >= 3) {
                                    try {
                                        MulticastClient.masterDownInform(properties.getProperty("multicastGroup"), Integer.parseInt(properties.getProperty("multicastPort")));
                                    } catch (IOException ignored) {
                                    }
                                }
                            }
                        };

                nodePanicTimer.scheduleAtFixedRate(nodePanic, 0, 3000);
                scheduledReportingTimer.scheduleAtFixedRate(scheduledReporting, 0, 30000);
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


            TimerTask windowCheck =
                    new TimerTask() {
                        @Override
                        public void run() {
                            activeWindows = Window.getWindows().length;
                            for (Window w : Window.getWindows()) {
                                if (!w.isShowing()) {
                                    activeWindows -= 1;
                                }

                            }
                            if (activeWindows < 1) {
                                System.exit(0);
                            }
                        }
                    };
            Timer frameTimer = new Timer();
            frameTimer.scheduleAtFixedRate(windowCheck, 0, 2000);
        }
    }

    /**
     * This method starts the required services for the master
     * run mode
     */
    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})

    static void startAsMaster() {
        MeshFS.isMaster = true;
        new File(MeshFS.properties.getProperty("repository") + ".manifest.json").delete();
        File catalog = new File(MeshFS.properties.getProperty("repository") + ".catalog.json");

        TimerTask discoveryBroadcast = new TimerTask() {
            @Override
            public void run() {
                try {
                    MulticastClient.notifyClients(MeshFS.properties.getProperty("multicastGroup"), Integer.parseInt(MeshFS.properties.getProperty("multicastPort")));
                } catch (IOException ignored) {
                }
            }
        };

        MeshFS.discoveryBroadcastTimer.scheduleAtFixedRate(discoveryBroadcast, 0, 1000);

        if (!catalog.exists()) {
            JSONObject newCatalog = new JSONObject();
            JSONObject folder = new JSONObject();
            JSONObject newRoot = new JSONObject();
            JSONArray groups = new JSONArray();
            groups.add("all");

            newCatalog.put("currentName", "0000000000000000");
            folder.put("type", "directory");
            folder.put("groups", groups);
            folder.put("blacklist", new JSONArray());
            folder.put("admins", new JSONArray());
            newRoot.put("Users", folder);
            newRoot.put("Shared", folder);
            newRoot.put("type", "directory");
            newRoot.put("groups", groups);
            newRoot.put("blacklist", new JSONArray());
            newRoot.put("admins", new JSONArray());
            newCatalog.put("root", newRoot);

            try {
                JSONUtils.writeJSONObject(
                        MeshFS.properties.getProperty("repository") + ".catalog.json", newCatalog);
            } catch (IOException ignored) {
            }
        }

        TimerTask manifestCheck = new TimerTask() {
            @Override
            public void run() {
                Long currentTimeStamp = new Date().getTime();
                if (!(new File(MeshFS.properties.getProperty("repository") + ".manifest.json")
                        .exists())) {
                    try {
                        JSONUtils.writeJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json", new JSONObject());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                JSONObject manifest =
                        JSONUtils.getJSONObject(
                                MeshFS.properties.getProperty("repository") + ".manifest.json");
                for (Object computer : manifest.keySet()) {
                    Long nodeTimeStamp =
                            (Long) ((JSONObject) manifest.get(computer)).get("checkInTimestamp");
                    if (currentTimeStamp > nodeTimeStamp + 62000) {
                        FileRestore.restoreAllFilesFromComputer(computer.toString());
                        System.out.println(computer.toString() + " was removed from the manifest");
                    }
                }
            }
        };


        manifestTimer.scheduleAtFixedRate(manifestCheck, 0, 1000);
    }
}

/**
 * This class is responsible for shutting down the servers started by
 * certain modes.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

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
    }
}

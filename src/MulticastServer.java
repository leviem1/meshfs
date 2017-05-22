import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by Levi Muniz on 3/1/17.
 */

class MulticastServer {
    private Thread mcast;
    private MulticastSocket socket;
    private Timer checkMastersTimer;

    void startServer(String groupAddress, int port) throws IOException {
        InetAddress group = InetAddress.getByName(groupAddress);
        socket = new MulticastSocket(port);
        socket.setReuseAddress(true);
        socket.joinGroup(group);

        mcast = new Thread(new MulticastServerInit(socket));
        System.out.println("Multicast server initialized...");
        mcast.start();
        System.out.println("Multicast server started!");

        TimerTask checkMasters = new TimerTask() {
            ArrayList<String> mastersToRemove = new ArrayList<>();

            @Override
            public void run() {
                for (String address : MulticastServerInit.foundMasters) {
                    try {
                        if (FileClient.ping(address, Integer.parseInt(MeshFS.properties.getProperty("portNumber"))) == -1) {
                            mastersToRemove.add(address);
                        }
                    } catch (Exception ignored) {
                    }
                }
                MulticastServerInit.foundMasters.removeAll(mastersToRemove);
            }
        };

        checkMastersTimer = new Timer();
        checkMastersTimer.scheduleAtFixedRate(checkMasters, 0, 1000);
    }

    void stopServer() {
        if (socket.isBound()) {
            mcast.interrupt();
            socket.close();
            try {
                mcast.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            checkMastersTimer.cancel();
        }
    }

    List<String> getFoundMasters() {
        return MulticastServerInit.foundMasters;
    }
}

@SuppressWarnings("unchecked")
class MulticastServerInit implements Runnable {

    private final DatagramSocket socket;
    private volatile boolean masterDown = false;
    private volatile boolean recordVotes = false;
    private Timer voteCastScheduler = new Timer();
    static List<String> foundMasters = new ArrayList<>();
    private HashMap<InetAddress, Long> reportedDown = new HashMap<>();
    private volatile ConcurrentHashMap<String, String> newMasterVotes = new ConcurrentHashMap<>();

    MulticastServerInit(DatagramSocket socket) {
        this.socket = socket;
    }

    private void evaluateMaster(String ip, String port) {
        if ((!MeshFS.nogui) && (FileClient.ping(ip, Integer.parseInt(port)) > -1) && !foundMasters.contains(ip)) {
            foundMasters.add(ip);
        }
    }

    private void masterDownRecord(InetAddress address) {
        reportedDown.put(address, Instant.now().toEpochMilli());
        if ((reportedDown.size() > JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json").size() / 2) && !masterDown) {
            System.out.println("Master is offline");
            Thread thread = new Thread(
                    () -> {
                        masterDown = true;
                        recordVotes = true;
                        LinkedHashMap<String, Integer> speeds = new LinkedHashMap();
                        LinkedHashMap<String, Integer> sortedSpeeds = new LinkedHashMap();
                        sortedSpeeds.put("temp", -1);

                        JSONObject manifestFile = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");

                        for (Object MACAddress : manifestFile.keySet()) {
                            if (Long.parseLong((((JSONObject) manifestFile.get(MACAddress)).get("FreeSpace")).toString()) > 21474836480L) {
                                speeds.put((((JSONObject) manifestFile.get(MACAddress)).get("IP")).toString(), FileClient.ping((((JSONObject) manifestFile.get(MACAddress)).get("IP")).toString(), Integer.parseInt(MeshFS.properties.getProperty("portNumber"))));
                            }
                        }

                        for (String key : speeds.keySet()) {
                            Integer latency = speeds.get(key);
                            boolean isBroken = false;
                            if (latency == -1) {
                                continue;
                            }
                            for (String sortedKey : sortedSpeeds.keySet()) {
                                if (latency <= sortedSpeeds.get(sortedKey)) {
                                    LinkedHashMap<String, Integer> reorderStorageMap = (LinkedHashMap<String, Integer>) sortedSpeeds.clone();
                                    sortedSpeeds.clear();
                                    for (String reorderKey : reorderStorageMap.keySet()) {
                                        if (reorderKey.equals(sortedKey)) {
                                            sortedSpeeds.put(key, latency);
                                        }
                                        sortedSpeeds.put(reorderKey, reorderStorageMap.get(reorderKey));
                                    }
                                    isBroken = true;
                                    break;
                                }
                            }

                            if (!isBroken) {
                                sortedSpeeds.put(key, speeds.get(key));
                            }
                        }

                        sortedSpeeds.remove("temp");
                        String idealMaster = sortedSpeeds.entrySet().iterator().next().getKey();

                        TimerTask voteCaster = new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    MulticastClient.castVote(MeshFS.properties.getProperty("multicastGroup"), Integer.parseInt(MeshFS.properties.getProperty("multicastPort")), idealMaster);
                                } catch (IOException ignored) {
                                }
                            }
                        };

                        voteCastScheduler.scheduleAtFixedRate(voteCaster, 0, 5000);

                        int currNumVotes = newMasterVotes.size();
                        final ExecutorService service = Executors.newSingleThreadExecutor();

                        try {
                            while (true) {
                                int finalCurrNumVotes = currNumVotes;
                                ConcurrentHashMap<String, String> localNewMasterVotes = newMasterVotes;
                                final Future<Object> f = service.submit(() -> {
                                    while (true) {
                                        Thread.sleep(1000);
                                        if (localNewMasterVotes.size() > finalCurrNumVotes) {
                                            return localNewMasterVotes.size();
                                        }
                                    }
                                });

                                currNumVotes = (int) f.get(30, TimeUnit.SECONDS);
                            }
                        } catch (TimeoutException | ExecutionException | InterruptedException ignored) {
                            recordVotes = false;
                            voteCastScheduler.cancel();
                        }

                        HashMap<String, Integer> voteResults = new HashMap<>();

                        for (Map.Entry vote : newMasterVotes.entrySet()) {
                            if (voteResults.containsKey(vote.getValue().toString())) {
                                Integer currValue = voteResults.get(vote.getValue().toString());
                                voteResults.put(vote.getValue().toString(), ++currValue);
                            } else {
                                voteResults.put(vote.getValue().toString(), 1);
                            }
                        }

                        LinkedHashMap<String, Integer> sortedVoteResults = new LinkedHashMap<>();

                        sortedVoteResults.put("temp", -1);

                        for (String key : voteResults.keySet()) {
                            Integer storageAmount = voteResults.get(key);
                            boolean isBroken = false;

                            for (String sortedKey : sortedVoteResults.keySet()) {
                                if (storageAmount >= sortedVoteResults.get(sortedKey)) {
                                    LinkedHashMap<String, Integer> reorderStorageMap =
                                            (LinkedHashMap<String, Integer>) sortedVoteResults.clone();
                                    sortedVoteResults.clear();
                                    for (String reorderKey : reorderStorageMap.keySet()) {
                                        if (reorderKey.equals(sortedKey)) {
                                            sortedVoteResults.put(key, storageAmount);
                                        }
                                        sortedVoteResults.put(reorderKey, reorderStorageMap.get(reorderKey));
                                    }
                                    isBroken = true;
                                    break;
                                }
                            }

                            if (!isBroken) {
                                sortedVoteResults.put(key, voteResults.get(key));
                            }
                        }

                        String newMasterIP = sortedVoteResults.entrySet().iterator().next().getKey();

                        Properties properties = ConfigParser.loadProperties();
                        properties.setProperty("masterIP", newMasterIP);
                        ConfigParser.write(properties);
                        MeshFS.properties.setProperty("masterIP", newMasterIP);

                        reportedDown.clear();

                        System.out.println("Migrated to new master with IP of " + newMasterIP);

                        if (newMasterIP.equals(Reporting.getIpAddresses().get(0))) {
                            resetAsMaster();
                        }

                        try {
                            Thread.sleep(180000);
                        } catch (InterruptedException ignored) {}

                        masterDown = false;
                    }
            );

            thread.start();
        }
    }

    private void recordVote(String ip, String vote) {
        if (masterDown && recordVotes && MeshFS.nogui) {
            newMasterVotes.put(ip, vote);
        }
    }

    private void resetAsMaster() {
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

        TimerTask manifestCheck =
                new TimerTask() {
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
                        if (currentTimeStamp > nodeTimeStamp + 32000) {
                            try {
                                JSONUtils.deleteManifestItem(computer.toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println(computer.toString() + " was removed from the manifest");
                        }
                    }
                }
        };

        MeshFS.manifestTimer.scheduleAtFixedRate(manifestCheck, 0, 1000);

        MeshFS.scheduledReportingTimer.cancel();
        MeshFS.nodePanicTimer.cancel();
        MeshFS.scheduledReportingTimer.purge();
        MeshFS.nodePanicTimer.purge();
    }

    private void processRequest(String request, DatagramPacket dp) {
        String[] requestParts = request.split("\\|");

        switch (requestParts[0]) {
            case "151":
                evaluateMaster(requestParts[1], requestParts[2]);
                break;
            case "152":
                masterDownRecord(dp.getAddress());
                break;
            case "153":
                recordVote(dp.getAddress().toString(), requestParts[1]);
                break;
        }
    }

    public void run() {
        TimerTask removeReportedDown = new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry reportedDownEntry : reportedDown.entrySet()) {
                    if (Instant.now().toEpochMilli() > (long) reportedDownEntry.getValue() + 15000) {
                        reportedDown.remove(reportedDownEntry.getKey());
                    }
                }
            }
        };

        Timer removeReportedDownTimer = new Timer();
        removeReportedDownTimer.scheduleAtFixedRate(removeReportedDown, 0, 5000);

        while (!Thread.interrupted()) {
            byte data[] = new byte[4096];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            try {
                socket.receive(dp);
                processRequest(new String(dp.getData()).trim(), dp);
            } catch (IOException ignored) {}
        }
        voteCastScheduler.cancel();
        System.out.println("Multicast socket closed");
    }
}
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

/**
 * The MulticastServer class starts a server on a
 * group address, port, and with a specific number of threads
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

class MulticastServer {
    private final ArrayList<Thread> sockets = new ArrayList<>();
    private MulticastSocket socket;
    private Timer checkMastersTimer;

    /**
     * This method is used to start the multicast server.
     *
     * @param groupAddress the address to listen on
     * @param port         the port number to listen on
     * @param maxThreads   the amount of sockets
     * @throws IOException if a socket cannot be initialized
     */

    void startServer(String groupAddress, int port, int maxThreads) throws IOException {
        InetAddress group = InetAddress.getByName(groupAddress);
        socket = new MulticastSocket(port);
        socket.setReuseAddress(true);
        socket.joinGroup(group);

        for (int threads = 0; threads < maxThreads; threads++) {
            sockets.add(new Thread(new MulticastServerInit(socket)));
            System.out.println("Multicast socket " + threads + " initialized...");
        }

        for (int socket = 0; socket < sockets.size(); socket++) {
            (sockets.get(socket)).start();
            System.out.println("Multicast socket " + socket + " started!");
        }

        TimerTask checkMasters = new TimerTask() {
            final ArrayList<String> mastersToRemove = new ArrayList<>();

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

        TimerTask removeReportedDown = new TimerTask() {
            @Override
            public void run() {
                for (Map.Entry<InetAddress, Long> reportedDownEntry : MulticastServerInit.reportedDown.entrySet()) {
                    if (Instant.now().toEpochMilli() > reportedDownEntry.getValue() + 15000) {
                        MulticastServerInit.reportedDown.remove(reportedDownEntry.getKey());
                    }
                }
            }
        };

        Timer removeReportedDownTimer = new Timer();
        removeReportedDownTimer.scheduleAtFixedRate(removeReportedDown, 0, 5000);

    }

    /**
     * This method is used to halt a server.
     */

    void stopServer() {
        if (socket.isBound()) {
            for (Thread socket : sockets) {
                socket.interrupt();
            }

            socket.close();

            for (Thread socket : sockets) {
                try {
                    socket.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            checkMastersTimer.cancel();
        }
    }

    /**
     * This method is used to get the list of currently broadcasting masters
     *
     * @return current list of known available masters
     */

    List<String> getFoundMasters() {
        return MulticastServerInit.foundMasters;
    }
}

@SuppressWarnings("unchecked")

class MulticastServerInit implements Runnable {

    static final CopyOnWriteArrayList<String> foundMasters = new CopyOnWriteArrayList<>();
    static final ConcurrentHashMap<InetAddress, Long> reportedDown = new ConcurrentHashMap<>();
    private static volatile boolean masterDown = false;
    private static volatile boolean recordVotes = false;
    private static final Timer voteCastScheduler = new Timer();
    private static final ConcurrentHashMap<String, String> newMasterVotes = new ConcurrentHashMap<>();
    private final DatagramSocket socket;

    MulticastServerInit(DatagramSocket socket) {
        this.socket = socket;
    }

    private void evaluateMaster(String ip, String port, String MACAddress) {
        if ((!MeshFS.nogui) && (FileClient.ping(ip, Integer.parseInt(port)) > -1) && !foundMasters.contains(ip)) {
            foundMasters.add(ip);
        } else if (!MeshFS.isMaster && MACAddress.equals(MeshFS.masterMAC) && FileClient.ping(MeshFS.properties.getProperty("masterIP"), Integer.parseInt(MeshFS.properties.getProperty("portNumber"))) == -1 && FileClient.ping(ip, Integer.parseInt(port)) > -1) {
            MeshFS.properties.setProperty("masterIP", ip);
            MeshFS.properties.setProperty("portNumber", port);
            ConfigParser.write(MeshFS.properties);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    private void masterDownRecord(InetAddress address) {
        reportedDown.put(address, Instant.now().toEpochMilli());
        if (!MeshFS.isMaster && !masterDown && MeshFS.numFailedConn[0] > 3 && (reportedDown.size() > JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json").size() / 2)) {
            System.err.println("Master is offline");
            Thread thread = new Thread(
                    () -> {
                        masterDown = true;
                        recordVotes = true;
                        LinkedHashMap<String, Long> speeds = new LinkedHashMap();

                        JSONObject manifestFile = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");

                        for (Object MACAddress : manifestFile.keySet()) {
                            if (Long.parseLong((((JSONObject) manifestFile.get(MACAddress)).get("FreeSpace")).toString()) > 21474836480L) {
                                speeds.put((((JSONObject) manifestFile.get(MACAddress)).get("IP")).toString(), (long) FileClient.ping((((JSONObject) manifestFile.get(MACAddress)).get("IP")).toString(), Integer.parseInt(MeshFS.properties.getProperty("portNumber"))));
                            }
                        }

                        LinkedHashMap<String, Long> sortedSpeeds = JSONUtils.sortMapByValue(speeds, true);

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

                        LinkedHashMap<String, Long> voteResults = new LinkedHashMap<>();

                        for (Map.Entry vote : newMasterVotes.entrySet()) {
                            if (voteResults.containsKey(vote.getValue().toString())) {
                                Long currValue = voteResults.get(vote.getValue().toString());
                                voteResults.put(vote.getValue().toString(), ++currValue);
                            } else {
                                voteResults.put(vote.getValue().toString(), 1L);
                            }
                        }

                        LinkedHashMap<String, Long> sortedVoteResults = JSONUtils.sortMapByValue(voteResults, false);

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
                        } catch (InterruptedException ignored) {
                        }

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
        MeshFS.startAsMaster();

        MeshFS.scheduledReportingTimer.cancel();
        MeshFS.nodePanicTimer.cancel();
        MeshFS.scheduledReportingTimer.purge();
        MeshFS.nodePanicTimer.purge();
    }

    private void processRequest(String request, DatagramPacket dp) {
        String[] requestParts = request.split("\\|");

        switch (requestParts[0]) {
            case "151":
                evaluateMaster(requestParts[1], requestParts[2], requestParts[3]);
                break;
            case "152":
                masterDownRecord(dp.getAddress());
                break;
            case "153":
                recordVote(dp.getAddress().toString(), requestParts[1]);
                break;
            default:
                break;
        }
    }

    public void run() {
        while (!Thread.interrupted()) {
            byte data[] = new byte[4096];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            try {
                socket.receive(dp);
                processRequest(new String(dp.getData()).trim(), dp);
            } catch (IOException ignored) {
            }
        }
        voteCastScheduler.cancel();
        System.out.println("Multicast socket closed");
    }
}
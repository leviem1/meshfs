import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.*;
import java.util.*;

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
                    }catch(Exception ignored){
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
    private Set<InetAddress> reportedDown = new LinkedHashSet<>();
    private Timer voteCastScheduler = new Timer();
    private boolean masterDown = false;
    static List<String> foundMasters = new ArrayList();

    MulticastServerInit(DatagramSocket socket) {
        this.socket = socket;
    }

    private void evaluateMaster(String ip, String port) {
        if ((!MeshFS.nogui) && (FileClient.ping(ip, Integer.parseInt(port)) > -1) && !foundMasters.contains(ip)) {
            foundMasters.add(ip);
        }
    }

    private void masterDownRecord(InetAddress address) {
        reportedDown.add(address);
        if ((reportedDown.size() > JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest").size() / 2) && !masterDown) {
            masterDown = true;
            LinkedHashMap<String, Integer> speeds = new LinkedHashMap();
            LinkedHashMap<String, Integer> sortedSpeeds = new LinkedHashMap();
            sortedSpeeds.put("temp", -1);

            JSONObject manifestFile = JSONUtils.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest");

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
        }
    }

    private void recordVote(String ip, String vote) {

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
        while (!Thread.interrupted()) {
            byte data[] = new byte[4096];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            try {
                socket.receive(dp);
                //if (!dp.getAddress().equals(InetAddress.getByName(Reporting.getIpAddresses().get(0)))) {
                processRequest(new String(dp.getData()).trim(), dp);
                //}
            } catch (SocketTimeoutException | SocketException ignored) {
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        voteCastScheduler.cancel();
        System.out.println("Multicast socket closed");
    }
}
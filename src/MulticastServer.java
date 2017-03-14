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

    void startServer(String groupAddress, int port) throws IOException {
        InetAddress group = InetAddress.getByName(groupAddress);
        socket = new MulticastSocket(port);
        socket.joinGroup(group);

        mcast = new Thread(new MulticastServerInit(socket));
        System.out.println("Multicast server initialized...");
        mcast.start();
        System.out.println("Multicast server started!");
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
        }
    }
}

class MulticastServerInit implements Runnable {

    private final DatagramSocket socket;
    private Set<InetAddress> reportedDown = new LinkedHashSet<>();
    private Timer voteCastScheduler = new Timer();
    private boolean masterDown = false;

    MulticastServerInit(DatagramSocket socket) {
        this.socket = socket;
    }

    private void evaluateMaster(String ip, String port) {
        if ((!MeshFS.isMaster) && (!ip.equals(MeshFS.properties.getProperty("masterIP"))) && !(FileClient.ping(MeshFS.properties.getProperty("masterIP"), Integer.parseInt(MeshFS.properties.getProperty("portNumber"))) > -1) && (FileClient.ping(ip, Integer.parseInt(port)) > -1)) {
            MeshFS.properties.setProperty("masterIP", ip);
            MeshFS.properties.setProperty("portNumber", port);
        }
    }

    private void masterDownRecord(InetAddress address) {
        reportedDown.add(address);
        if ((reportedDown.size() > JSONManipulator.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest").size() / 2) && !masterDown) {
            masterDown = true;
            LinkedHashMap<String, Integer> speeds = new LinkedHashMap();
            LinkedHashMap<String, Integer> sortedSpeeds = new LinkedHashMap();
            sortedSpeeds.put("temp", -1);

            JSONObject manifestFile = JSONManipulator.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest");

            for (Object MACAddress : manifestFile.keySet()) {
                if (Long.parseLong((((JSONObject) manifestFile.get(MACAddress)).get("FreeSpace")).toString()) > 21474836480L) {
                    speeds.put((((JSONObject) manifestFile.get(MACAddress)).get("IP")).toString(), FileClient.ping((((JSONObject) manifestFile.get(MACAddress)).get("IP")).toString(), Integer.parseInt(MeshFS.properties.getProperty("portNumber"))));
                }
            }

            for (String key : speeds.keySet()) {
                Integer latency = speeds.get(key);
                boolean isBroken = false;
                if (latency == -1) {continue;}
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

    private void processRequest(String request, DatagramPacket dp) {
        String[] requestParts = request.split("\\|");

        if (requestParts[0].equals("151")) {
            evaluateMaster(requestParts[1], requestParts[2]);
        } else if (requestParts[0].equals("152")) {
            masterDownRecord(dp.getAddress());
        }
    }

    public void run() {
        while (!Thread.interrupted()) {
            byte data[] = new byte[4096];
            DatagramPacket dp = new DatagramPacket(data, data.length);
            try {
                socket.receive(dp);
                if (!dp.getAddress().equals(InetAddress.getByName(Reporting.getIpAddresses().get(0)))) {
                    processRequest(new String(dp.getData()).trim(), dp);
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        voteCastScheduler.cancel();
        System.out.println("Multicast socket closed");
    }
}
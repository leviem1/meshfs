import java.io.IOException;
import java.net.*;
import java.util.LinkedHashSet;
import java.util.Set;

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

    MulticastServerInit(DatagramSocket socket) {
        this.socket = socket;
    }

    private void evaluateMaster (String ip, String port) {
        if ((!MeshFS.isMaster) && (!ip.equals(MeshFS.properties.getProperty("masterIP"))) && (!FileClient.ping(MeshFS.properties.getProperty("masterIP"), Integer.parseInt(MeshFS.properties.getProperty("portNumber"))) && (FileClient.ping(ip, Integer.parseInt(port))))) {
            MeshFS.properties.setProperty("masterIP", ip);
            MeshFS.properties.setProperty("portNumber", port);
        }
    }

    private void masterDownRecord(InetAddress address) {
        reportedDown.add(address);
        //TODO: Get count of total ip's in manifest
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
        while(!Thread.interrupted()) {
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
        System.out.println("Multicast socket closed");
    }
}
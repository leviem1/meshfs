import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created by Levi Muniz on 3/1/17.
 */
class MulticastClient {

    private MulticastClient() {
    }

    static void notifyClients(String groupAddress, int port) throws IOException {
        byte[] request = ("151|" + Reporting.getIpAddresses().get(0) + "|" + MeshFS.properties.getProperty("portNumber")).getBytes();

        MulticastSocket socket = new MulticastSocket(port);
        socket.setBroadcast(true);
        socket.send(new DatagramPacket(request, request.length, InetAddress.getByName(groupAddress), port));
        socket.close();
    }

    static void masterDownInform(String groupAddress, int port) throws IOException {
        byte[] request = "152".getBytes();

        MulticastSocket socket = new MulticastSocket(port);
        socket.setBroadcast(true);
        socket.send(new DatagramPacket(request, request.length, InetAddress.getByName(groupAddress), port));
        socket.close();
    }

    static void castVote(String groupAddress, int port, String vote) throws IOException {
        byte[] request = ("153|" + vote).getBytes();

        MulticastSocket socket = new MulticastSocket(port);
        socket.setBroadcast(true);
        socket.send(new DatagramPacket(request, request.length, InetAddress.getByName(groupAddress), port));
        socket.close();
    }
}
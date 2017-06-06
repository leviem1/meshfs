import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * This class is used to send multicast packets to the group
 *
 * @author Levi Muniz
 * @version 1.0.0
 */
class MulticastClient {

    private MulticastClient() {}

    /**
     * This method is used by the master to notify the nodes of the master's
     * presence.
     *
     * @param groupAddress  address to broadcast to
     * @param port          port to broadcast to
     * @throws IOException  if address or port cannot be bound
     */

    static void notifyClients(String groupAddress, int port) throws IOException {
        byte[] request = ("151|" + Reporting.getIpAddresses().get(0) + "|" + MeshFS.properties.getProperty("portNumber") + "|" + Reporting.getMacAddress()).getBytes();

        MulticastSocket socket = new MulticastSocket(port);
        socket.setBroadcast(true);
        socket.send(new DatagramPacket(request, request.length, InetAddress.getByName(groupAddress), port));
        socket.close();
    }

    /**
     * This method is to inform other nodes that the current node has
     * detected that the master may be offline
     *
     * @param groupAddress  address to broadcast to
     * @param port          port to broadcast to
     * @throws IOException  if address or port cannot be bound
     */

    static void masterDownInform(String groupAddress, int port) throws IOException {
        byte[] request = "152".getBytes();

        MulticastSocket socket = new MulticastSocket(port);
        socket.setBroadcast(true);
        socket.send(new DatagramPacket(request, request.length, InetAddress.getByName(groupAddress), port));
        socket.close();
    }

    /**
     * This method is used to cast a vote for the most ideal master
     *
     * @param groupAddress  address to broadcast to
     * @param port          port to broadcast to
     * @param vote          address of desired master
     * @throws IOException  if address or port cannot be bound
     */

    static void castVote(String groupAddress, int port, String vote) throws IOException {
        byte[] request = ("153|" + vote).getBytes();

        MulticastSocket socket = new MulticastSocket(port);
        socket.setBroadcast(true);
        socket.send(new DatagramPacket(request, request.length, InetAddress.getByName(groupAddress), port));
        socket.close();
    }
}
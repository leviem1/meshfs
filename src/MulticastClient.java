import java.io.IOException;
import java.net.*;

/**
 * Created by Levi Muniz on 3/1/17.
 */
class MulticastClient {
    static void notifyClients(String groupAddress, int port) throws IOException {
        byte[] request = ("151|" +  Reporting.getIpAddresses().get(0) + "|" + MeshFS.properties.getProperty("portNumber")).getBytes();
        InetAddress group = InetAddress.getByName(groupAddress);
        MulticastSocket socket = new MulticastSocket(port);

        socket.setBroadcast(true);
        socket.send(new DatagramPacket(request, request.length, group, port));

        socket.close();
    }
}
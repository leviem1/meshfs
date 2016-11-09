/**
 * Created by Levi Muniz on 10/16/16.
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class FileServer {

    public void startServer(int port, int maxThreads, int timeout) {
        ArrayList<Thread> sockets = new ArrayList<>();
        try {

            ServerSocket FileServer = new ServerSocket(port);

            for (int threads = 0; threads < maxThreads; threads++) {
                sockets.add(new Thread(new ServerInit(FileServer, timeout)));
                System.out.println("Socket " + threads + " initialized...");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int socket = 0; socket < sockets.size(); socket++) {
            (sockets.get(socket)).start();
            System.out.println("Socket " + socket + " started!");
        }
    }
}

class ServerInit implements Runnable {
    private ServerSocket server;
    private int timeout;

    public ServerInit(ServerSocket server, int timeout) {
        this.server = server;
        this.timeout = timeout;
    }

    private String receiveRequest(Socket client) {
        String requestPart;
        String requestFull = "";
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            while (((requestPart = input.readLine()) != null) && (requestFull.length() < 2048)) {
                requestFull = requestFull + requestPart;
            }

            return requestFull;
        } catch (IOException io) {
            return requestFull;
        }
    }

    public void run() {
            while (!Thread.interrupted()) {
                try {
                    Socket client = server.accept();
                    client.setSoTimeout(timeout);
                    String requestString = receiveRequest(client);
                    client.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
    }
}

/**
 * Created by Levi Muniz on 10/16/16.
 */
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * The FileServer class starts a file server
 * with a variable port, amount of sockets, and
 * timeout.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

public class FileServer {

    private ArrayList<Thread> sockets = new ArrayList<>();

    /**
     * This method is used to start the file server.
     * @param port       the port number
     * @param maxThreads the amount of sockets
     * @param timeout    the maximum amount of seconds that a client can be idle for
     * @throws Exception if a socket cannot be initialized for any reason
     */

    public void startServer(int port, int maxThreads, int timeout) throws Exception {

        ServerSocket FileServer = new ServerSocket();
        FileServer.setPerformancePreferences(1,0,1);

        if (MeshFS.properties.getProperty("preferredInterface").equals("")) {
            FileServer.bind(new InetSocketAddress(port));
        } else {
            NetworkInterface iface = NetworkInterface.getByName(MeshFS.properties.getProperty("preferredInterface"));
            Enumeration<InetAddress> ipList = iface.getInetAddresses();
            InetAddress ip = InetAddress.getByName(ipList.nextElement().getHostName());
            FileServer.bind(new InetSocketAddress(ip, port));
        }

        for (int threads = 0; threads < maxThreads; threads++) {
            sockets.add(new Thread(new ServerInit(FileServer, timeout)));
            System.out.println("Socket " + threads + " initialized...");
        }

        for (int socket = 0; socket < sockets.size(); socket++) {
            (sockets.get(socket)).start();
            System.out.println("Socket " + socket + " started!");
        }
    }

    /**
     * This method is used to halt a server.
     */

    public void stopServer() {
        for (int thread = 0; thread < sockets.size(); thread++) {
            sockets.get(thread).interrupt();
        }
        for (int thread = 0; thread < sockets.size(); thread++) {
            try {
                sockets.get(thread).join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

class ServerInit implements Runnable {
    private static ServerSocket server;
    private int timeout;

    public ServerInit(ServerSocket server, int timeout) {
        this.server = server;
        this.timeout = timeout;
    }

    private void processRequest(String request, Socket out) {
        if (request != null) {
            try {
                PrintWriter client = new PrintWriter(out.getOutputStream(), true);

                String[] requestParts = request.split("\\|");
                if (requestParts[0].equals("101")) {            //101:Get file
                    client.println("201");
                    sendFile(requestParts[1], out);

                } else if (requestParts[0].equals("102")) {     //102:Post file
                    client.println("201");
                    receiveFile(requestParts[1], out);

                } else if (requestParts[0].equals("103")) {     //103:Move file (virtual only)
                    client.println("201");
                    //moveFile(requestParts[1], requestParts[2]);

                } else if (requestParts[0].equals("104")) {     //104:Copy file (virtual only)
                    client.println("201");
                    //copyFile(requestParts[1], requestParts[2]);

                } else if (requestParts[0].equals("105")) {     //105:Delete file (virtual and physical)
                    client.println("201");
                    //deleteFile(requestParts[1], requestParts[2]);

                } else if (requestParts[0].equals("106")) {     //106:Make directory (virtual)
                    client.println("201");
                    //makeDir(requestParts[1]);

                } else if (requestParts[0].equals("107")) {     //107:Get report
                    client.println("201");
                    sendReport(out);

                } else if (requestParts[0].equals("108")) {     //108:Post report
                    client.println("201");
                    //receiveReport(requestParts[1]);

                } else if (requestParts[0].equals("109")) {     //109:Ping
                    client.println("201");
                    ping(out);

                } else if (requestParts[0].equals("110")) {     //110:Bind
                    client.println("201");
                    //bindClient(requestParts[1], requestParts[2]);

                } else {
                    badRequest(out, request);
                }
            } catch (Exception e) {
                badRequest(out, request);
                e.printStackTrace();
            }
        }
    }

    private void ping(Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();
    }

    private void badRequest(Socket client, String request) {
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream());
            out.println("ERROR 202\n Bad request:\n\n" + request);
            out.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private void sendReport(Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.print("108|");
        out.println(Reporting.generate());
        out.print("EOR");
        out.flush();
    }

    private void receiveReport(String report){

    }

    private void sendFile(String filename, Socket client) throws Exception {
        int br;
        byte[] data = new byte[4096];
        PrintWriter out = new PrintWriter(client.getOutputStream());
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        FileInputStream fis = new FileInputStream(MeshFS.properties.getProperty("repository") + filename);
        while ((br = fis.read(data, 0, data.length)) != -1) {
            dos.write(data, 0, br);
        }

        fis.close();
        dos.close();
    }

    private void receiveFile(String filename, Socket client) throws Exception{
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        if (!input.readLine().equals("201")) return;
        int br;
        byte[] data = new byte[4096];
        DataInputStream dis = new DataInputStream(client.getInputStream());
        FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + filename);

        while ((br = dis.read(data, 0, data.length)) != -1){
            fos.write(data, 0, br);
        }

        fos.close();
        dis.close();
    }

    private String receiveRequest(Socket client) {
        String requestPart;
        String requestFull = "";
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

            while (((requestPart = input.readLine()) != null) && (requestFull.length() < 2048)) {
                if (requestPart.equals("")) break;
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
                    server.setSoTimeout(1000);
                    Socket client = server.accept();
                    client.setSoTimeout(timeout);
                    processRequest(receiveRequest(client), client);
                    client.close();
                } catch (SocketTimeoutException ste) {
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
            System.out.println("Socket closed");
    }
}

/**
 * Created by Levi Muniz on 10/16/16.
 */

import org.json.simple.JSONObject;
import java.io.*;
import java.net.*;
import java.util.ArrayList;


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
    private ServerSocket fileServer;

    /**
     * This method is used to start the file server.
     *
     * @param port       the port number
     * @param maxThreads the amount of sockets
     * @param timeout    the maximum amount of seconds that a client can be idle for
     * @throws IOException if a socket cannot be initialized
     */

    public void startServer(int port, int maxThreads, int timeout) throws IOException {

        fileServer = new ServerSocket();
        fileServer.setPerformancePreferences(1, 0, 1);
        fileServer.bind(new InetSocketAddress(port));

        for (int threads = 0; threads < maxThreads; threads++) {
            sockets.add(new Thread(new ServerInit(fileServer, timeout)));
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
        if (fileServer.isBound()) {
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
}

class ServerInit implements Runnable {
    private ServerSocket server;
    private int timeout;

    public ServerInit(ServerSocket server, int timeout) {
        this.server = server;
        this.timeout = timeout;
    }

    @SuppressWarnings("deprecation")
    private String receiveRequest(Socket client) {
        String requestPart;
        String requestFull = "";
        try {
            DataInputStream input = new DataInputStream(client.getInputStream());

            while (((requestPart = input.readLine()) != null) && (requestFull.length() < 2048)) {
                if (requestPart.equals("")) break;
                requestFull = requestFull + requestPart;
            }

            return requestFull;
        } catch (IOException ioe) {
            return requestFull;
        }
    }

    private void processRequest(String request, Socket out) {

        if (request != null) {
            try {
                String[] requestParts = request.trim().split("\\|");
                if (requestParts[0].equals("101")) {            //101:Get file
                    sendFile(requestParts[1], out);

                } else if (requestParts[0].equals("102")) {     //102:Post file
                    receiveFile(requestParts[1], out);

                } else if (requestParts[0].equals("103")) {     //103:Move file (virtual only)
                    moveFile(requestParts[1], requestParts[2], out);

                } else if (requestParts[0].equals("104")) {     //104:Copy file (virtual only)
                    duplicateFile(requestParts[1], out);

                } else if (requestParts[0].equals("105")) {     //105:Delete file (virtual and physical)
                    deleteFile(requestParts[1], out);

                } else if (requestParts[0].equals("106")) {     //106:Make directory (virtual)
                    createDirectory(requestParts[1], requestParts[2], out, requestParts[3]);

                } else if (requestParts[0].equals("107")) {     //107:Get report
                    sendReport(out);

                } else if (requestParts[0].equals("108")) {     //108:Post report
                    receiveReport(out);

                } else if (requestParts[0].equals("109")) {     //109:Ping
                    ping(out);

                } else if (requestParts[0].equals("110")) {     //110:Rename File (virtual only)
                    renameFile(requestParts[1], requestParts[2], out);

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

    private void sendReport(Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.print(Reporting.generate());
        out.flush();
    }

    private void moveFile(String currentPath, String newPath, Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();
        JSONObject jsonObj = JSONManipulator.getJSONObject(MeshFS.properties.getProperty("repository")+".catalog.json");
        JSONManipulator.writeJSONObject(MeshFS.properties.getProperty("repository")+".catalog.json", JSONManipulator.moveFile(jsonObj, currentPath.toString(), newPath));
    }

    private void deleteFile(String jsonPath, Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();

        JSONObject jsonObj = JSONManipulator.getJSONObject(MeshFS.properties.getProperty("repository")+".catalog.json");
        JSONManipulator.writeJSONObject(MeshFS.properties.getProperty("repository")+".catalog.json", JSONManipulator.removeItem(jsonObj, jsonPath));
    }

    private void duplicateFile(String currentPath, Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();
        JSONObject jsonObj = JSONManipulator.getJSONObject(MeshFS.properties.getProperty("repository")+".catalog.json");
        JSONManipulator.writeJSONObject(MeshFS.properties.getProperty("repository")+".catalog.json", JSONManipulator.copyFile(jsonObj, currentPath, currentPath.substring(0, currentPath.lastIndexOf("/")), true));
    }

    private void receiveReport(Socket client) throws IOException {
        String reportPart;
        String reportFull = "";
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        out.println("201");

        while (true) {
            reportPart = input.readLine();
            if ((reportPart == null) || (reportPart.equals("\n"))) break;
            reportFull = reportFull + reportPart + "\n";
        }
        reportFull = reportFull.trim();
        JSONManipulator.writeJSONObject("manifest.json", Reporting.splitter(reportFull));
    }

    private void sendFile(String filename, Socket client) throws IOException {
        int br;
        byte[] data = new byte[4096];
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        out.println("201");

        FileInputStream fis = new FileInputStream(MeshFS.properties.getProperty("repository") + filename);

        while ((br = fis.read(data, 0, data.length)) != -1) {
            dos.write(data, 0, br);
            dos.flush();
        }

        fis.close();
        dos.close();
    }

    private void receiveFile(String filename, Socket client) throws IOException {
        int br;
        byte[] data = new byte[4096];
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        out.println("201");

        DataInputStream dis = new DataInputStream(client.getInputStream());
        FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + filename);

        while ((br = dis.read(data, 0, data.length)) != -1){
            fos.write(data, 0, br);
            fos.flush();
        }
        out.close();
        fos.close();
        dis.close();

        Thread distributor = new Thread() {
            public void run() {
                Distributor distributorObj = new Distributor(Integer.parseInt(MeshFS.properties.getProperty("numStripes")), Integer.parseInt(MeshFS.properties.getProperty("numWholeCopy")), Integer.parseInt(MeshFS.properties.getProperty("numStripeCopy")));
                //Run Distributor Code
            }
        };
        distributor.setDaemon(true);
        distributor.start();

    }

    private void createDirectory(String directoryPath, String directoryName, Socket client, String userAccount) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();
        JSONObject jsonObj = JSONManipulator.getJSONObject(MeshFS.properties.getProperty("repository")+".catalog.json");
        JSONManipulator.writeJSONObject(MeshFS.properties.getProperty("repository")+".catalog.json", JSONManipulator.addFolder(jsonObj, directoryPath, directoryName, userAccount));

    }

    private void renameFile(String jsonPath, String newName, Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();

        JSONObject jsonObj = JSONManipulator.getJSONObject(MeshFS.properties.getProperty("repository")+".catalog.json");
        JSONManipulator.writeJSONObject(MeshFS.properties.getProperty("repository")+".catalog.json", JSONManipulator.renameFile(jsonObj, jsonPath, newName));
    }

    private void badRequest(Socket client, String request) {
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream());
            out.println("202\nBad request:\n\n" + request);
            out.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * The FileServer class starts a file server with a variable port, amount of sockets, and timeout.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */
class FileServer {

    private final ArrayList<Thread> sockets = new ArrayList<>();
    private ServerSocket fileServer;

    /**
     * This method is used to start the file server.
     *
     * @param port       the port number
     * @param maxThreads the amount of sockets
     * @param timeout    the maximum amount of seconds that a client can be idle for
     * @throws IOException if a socket cannot be initialized
     */
    void startServer(int port, int maxThreads, int timeout) throws IOException {
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
    void stopServer() {
        if (fileServer.isBound()) {
            for (Thread socket : sockets) {
                socket.interrupt();
            }

            try {
                fileServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (Thread socket : sockets) {
                try {
                    socket.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class ServerInit implements Runnable {
    private final ServerSocket server;
    private final int timeout;

    ServerInit(ServerSocket server, int timeout) {
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
                switch (requestParts[0]) {
                    case "101": //101:Get file
                        sendFile(requestParts[1], out);

                        break;
                    case "102": //102:Post file
                        if (requestParts.length == 3) {
                            receiveFile(requestParts[1], requestParts[2], out);
                        } else {
                            receiveFile(requestParts[1], out);
                        }

                        break;
                    case "103": //103:Move file (virtual only)
                        moveFile(requestParts[1], requestParts[2], out);

                        break;
                    case "104": //104:Copy file (virtual only)
                        duplicateFile(requestParts[1], out);

                        break;
                    case "105": //105:Delete file (virtual and physical)
                        deleteFile(requestParts[1], out);

                        break;
                    case "106": //106:Make directory (virtual only)
                        createDirectory(requestParts[1], requestParts[2], out, requestParts[3]);

                        break;
                    case "107": //107:Get report
                        sendReport(out);

                        break;
                    case "108": //108:Post report
                        receiveReport(out);

                        break;
                    case "109": //109:Ping
                        ping(out);

                        break;
                    case "110": //110:Rename File (virtual only)
                        renameFile(requestParts[1], requestParts[2], out);

                        break;
                    default:
                        badRequest(out, request);
                        break;
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
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println("201");
        out.println(Reporting.generate() + "\n");
    }

    private synchronized void receiveReport(Socket client) throws IOException {
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

        JSONObject manifest = new JSONObject();
        if (new File(MeshFS.properties.getProperty("repository") + ".manifest.json").exists()) {
            manifest =
                    JSONManipulator.getJSONObject(
                            MeshFS.properties.getProperty("repository") + ".manifest.json");
        }
        JSONArray reportArray = Reporting.splitter(reportFull);

        manifest.put(reportArray.get(0), reportArray.get(1));
        JSONManipulator.writeJSONObject(
                MeshFS.properties.getProperty("repository") + ".manifest.json", manifest);
    }

    private void moveFile(String currentPath, String newPath, Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();
        JSONObject jsonObj =
                JSONManipulator.getJSONObject(
                        MeshFS.properties.getProperty("repository") + ".catalog.json");
        JSONManipulator.writeJSONObject(
                MeshFS.properties.getProperty("repository") + ".catalog.json",
                JSONManipulator.moveFile(jsonObj, currentPath, newPath));
    }

    private void deleteFile(String jsonPath, Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();

        JSONObject jsonObj =
                JSONManipulator.getJSONObject(
                        MeshFS.properties.getProperty("repository") + ".catalog.json");
        JSONManipulator.writeJSONObject(
                MeshFS.properties.getProperty("repository") + ".catalog.json",
                JSONManipulator.removeItem(jsonObj, jsonPath));
    }

    private void duplicateFile(String currentPath, Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();
        JSONObject jsonObj =
                JSONManipulator.getJSONObject(
                        MeshFS.properties.getProperty("repository") + ".catalog.json");
        JSONManipulator.writeJSONObject(
                MeshFS.properties.getProperty("repository") + ".catalog.json",
                JSONManipulator.copyFile(
                        jsonObj,
                        currentPath,
                        currentPath.substring(0, currentPath.lastIndexOf("/")),
                        true,
                        currentPath.substring(currentPath.lastIndexOf("/") + 1)));
    }

    private void sendFile(String filename, Socket client) throws IOException {
        int br;
        byte[] data = new byte[4096];
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        FileInputStream fis =
                new FileInputStream(MeshFS.properties.getProperty("repository") + filename);

        out.println("201");

        while ((br = fis.read(data, 0, data.length)) != -1) {
            dos.write(data, 0, br);
            dos.flush();
        }

        out.close();
        fis.close();
        dos.close();
    }

    private void receiveFile(String filename, Socket client) throws IOException {
        int br;
        byte[] data = new byte[4096];
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(client.getInputStream());
        FileOutputStream fos =
                new FileOutputStream(MeshFS.properties.getProperty("repository") + filename);

        out.println("201");

        while ((br = dis.read(data, 0, data.length)) != -1) {
            fos.write(data, 0, br);
            fos.flush();
        }

        out.close();
        fos.close();
        dis.close();
    }

    private void receiveFile(String filename, String userAccount, Socket client) throws IOException {
        int br;
        byte[] data = new byte[4096];
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(client.getInputStream());
        FileOutputStream fos =
                new FileOutputStream(MeshFS.properties.getProperty("repository") + filename);

        out.println("201");
        JSONManipulator.addToIndex(
                userAccount,
                filename + " (uploading)",
                MeshFS.properties.getProperty("repository") + ".catalog.json",
                userAccount);

        while ((br = dis.read(data, 0, data.length)) != -1) {
            fos.write(data, 0, br);
            fos.flush();
        }

        out.close();
        fos.close();
        dis.close();

        JSONManipulator.writeJSONObject(
                MeshFS.properties.getProperty("repository") + ".catalog.json",
                JSONManipulator.removeItem(
                        JSONManipulator.getJSONObject(
                                MeshFS.properties.getProperty("repository") + ".catalog.json"),
                        userAccount + "/" + filename + " (uploading)"));

        Thread distributor = new Thread(() -> DISTAL.distributor(filename, userAccount));
        distributor.start();
    }

    private void createDirectory(
            String directoryPath, String directoryName, Socket client, String userAccount)
            throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();
        JSONObject jsonObj =
                JSONManipulator.getJSONObject(
                        MeshFS.properties.getProperty("repository") + ".catalog.json");
        JSONManipulator.writeJSONObject(
                MeshFS.properties.getProperty("repository") + ".catalog.json",
                JSONManipulator.addFolder(jsonObj, directoryPath, directoryName, userAccount));
    }

    private void renameFile(String jsonPath, String newName, Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("201");
        out.flush();

        JSONObject jsonObj =
                JSONManipulator.getJSONObject(
                        MeshFS.properties.getProperty("repository") + ".catalog.json");
        JSONManipulator.writeJSONObject(
                MeshFS.properties.getProperty("repository") + ".catalog.json",
                JSONManipulator.renameFile(jsonObj, jsonPath, newName));
    }

    private void badRequest(Socket client, String request) {
        try {
            if (client.isClosed()) return;
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
                server.setSoTimeout(timeout);
                Socket client = server.accept();
                processRequest(receiveRequest(client), client);
                client.close();
            } catch (SocketTimeoutException | SocketException ignored) {
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
        System.out.println("Socket closed");
    }
}

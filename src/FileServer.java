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

    private void processRequest(String request, Socket out) {
        if (request != null) {
            try {
                String[] requestParts = request.split("\\|");
                if (requestParts[0].equals("101")) {            //101:Get file
                    sendFile(requestParts[1], out);

                } else if (requestParts[0].equals("102")) {     //102:Post file
                    receiveFile(requestParts[1], out);

                } else if (requestParts[0].equals("103")) {     //103:Move file (virtual only)
                    //moveFile(requestParts[1], requestParts[2]);

                } else if (requestParts[0].equals("104")) {     //104:Copy file (virtual only)
                    //copyFile(requestParts[1], requestParts[2]);

                } else if (requestParts[0].equals("105")) {     //105:Delete file (virtual and physical)
                    //deleteFile(requestParts[1], requestParts[2]);

                } else if (requestParts[0].equals("106")) {     //106:Make directory (virtual)
                    //makeDir(requestParts[1]);

                } else if (requestParts[0].equals("107")) {     //107:Get report
                    sendReport(out);

                } else if (requestParts[0].equals("108")) {     //108:Post report
                    //receiveReport(requestParts[1]);

                } else if (requestParts[0].equals("109")) {     //109:Ping
                    ping(out);

                } else if (requestParts[0].equals("110")) {     //110:Bind
                    //bindClient(requestParts[1], requestParts[2]);

                } else {
                    badRequest(out, request);
                }
            } catch (Exception e) {
                badRequest(out, request);
            }
        }
    }

    private void ping(Socket client) throws IOException {
        PrintWriter out = new PrintWriter(client.getOutputStream());
        out.println("Pong!");
        out.flush();
    }

    private void badRequest(Socket client, String request) {
        try {
            PrintWriter out = new PrintWriter(client.getOutputStream());
            out.println("ERROR! Bad request:\n\n" + request);
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
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        FileInputStream fis = new FileInputStream(MeshFS.properties.getProperty("repository") + filename);

        while ((br = fis.read(data, 0, data.length)) != -1) {
            dos.write(data, 0, br);
        }

        fis.close();
        dos.close();
    }

    private void receiveFile(String filename, Socket client) throws Exception{
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
                if (requestPart.equals("EOR")) break;
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
                    processRequest(receiveRequest(client), client);
                    client.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
    }
}

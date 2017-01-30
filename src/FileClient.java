/**
 * Created by Levi Muniz on 12/6/16.
 */

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 * The FileClient class handles connecting to
 * other servers in the cluster
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

public final class FileClient {

    /**
     * This method is used to ping a server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port the port of the server to connect to
     * @return true on success, false on failure
     */

    static boolean ping(String serverAddress, int port) {
        try {
            Socket client = new Socket(serverAddress, port);
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out.println("109\n");

            if (input.readLine().trim().equals("201")) {
                client.close();
                return true;
            } else {
                client.close();
                return false;
            }
        } catch (IOException ioe) {
            return false;
        }
    }

    /**
     * This method is used to generate and send a report to a server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port the port of the server to connect to
     * @throws IOException on error connecting
     */

    static void sendReport(String serverAddress, int port) throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("108\n");

            if (input.readLine().trim().equals("201")) {
                out.println(Reporting.generate() + "\n");
            }
            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    /**
     * This method is used to request a report from a server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port the port of the server to connect to
     * @throws IOException on error connecting
     */

    static void receiveReport(String serverAddress, int port) throws IOException {
        String reportPart;
        String reportFull = "";
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        out.println("107\n");

        if (input.readLine().trim().equals("201")) {
            while (true) {
                reportPart = input.readLine();
                if ((reportPart == null) || (reportPart.equals("\n"))) break;
                reportFull = reportFull + reportPart + "\n";
            }
            reportFull = reportFull.trim();
            client.close();

            JSONObject manifest = new JSONObject();
            if (new File(MeshFS.properties.getProperty("repository") + ".manifest.json").exists()){
                manifest = JSONManipulator.getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");
            }
            JSONArray reportArray = Reporting.splitter(reportFull);
            manifest.put(reportArray.get(0),reportArray.get(1));
            JSONManipulator.writeJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json", manifest);
        }
    }

    static void duplicateFile(String serverAddress, int port, String currFile) throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("104|" + currFile + "\n");

            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    static void renameFile(String serverAddress, int port, String jsonObj, String newName) throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("110|" + jsonObj + "|" + newName + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    static void moveFile(String serverAddress, int port, String currFile, String destFile) throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("103|" + currFile + "|" + destFile + "\n");

            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    static void deleteFile(String serverAddress, int port, String currFile) throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try {
            out.println("105|" + currFile + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    static void addFolder(String serverAddress, int port, String directoryPath, String directoryName, String userAccount) throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("106|" + directoryPath + "|" + directoryName + "|" + userAccount + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    static void sendFile(String serverAddress, int port, String filepath) throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("102|" + (new File(filepath)).getName() + "\n");

            if (input.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];

                FileInputStream fis = new FileInputStream(filepath);

                while ((br = fis.read(data, 0, data.length)) != -1) {
                    dos.write(data, 0, br);
                    dos.flush();
                }

                fis.close();
                dos.close();
            }
            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    static void sendFile(String serverAddress, int port, String filepath, String userAccount) throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("102|" + (new File(filepath)).getName() + "|" + userAccount + "\n");

            if (input.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];

                FileInputStream fis = new FileInputStream(filepath);

                while ((br = fis.read(data, 0, data.length)) != -1) {
                    dos.write(data, 0, br);
                    dos.flush();
                }

                fis.close();
                dos.close();
            }
            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }


    @SuppressWarnings( "deprecation" )
    public static void receiveFile(String serverAddress, int port, String fileName) throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(client.getInputStream());

        try {
            out.println("101|" + fileName + "\n");

            if (dis.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];
                FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + fileName);

                while ((br = dis.read(data, 0, data.length)) != -1) {
                    fos.write(data, 0, br);
                    fos.flush();
                }

                fos.close();
                dis.close();
            }
            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    @SuppressWarnings( "deprecation" )
    static void receiveFile(String serverAddress, int port, String fileName, String fileOut) throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(client.getInputStream());

        try {
            out.println("101|" + fileName + "\n");

            if (dis.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];

                FileOutputStream fos = new FileOutputStream(fileOut);

                while ((br = dis.read(data, 0, data.length)) != -1){
                    fos.write(data, 0, br);
                    fos.flush();
                }

                fos.close();
                dis.close();
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

}

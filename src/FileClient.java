import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.Instant;

/**
 * The FileClient class handles connecting to other servers in the cluster
 *
 * @author Levi Muniz
 * @version 1.0.0
 */
final class FileClient {

    /**
     * This method is used to ping a server and report latency.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @return latency in milliseconds, -1 if cannot connect
     */
    static int ping(String serverAddress, int port) {
        try {
            Socket client = new Socket(serverAddress, port);
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out.println(MeshFS.properties.getProperty("uuid") + "|109|" + String.valueOf(Instant.now().toEpochMilli()) + "\n");

            if (input.readLine().trim().equals("201")) {
                return Integer.parseInt(input.readLine());
            } else {
                client.close();
                return -1;
            }
        } catch (IOException ioe) {
            return -1;
        }
    }

    /**
     * This method is used to generate and send a report to a server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @throws IOException on error connecting
     */
    static void sendReport(String serverAddress, int port) throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|108\n");

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
     * @param port          the port of the server to connect to
     * @throws IOException on error connecting or writing to manifest file
     */
    static void receiveReport(String serverAddress, int port) throws IOException {
        String reportPart;
        String reportFull = "";
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        out.println(MeshFS.properties.getProperty("uuid") + "|107\n");

        if (input.readLine().trim().equals("201")) {
            while (true) {
                reportPart = input.readLine();
                if ((reportPart == null) || (reportPart.equals("\n"))) break;
                reportFull = reportFull + reportPart + "\n";
            }
            reportFull = reportFull.trim();
            client.close();

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
    }

    /**
     * This method is used to request a duplication of a file in the catalog.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param currFile      the file to duplicate
     * @throws IOException on error connecting
     */
    static void duplicateFile(String serverAddress, int port, String currFile)
            throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|104|" + currFile + "\n");

            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    /**
     * This method is used to request a rename of a file or folder in the catalog.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param jsonObj       the JSONObject to rename
     * @param newName       the name to set the JSONObject to
     * @throws IOException on error connecting
     */
    static void renameFile(
            String serverAddress, int port, String jsonObj, String newName)
            throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|110|" + jsonObj + "|" + newName + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    /**
     * This method is used to request a move of a file or folder in the catalog.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param currFile      the file that is to be moved
     * @param destFile      the location that the file is to be moved to
     * @throws IOException on error connecting
     */
    static void moveFile(
            String serverAddress, int port, String currFile, String destFile)
            throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|103|" + currFile + "|" + destFile + "\n");

            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    /**
     * This method is used to request file deletion from the catalog.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param currFile      the file to be deleted
     * @throws IOException on error connecting
     */

    static void deleteFile(String serverAddress, int port, String currFile, String uuid) throws IOException {
        deleteFile(serverAddress, port, currFile, uuid, false);
    }

    /**
     * This method is used to request file deletion from the catalog or disk.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param currFile      the file to be deleted
     * @param physical      if true, delete the physical file. false to delete reference to file
     * @throws IOException on error connecting
     */
    static void deleteFile(String serverAddress, int port, String currFile, String uuid, boolean physical)
            throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|105|" + currFile + "|" + physical + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    /**
     * This method is used to request a folder addition in the catalog.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param directoryPath the location to add the folder to
     * @param directoryName the name of folder
     * @param userAccount   the name of the user to add the folder for
     * @throws IOException on error connecting
     */
    static void addFolder(
            String serverAddress,
            int port,
            String directoryPath,
            String directoryName,
            String userAccount)
            throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println(
                    MeshFS.properties.getProperty("uuid") + "|106|" + directoryPath + "|" + directoryName + "|" + userAccount + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }

            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    /**
     * This method is used to request to send a file to the server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param filepath      the location of the file to send
     * @throws IOException on error connecting
     */
    static void sendFile(String serverAddress, int port, String filepath)
            throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        FileInputStream fis = new FileInputStream(filepath);

        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|102|" + (new File(filepath)).getName() + "\n");

            if (input.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];

                while ((br = fis.read(data, 0, data.length)) != -1) {
                    dos.write(data, 0, br);
                    dos.flush();
                }
            }
        } catch (SocketTimeoutException ignored) {
        } finally {
            input.close();
            out.close();
            fis.close();
            dos.close();
            client.close();
        }
    }

    /**
     * This method is used to request to send a file to the server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param filepath      the location of the file to send
     * @param userAccount   the account to add the file for
     * @throws IOException on error connecting
     */
    static void sendFile(
            String serverAddress, int port, String filepath, String userAccount)
            throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        FileInputStream fis = new FileInputStream(filepath);

        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|102|" + (new File(filepath)).getName() + "|" + userAccount + "\n");

            if (input.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];

                while ((br = fis.read(data, 0, data.length)) != -1) {
                    dos.write(data, 0, br);
                    dos.flush();
                }
            }
        } catch (SocketTimeoutException ignored) {
        } finally {
            out.close();
            input.close();
            dos.close();
            fis.close();
            client.close();
        }
    }

    /**
     * This method is used to request to download a file from the server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param fileName      the name of the file that is requested
     * @param fileOut       the file name to write the received file as
     * @throws IOException on error connecting or writing file
     */
    @SuppressWarnings("deprecation")
    static void receiveFile(
            String serverAddress, int port, String fileName, String fileOut)
            throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(client.getInputStream());
        FileOutputStream fos = new FileOutputStream(fileOut);

        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|101|" + fileName + "\n");

            if (dis.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];

                while ((br = dis.read(data, 0, data.length)) != -1) {
                    fos.write(data, 0, br);
                    fos.flush();
                }
            }
        } catch (SocketTimeoutException ignored) {
        } finally {
            out.close();
            dis.close();
            fos.close();
            client.close();
        }
    }

    static void receiveAuthFile(String serverAddress, int port, String fileOut) throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(client.getInputStream());
        FileOutputStream fos = new FileOutputStream(fileOut);

        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|113\n");

            if (dis.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];

                while ((br = dis.read(data, 0, data.length)) != -1) {
                    fos.write(data, 0, br);
                    fos.flush();
                }
            }
        } catch (SocketTimeoutException ignored) {
        } finally {
            out.close();
            dis.close();
            fos.close();
            client.close();
        }
    }

    static String getServerUUID(String serverAddress, int port) throws IOException {
        //TODO: Fix shit code, learn how it works before trying to fuck with this.

        /*
        String response;
        String uuid = "";
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try {
            out.println("112|\n");
            uuid = input.readLine();
            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }
            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
        return uuid;
        */
        return "Shitty code doesn't work";
    }

    static boolean changePassword(
            String serverAddress, int port, String username, String oldPassword, String newPassword)
            throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|111|" + username + "|" + oldPassword + "|" + newPassword + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
                return false;
            }
            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
        return true;
    }

    static void deleteAccount(String serverAddress, int port, String userAccount) throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try {
            out.println(MeshFS.properties.getProperty("uuid") + "|114|" + userAccount + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                System.err.println(response);
            }
            client.close();
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }
}

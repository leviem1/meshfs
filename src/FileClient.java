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

    private FileClient() {}

    /**
     * This method is used to request to download a file from the server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param fileName      the name of the file that is requested and
     *                      name to be used to write as in repo
     * @throws IOException on error connecting or writing file
     */
    static void receiveFile(
            String serverAddress, int port, String fileName)
            throws IOException, MalformedRequestException {
        receiveFile(serverAddress, port, fileName, MeshFS.properties.getProperty("repository") + fileName);
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
            throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(client.getInputStream());
        FileOutputStream fos = new FileOutputStream(fileOut);

        try {
            out.println("101|" +MeshFS.properties.getProperty("uuid") + "|" + fileName + "\n");

            if (!(response = dis.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }
            int br;
            byte[] data = new byte[4096];

            while ((br = dis.read(data, 0, data.length)) != -1) {
                fos.write(data, 0, br);
                fos.flush();
            }
        } catch (SocketTimeoutException ignored) {
        } finally {
            out.close();
            dis.close();
            fos.close();
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
            throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        FileInputStream fis = new FileInputStream(filepath);

        try {
            out.println("102|" + MeshFS.properties.getProperty("uuid") + "|" + (new File(filepath)).getName() + "\n");

            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }
            int br;
            byte[] data = new byte[4096];

            while ((br = fis.read(data, 0, data.length)) != -1) {
                dos.write(data, 0, br);
                dos.flush();
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
            throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        FileInputStream fis = new FileInputStream(filepath);

        try {
            out.println("102|" + MeshFS.properties.getProperty("uuid") + "|" + (new File(filepath)).getName() + "|" + userAccount + "\n");

            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }
            int br;
            byte[] data = new byte[4096];

            while ((br = fis.read(data, 0, data.length)) != -1) {
                dos.write(data, 0, br);
                dos.flush();
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
            throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("103|" + MeshFS.properties.getProperty("uuid") + "|" + currFile + "|" + destFile + "\n");

            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }

        } catch (SocketTimeoutException ignored) {}
        finally {
            input.close();
            out.close();
            client.close();
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
            throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("104|" + MeshFS.properties.getProperty("uuid") + "|" + currFile + "\n");

            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }

        } catch (SocketTimeoutException ignored) {}
        finally {
            input.close();
            out.close();
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

    static void deleteFile(String serverAddress, int port, String currFile) throws IOException, MalformedRequestException {
        deleteFile(serverAddress, port, currFile, false);
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
    static void deleteFile(String serverAddress, int port, String currFile, boolean physical)
            throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try {
            out.println("105|" + MeshFS.properties.getProperty("uuid") + "|" + currFile + "|" + physical + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }
        } catch (SocketTimeoutException ignored) {}
        finally {
            input.close();
            out.close();
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
            throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println(
                    "106|" + MeshFS.properties.getProperty("uuid") + "|" + directoryPath + "|" + directoryName + "|" + userAccount + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }

        } catch (SocketTimeoutException ignored) {}
        finally {
            input.close();
            out.close();
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
    static void receiveReport(String serverAddress, int port) throws IOException, MalformedRequestException {
        String response;
        String reportPart;
        String reportFull = "";
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);


        try {
            out.println("107|" + MeshFS.properties.getProperty("uuid") + "\n");

            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }
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
        } catch (SocketTimeoutException ignored) {}
        finally {
            input.close();
            out.close();
            client.close();
        }
    }

    /**
     * This method is used to generate and send a report to a server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @throws IOException on error connecting
     */
    static void sendReport(String serverAddress, int port) throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("108|" + MeshFS.properties.getProperty("uuid") + "\n");

            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }

            out.println(Reporting.generate() + "\n");
        } catch (SocketTimeoutException ignored) {}
        finally {
            input.close();
            out.close();
            client.close();
        }
    }


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
            out.println("109|" + String.valueOf(Instant.now().toEpochMilli()) + "\n");

            if (!(input.readLine().trim()).equals("201")) {
                client.close();
                return -1;
            }

            client.close();
            return Integer.parseInt(input.readLine());
        } catch (IOException ioe) {
            return -1;
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
            throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("110|" + MeshFS.properties.getProperty("uuid") + "|" + jsonObj + "|" + newName + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }

        } catch (SocketTimeoutException ignored) {}
        finally {
            input.close();
            out.close();
            client.close();
        }
    }

    static void changePassword(
            String serverAddress, int port, String username, String oldPassword, String newPassword)
            throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try {
            out.println("111|" + MeshFS.properties.getProperty("uuid") + "|" + username + "|" + oldPassword + "|" + newPassword + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }
            client.close();
        } catch (SocketTimeoutException ignored) {}
        finally {
            out.close();
            input.close();
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
            out.println("112\n");
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

    @SuppressWarnings("deprecation")
    static void receiveAuthFile(String serverAddress, int port, String fileOut) throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(client.getInputStream());
        FileOutputStream fos = new FileOutputStream(fileOut);

        try {
            out.println("113|" + MeshFS.properties.getProperty("uuid") + "\n");

            if (!(response = dis.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }
            int br;
            byte[] data = new byte[4096];

            while ((br = dis.read(data, 0, data.length)) != -1) {
                fos.write(data, 0, br);
                fos.flush();
            }
        } catch (SocketTimeoutException ignored) {
        } finally {
            out.close();
            dis.close();
            fos.close();
            client.close();
        }
    }

    static String loginAsUser(String serverAddress, int port, String username, String password) throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        String uuid;
        out.println("113|" + username + "|" + password + "\n");

        if (input.readLine().trim().equals("201")) {
            uuid = input.readLine();
            uuid = uuid.trim();
            client.close();
            return uuid;
        }else{
            return "-1";
        }
    }

    static void deleteAccount(String serverAddress, int port, String userAccount) throws IOException, MalformedRequestException {
        String response;
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try {
            out.println("114|" + MeshFS.properties.getProperty("uuid") + "|" + userAccount + "\n");
            if (!(response = input.readLine().trim()).equals("201")) {
                throw new MalformedRequestException(response);
            }
            client.close();
        } catch (SocketTimeoutException ignored) {}
        finally {
            out.close();
            input.close();
            client.close();
        }
    }
}

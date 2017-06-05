import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * The FileClient class handles connecting to other servers in the cluster
 *
 * @author Levi Muniz
 * @version 1.0.0
 */
class FileClient {

    private FileClient() {}

    /**
     * This method is used to request to download a file from the server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param fileName      the name of the file that is requested and
     *                      name to be used to write in repo
     * @throws IOException on error connecting or writing file
     */
    static void receiveFile(
            String serverAddress, int port, String fileName)
            throws IOException, MalformedRequestException, FileTransferException {
        receiveFile(serverAddress, port, fileName, MeshFS.properties.getProperty("repository") + fileName);
    }

    /**
     * This method is used to request to download a file from the server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param fileName      the name of the file that is requested
     * @param fileOut       the file name to write the received file as
     * @throws IOException  on error connecting or writing file
     */
    @SuppressWarnings("deprecation")
    static void receiveFile(
            String serverAddress, int port, String fileName, String fileOut)
            throws IOException, MalformedRequestException, FileTransferException {
        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                DataInputStream dis = new DataInputStream(client.getInputStream());
                FileOutputStream fos = new FileOutputStream(fileOut)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("101|" + MeshFS.properties.getProperty("uuid") + "|" + fileName + "\n");
            String response = dis.readLine().trim();
            String[] responseParts = response.split("\\|");

            if (response.split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            int br;
            byte[] data = new byte[4096];

            while ((br = dis.read(data, 0, data.length)) != -1) {
                fos.write(data, 0, br);
                fos.flush();
            }

            try {
                if (!responseParts[1].equals(FileUtils.getMD5Hash(fileOut))) {
                    throw new FileTransferException();
                }
            } catch (NoSuchAlgorithmException ignored) {}
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

        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                FileInputStream fis = new FileInputStream(filepath)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("102|" + MeshFS.properties.getProperty("uuid") + "|" + (new File(filepath)).getName() + "|" + FileUtils.getMD5Hash(filepath) + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            int br;
            byte[] data = new byte[4096];

            while ((br = fis.read(data, 0, data.length)) != -1) {
                dos.write(data, 0, br);
                dos.flush();
            }
        } catch (NoSuchAlgorithmException ignored) {}
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

        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                FileInputStream fis = new FileInputStream(filepath)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("102|" + MeshFS.properties.getProperty("uuid") + "|" + (new File(filepath)).getName() + "|" + FileUtils.getMD5Hash(filepath) + "|" + userAccount + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            int br;
            byte[] data = new byte[4096];

            while ((br = fis.read(data, 0, data.length)) != -1) {
                dos.write(data, 0, br);
                dos.flush();
            }
        } catch (NoSuchAlgorithmException ignored) {}
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

        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("103|" + MeshFS.properties.getProperty("uuid") + "|" + currFile + "|" + destFile + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }
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

        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            out.println("104|" + MeshFS.properties.getProperty("uuid") + "|" + currFile + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }
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

        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("105|" + MeshFS.properties.getProperty("uuid") + "|" + currFile + "|" + physical + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }
        }
    }

    /**
     * This method is used to request a folder addition in the catalog.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @param directoryPath the location to add the folder to
     * @param directoryName the name of folder
     * @throws IOException on error connecting
     */
    static void addFolder(
            String serverAddress,
            int port,
            String directoryPath,
            String directoryName)
            throws IOException, MalformedRequestException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println(
                    "106|" + MeshFS.properties.getProperty("uuid") + "|" + directoryPath + "|" + directoryName + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }
        }
    }

    /**
     * This method is used to request a report from a server and write to .manifest.json.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @throws IOException on error connecting or writing to manifest file
     */
    @SuppressWarnings("unchecked")
    static void receiveReport(String serverAddress, int port) throws IOException, MalformedRequestException {
        JSONObject manifest = new JSONObject();
        if (new File(MeshFS.properties.getProperty("repository") + ".manifest.json").exists()) {
            manifest =
                    JSONUtils.getJSONObject(
                            MeshFS.properties.getProperty("repository") + ".manifest.json");
        }
        JSONArray reportArray = receiveReportAsJSON(serverAddress, port);

        manifest.put(reportArray.get(0), reportArray.get(1));
        JSONUtils.writeJSONObject(
                MeshFS.properties.getProperty("repository") + ".manifest.json", manifest);
    }

    /**
     * This method is used to request a report from a server as JSONArray.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port          the port of the server to connect to
     * @throws IOException on error connecting or writing to manifest file
     */
    static JSONArray receiveReportAsJSON(String serverAddress, int port) throws IOException, MalformedRequestException {
        String response;
        String reportPart;
        StringBuilder reportFull = new StringBuilder();

        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("107|" + MeshFS.properties.getProperty("uuid") + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            while (true) {
                reportPart = input.readLine();
                if ((reportPart == null) || (reportPart.equals("\n"))) break;
                reportFull.append(reportPart).append("\n");
            }
        }

        reportFull = new StringBuilder(reportFull.toString().trim());

        return Reporting.splitter(reportFull.toString());
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

        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("108|" + MeshFS.properties.getProperty("uuid") + "\n");
            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            out.println(Reporting.generate() + "\n");
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
        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            long initial = Instant.now().toEpochMilli();

            out.println("109\n");

            if (!(input.readLine().trim()).equals("201")) {
                return -1;
            }

            return (int) (Instant.now().toEpochMilli() - initial);
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

        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("110|" + MeshFS.properties.getProperty("uuid") + "|" + jsonObj + "|" + newName + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }
        }
    }

    static void changePassword(
            String serverAddress, int port, String username, String oldPassword, String newPassword)
            throws IOException, MalformedRequestException, IncorrectCredentialException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("111|" + MeshFS.properties.getProperty("uuid") + "|" + username + "|" + oldPassword + "|" + newPassword + "\n");



            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            } else if (response.equals("203")) {
                throw new IncorrectCredentialException();
            }
        }
    }

    static boolean doesFileExist(String serverAddress, int port, String fileName) throws MalformedRequestException, IOException {
        String response;
        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("112|" + MeshFS.properties.getProperty("uuid") + "|" + fileName + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            return Boolean.parseBoolean(input.readLine().trim());
        }
    }

    static String loginAsUser(String serverAddress, int port, String username, String password) throws IOException, MalformedRequestException, IncorrectCredentialException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
            out.println("113|" + username + "|" + password + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            } else if (response.split(";")[0].equals("203")) {
                throw new IncorrectCredentialException();
            }

            return input.readLine().trim();
        }
    }

    static void deleteAccount(String serverAddress, int port, String userAccount) throws IOException, MalformedRequestException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()))
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("114|" + MeshFS.properties.getProperty("uuid") + "|" + userAccount + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }
        }
    }

    static JSONObject getUserFiles(String serverAddress, int port, String userAccount, String uuid) throws MalformedRequestException, IOException {
        String response;
        JSONObject jsonObject = new JSONObject();

        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)

        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("115|" + uuid + "|" + userAccount + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            jsonObject = (JSONObject) new JSONParser().parse(input.readLine().trim());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    static String getUserGroups(String serverAddress, int port, String userAccount, String uuid) throws MalformedRequestException, IOException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("116|" + uuid + "|" + userAccount + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            String groups = input.readLine().trim();

            return groups.replace("[", "").replace("]", "");
        }
    }

    static String getGroups(String serverAddress, int port, String uuid) throws MalformedRequestException, IOException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("117|" + uuid + "\n");
            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            String groups = input.readLine().trim();

            return groups.replace("[", "").replace("]", "");
        }

    }

    static void updateUserGroupMembership(String serverAddress, int port, String userAccount, String newGroups, String uuid) throws MalformedRequestException, IOException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("118|" + uuid + "|" + userAccount + "|" + newGroups + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }
        }
    }

    static String getUserType(String serverAddress, int port, String userAccount, String uuid) throws MalformedRequestException, IOException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {

            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("119|" + uuid + "|" + userAccount + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            return input.readLine().trim();
        }
    }

    static void editPermissions(String serverAddress, int port, String itemLocation, String groups, String uuid) throws MalformedRequestException, IOException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("120|" + uuid + "|" + itemLocation + "|" + groups + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }
        }
    }

    static List<String> getNodeIntendedFiles(String serverAddress, int port, String macAddr) throws MalformedRequestException, IOException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);

            out.println("121|" + MeshFS.properties.getProperty("uuid") + "|" + macAddr + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }

            return Arrays.asList(response.substring(1, response.length() - 1).split(", "));
        }
    }

    static void blacklistUser(String serverAddress, int port, String itemLocation, String username, String uuid) throws MalformedRequestException, IOException {
        String response;

        try (
                Socket client = new Socket(serverAddress, port);
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {
            client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
            out.println("122|" + uuid + "|" + itemLocation + "|" + username + "\n");

            if ((response = input.readLine().trim()).split(";")[0].equals("202")) {
                throw new MalformedRequestException(response);
            }
        }
    }

}

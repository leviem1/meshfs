import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
            System.out.println("Unicast socket " + threads + " initialized...");
        }

        for (int socket = 0; socket < sockets.size(); socket++) {
            (sockets.get(socket)).start();
            System.out.println("Unicast socket " + socket + " started!");
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
                } catch (InterruptedException e) {
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
        StringBuilder requestFull = new StringBuilder();
        try {
            DataInputStream input = new DataInputStream(client.getInputStream());

            while (((requestPart = input.readLine()) != null) && (requestFull.length() < 2048)) {
                if (requestPart.equals("")) break;
                requestFull.append(requestPart);
            }

            return requestFull.toString();
        } catch (IOException ioe) {
            return requestFull.toString();
        }
    }

    private void processRequest(String request, Socket out) {

        if (request != null) {
            try {
                String[] requestParts = request.trim().split("\\|");

                /*for(String x : requestParts){
                    System.out.println(x);
                }*/


                //format requests in form of #, uuid, parameters
                if (!(requestParts[0].equals("109") || requestParts[0].equals("113")) && (!requestParts[1].equals(MeshFS.properties.getProperty("uuid")))) {
                    return;
                }
                switch (requestParts[0]) {
                    case "101": //101:Get file
                        sendFile(requestParts[2], out);

                        break;
                    case "102": //102:Post file
                        if (requestParts.length == 5) {
                            receiveFile(requestParts[2], requestParts[3], requestParts[4], out);
                        } else {
                            receiveFile(requestParts[2], requestParts[3], out);
                        }

                        break;
                    case "103": //103:Move file (virtual only)
                        moveFile(requestParts[2], requestParts[3], out);

                        break;
                    case "104": //104:Copy file (virtual only)
                        duplicateFile(requestParts[2], out);

                        break;
                    case "105": //105:Delete file (virtual and physical)
                        deleteFile(requestParts[2], Boolean.parseBoolean(requestParts[3]), out);

                        break;
                    case "106": //106:Make directory (virtual only)
                        createDirectory(requestParts[2], requestParts[3], out);

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
                    case "110": //110:Rename File
                        renameFile(requestParts[2], requestParts[3], out);

                        break;
                    case "111": //111:Change Password
                        changePassword(requestParts[2], requestParts[3], requestParts[4], out);

                        break;
                    case "112": //122: Check file's existence
                        doesFileExist(requestParts[2], out);

                        break;
                    case "113": //113:Send Auth Info
                        sendAuthInfo(requestParts[1], requestParts[2], out);

                        break;
                    case "114": //114:Delete Account
                        deleteAccount(requestParts[2], out);

                        break;
                    case "115": //115:Get User Files
                        getUserFiles(requestParts[2], out);

                        break;
                    case "116": //116:Get User Groups
                        getUserGroups(requestParts[2], out);

                        break;
                    case "117": //117:Get All Groups
                        getGroups(out);

                        break;
                    case "118": //118:Update user Groups
                        setUserGroup(requestParts[2], requestParts[3], out);

                        break;
                    case "119": //119: Get User Type
                        getUserType(requestParts[2], out);

                        break;
                    case "120": //120: Set item permissions
                        setItemPermissions(requestParts[2], requestParts[3], out);

                        break;
                    case "121": //121: Get node intended files
                        getNodeIntendedFiles(requestParts[2], out);

                        break;
                    case "122": //122: Blacklist a user
                        blacklistUser(requestParts[2], requestParts[3], out);

                        break;

                    default:
                        badRequest(out, request, "Invalid Request");
                        break;
                }
            } catch (Exception e) {
                badRequest(out, request, e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void ping(Socket client) throws IOException {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            out.println("201");
        }
    }

    private void sendReport(Socket client) throws IOException {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            out.println("201");
            out.println(Reporting.generate() + "\n");
        }
    }

    @SuppressWarnings("unchecked")
    private synchronized void receiveReport(Socket client) throws IOException {
        String reportPart;
        StringBuilder reportFull = new StringBuilder();

        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true)
        ) {

            out.println("201");

            while (true) {
                reportPart = input.readLine();
                if ((reportPart == null) || (reportPart.equals("\n"))) break;
                reportFull.append(reportPart).append("\n");
            }
            reportFull = new StringBuilder(reportFull.toString().trim());

            JSONObject manifest = new JSONObject();
            if (new File(MeshFS.properties.getProperty("repository") + ".manifest.json").exists()) {
                manifest =
                        JSONUtils.getJSONObject(
                                MeshFS.properties.getProperty("repository") + ".manifest.json");
            }
            JSONArray reportArray = Reporting.splitter(reportFull.toString());

            manifest.put(reportArray.get(0), reportArray.get(1));
            JSONUtils.writeJSONObject(
                    MeshFS.properties.getProperty("repository") + ".manifest.json", manifest);
        }
    }

    private void moveFile(String currentPath, String newPath, Socket client)
            throws IOException {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            out.println("201");
            JSONUtils.moveItem(currentPath, newPath); //TODO: Someone please explain how this works...
        }
    }

    private void deleteFile(String filePath, boolean physical, Socket client) throws IOException {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            out.println("201");

            if (physical) {
                FileUtils.removeFile(MeshFS.properties.getProperty("repository") + filePath);
            } else {
                try {
                    JSONUtils.deleteItem(filePath, true); //TODO: and this....
                } catch (MalformedRequestException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void duplicateFile(String currentPath, Socket client) throws IOException {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            out.println("201");
            JSONUtils.duplicateItem(currentPath);
        }
    }

    private void sendFile(String filename, Socket client) throws IOException {
        try (
                DataOutputStream dos = new DataOutputStream(client.getOutputStream());
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                FileInputStream fis = new FileInputStream(MeshFS.properties.getProperty("repository") + filename)
        ) {
            int br;
            byte[] data = new byte[4096];

            out.println("201|" + FileUtils.getMD5Hash(MeshFS.properties.getProperty("repository") + filename));

            while ((br = fis.read(data, 0, data.length)) != -1) {
                dos.write(data, 0, br);
                dos.flush();
            }
        } catch (NoSuchAlgorithmException ignored) {}
    }

    private void receiveFile(String filename, String md5, Socket client) throws IOException, FileTransferException {
        try (
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                DataInputStream dis = new DataInputStream(client.getInputStream());
                FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + filename)
        ) {
            int br;
            byte[] data = new byte[4096];

            out.println("201");

            while ((br = dis.read(data, 0, data.length)) != -1) {
                fos.write(data, 0, br);
                fos.flush();
            }

            if (!md5.equals(FileUtils.getMD5Hash(MeshFS.properties.getProperty("repository") + filename))) {
                throw new FileTransferException();
            }
        } catch (NoSuchAlgorithmException ignored) {}
    }

    private void receiveFile(String filename, String md5, String userAccount, Socket client)
            throws IOException, FileTransferException {
        try (
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                DataInputStream dis = new DataInputStream(client.getInputStream())
        ) {
            int br;
            byte[] data = new byte[4096];
            FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + filename);


            out.println("201");

            if ((new File(filename)).exists()) {
                String fileNew;
                int count = 1;
                while (true) {
                    fileNew = filename.substring(0, filename.lastIndexOf(".")) + " (" + count + ")" + filename.substring(filename.lastIndexOf("."));

                    if (!new File(fileNew).exists()) {
                        break;
                    }

                    count++;

                }
                filename = fileNew;
                fos = new FileOutputStream(filename);
            }

        final String filenameTrue = filename;

        JSONUtils.addTempFile("root/Users/" + userAccount, filenameTrue + " (uploading)", userAccount);

        while ((br = dis.read(data, 0, data.length)) != -1) {
            fos.write(data, 0, br);
            fos.flush();
        }

        fos.close();

            if (!md5.equals(FileUtils.getMD5Hash(MeshFS.properties.getProperty("repository") + filename))) {
                throw new FileTransferException();
            }

        Thread distributor = new Thread(() -> {
            try {
                DISTAL.distributor(filenameTrue, "root/Users/" + userAccount, userAccount);
            } catch (IOException | MalformedRequestException e) {
                e.printStackTrace();
            }
        });
        distributor.start();
        } catch (NoSuchAlgorithmException ignored) {}

    }

    private void createDirectory(
            String directoryPath, String directoryName, Socket client)
            throws IOException {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            out.println("201");
            JSONUtils.createNewFolder(directoryPath, directoryName);
        }
    }

    private void renameFile(String jsonPath, String newName, Socket client)
            throws IOException {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            out.println("201");
            JSONUtils.renameItem(jsonPath, newName); //TODO: ...and here...
        }
    }

    @SuppressWarnings("unchecked")
    private void changePassword(String username, String oldPassword, String newPassword, Socket client) throws IOException {
        ArrayList<UserAccounts> accounts;
        UserAccounts accountToRemove;
        String accountType;
        ArrayList<String> accountGroups;
        File auth = new File(MeshFS.properties.getProperty("repository") + ".auth");

        try (
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(auth))
        ) {
            accounts = (ArrayList) ois.readObject();

            if (auth.exists() && !username.equals("guest")) {
                for (UserAccounts userAccount : accounts) {
                    if (userAccount.getUsername().equals(username)) {
                        if (userAccount.getPassword().equals(Crypt.generateEncryptedPass(username, oldPassword))) {
                            accountToRemove = userAccount;
                            accountType = userAccount.getAccountType();
                            accountGroups = userAccount.getGroups();
                            accounts.remove(accountToRemove);
                            accounts.add(new UserAccounts(username, Crypt.generateEncryptedPass(username, newPassword), accountType, accountGroups));
                            Crypt.writeAuthFile(accounts);
                            out.println("201");
                        } else {
                            out.println("203");
                        }
                    }
                }
            }
        } catch (ClassNotFoundException ignored) {}
    }

    private void doesFileExist(String fileName, Socket client) throws IOException {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            out.println("201");
            out.println(new File(MeshFS.properties.getProperty("repository") + fileName).exists() + "\n");
            out.close();
        }
    }

    @SuppressWarnings("unchecked")
    private void sendAuthInfo(String username, String password, Socket client) throws IOException {
        File auth = new File(MeshFS.properties.getProperty("repository") + ".auth");
        ArrayList<UserAccounts> accounts;

        try (
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(auth))
        ) {
            accounts = (ArrayList) ois.readObject();
            if (auth.exists()) {
                for (UserAccounts userAccount : accounts) {
                    String un = userAccount.getUsername();
                    String pw = userAccount.getPassword();

                    if (username.toLowerCase().trim().equals(un) && password.trim().equals(pw)) {
                        out.println("201");
                        out.println(MeshFS.properties.getProperty("uuid") + "\n");
                        return;
                    }
                }
                out.println("203");
            } else {
                out.println("203");
            }
        } catch (ClassNotFoundException ignored) {}
    }

    @SuppressWarnings("unchecked")
    private void deleteAccount(String username, Socket client) throws IOException {
        ArrayList<UserAccounts> userAccounts;
        ArrayList<UserAccounts> toRemove = new ArrayList<>();

        try (
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MeshFS.properties.getProperty("repository") + ".auth"));
                FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + ".auth");
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            userAccounts = (ArrayList) ois.readObject();

            for (UserAccounts userAccount : userAccounts) {
                if (!(username.equals("guest") || username.equals("admin"))) {
                    if (userAccount.getUsername().equals(username)) {
                        toRemove.add(userAccount);
                    }
                }
            }

            userAccounts.removeAll(toRemove);
            oos.writeObject(userAccounts);
            oos.flush();
            out.println("201");
        } catch (ClassNotFoundException ignored) {}
    }

    @SuppressWarnings("unchecked")
    private void getUserFiles(String userAccount, Socket client) throws IOException {
        UserAccounts user = null;
        ArrayList<UserAccounts> accounts;
        JSONObject userObj;

        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
        for (UserAccounts account : accounts) {
            if (account.getUsername().equals(userAccount)) {
                user = account;
            }
        }

        if (user != null) {
            userObj = JSONUtils.buildUserCatalog(user);
        } else {
            userObj = new JSONObject();
        }

            out.println("201");
            out.println(userObj.toString() + "\n");
        } catch (ClassNotFoundException ignored) {}
    }

    @SuppressWarnings("unchecked")
    private void getUserGroups(String userAccount, Socket client) throws IOException {
        ArrayList<UserAccounts> accounts;
        ArrayList<String> groups = new ArrayList<>();

        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
            for (UserAccounts account : accounts) {
                if (account.getUsername().equals(userAccount)) {
                    groups.addAll(account.getGroups());
                }
            }
            out.println("201");
            out.println(groups.toString() + "\n");
        } catch (ClassNotFoundException ignored) {}
    }

    @SuppressWarnings("unchecked")
    private void getGroups(Socket client) throws IOException {
        ArrayList<UserAccounts> accounts;
        ArrayList<String> groups = new ArrayList<>();

        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
            for (UserAccounts account : accounts) {
                groups.addAll(account.getGroups());
            }

            out.println("201");
            out.println(groups.toString() + "\n");
        } catch (ClassNotFoundException ignored) {}
    }

    @SuppressWarnings("unchecked")
    private void setUserGroup(String userAccount, String userGroups, Socket client) throws IOException {
        String un = null;
        String pw = null;
        String at = null;
        ArrayList<UserAccounts> accounts;

        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();
            List<String> newGroupsList = Arrays.asList(userGroups.split(", "));

            for (UserAccounts account : accounts) {
                if (account.getUsername().equals(userAccount.toLowerCase())) {
                    un = account.getUsername();
                    pw = account.getPassword();
                    at = account.getAccountType();
                    accounts.remove(account);
                    break;
                }
            }

            accounts.add(new UserAccounts(un, pw, at, new ArrayList<>(newGroupsList)));
            Crypt.writeAuthFile(accounts);

            out.println("201");

        } catch (ClassNotFoundException ignored) {}
    }

    @SuppressWarnings("unchecked")
    private void getUserType(String userAccount, Socket client) throws IOException {
        String userType = null;
        ArrayList<UserAccounts> accounts;

        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            accounts = (ArrayList<UserAccounts>) new ObjectInputStream(new FileInputStream(new File(MeshFS.properties.getProperty("repository") + ".auth"))).readObject();

            for (UserAccounts account : accounts) {
                if (account.getUsername().equals(userAccount)) {
                    userType = account.getAccountType();
                }
            }

            out.println("201");
            out.println(userType + "\n");
        } catch (ClassNotFoundException ignored) {}
    }

    private void setItemPermissions(String itemLocation, String groups, Socket client) throws IOException {
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        ArrayList<String> newGroups = new ArrayList<>();


        for (String group : groups.replace("[", "").replace("]", "").split(",")) {
            newGroups.add(group.trim());
        }

        JSONUtils.editPermissions(itemLocation, newGroups, new ArrayList<>());
        out.println("201");

        out.close();
        dos.close();
    }

    private void getNodeIntendedFiles(String macAddress, Socket client) throws IOException {
        try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
            out.println("201");
            out.println(FileRestore.getFilesOnComputerFromCatalog(macAddress) + "\n");
        }
    }

    private void blacklistUser(String itemLocation, String username, Socket client) throws IOException {
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        JSONUtils.blacklistUsers(itemLocation, new ArrayList<>(Collections.singletonList(username)), true);

        out.println("201");

        out.close();
        dos.close();
    }


    private void badRequest(Socket client, String request, String message) {
        try {
            if (client.isClosed()) return;
            try (PrintWriter out = new PrintWriter(client.getOutputStream(), true)) {
                out.println("202;" + request + ";" + message);
            }
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
        System.out.println("Unicast socket closed");
    }
}

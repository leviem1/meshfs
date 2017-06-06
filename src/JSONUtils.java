import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Aaron Duran on 5/12/2017.
 */
class JSONUtils {

    private JSONUtils() {
    }

    /**
     * This method returns the JSONObject of a file.
     *
     * @param filePath is the file path of the file that is to be read
     * @return JSONObject is from the read from file
     */
    static synchronized JSONObject getJSONObject(String filePath) {
        JSONObject jsonObject = null;
        JSONParser reader = new JSONParser();

        try {
            Object obj = reader.parse(new FileReader(filePath));
            jsonObject = (JSONObject) obj;
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    /**
     * This method returns the JSONObject associated with the designated item.
     *
     * @param jsonObject   the JSONObject that is being read
     * @param itemLocation the virtual path within the JSONObject
     * @return the JSONObject associated with the designated item
     */
    static JSONObject getItemContents(JSONObject jsonObject, String itemLocation){
        itemLocation = catalogStringFixer(itemLocation);
        String[] folders = itemLocation.split("/");
        JSONObject folderToRead = jsonObject;
        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        return folderToRead;
    }

    /**
     * This method returns the ID associated with the designated google file from a JSONObject.
     *
     * @param masterJSON    the JSONObject of google drive
     * @param itemLocation  the virtual path within the google drive JSONObject
     * @return              the google drive file id
     */
    static String getGoogleFileID(JSONObject masterJSON, String itemLocation){
        String[] folders = itemLocation.split("/");
        JSONObject folderToRead = masterJSON;
        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        return folderToRead.get("ID").toString();
    }

    /**
     * This method is returns a LinkedHashMap of the contents of a folder. The contents are returned
     * in the format of "itemName", "itemType".
     *
     * @param jsonObject     the JSONObject that is being read
     * @param user           the UserAccount object of the user, if null, the folder is read as if the user was an admin
     * @return a map of the folder's contents
     */
    @SuppressWarnings("unchecked")
    static LinkedHashMap<String, String> getMapOfFolderContents(JSONObject jsonObject, UserAccount user) {
        LinkedHashMap<String, String> contents = new LinkedHashMap<>();
        for (Object key : jsonObject.keySet()) {
            String keyStr = key.toString();
            try {
                if ((user == null)
                        || (user.getUsername().equals("admin")
                        || ((JSONArray) (((JSONObject) jsonObject.get(keyStr)).get("groups"))).contains("all")
                        || (!Collections.disjoint(((JSONArray) (((JSONObject) jsonObject.get(keyStr)).get("groups"))), user.getGroups()))
                        && !((JSONArray) (((JSONObject) jsonObject.get(keyStr)).get("blacklist"))).contains(user.getUsername()))) {
                    contents.put(keyStr, (((JSONObject) jsonObject.get(keyStr)).get("type")).toString());
                }
            } catch (Exception ignored) {
            }
        }

        return contents;
    }

    /**
     * This method adds a reference to the catalog when a file is uploaded.
     *
     * @param stripes       the List of Lists that shows what computer each stripe was sent to
     * @param fileName      the name the the file will have in the JTree
     * @param JSONFilePath  the virtual path of the folder in the catalog that the file will be put into
     */
    @SuppressWarnings("unchecked")
    static void addFileToCatalog(List<List<String>> stripes, String itemDestinationLocation, String fileName, String JSONFilePath, String alphanumericName, String username, long fileSize) throws IOException {
        itemDestinationLocation = catalogStringFixer(itemDestinationLocation);
        JSONObject catalog = getJSONObject(JSONFilePath);


        JSONObject objChild = new JSONObject();
        JSONArray ipArray = new JSONArray();

        for (int stripe = 0; stripe < stripes.size(); stripe++) {
            ipArray.addAll(stripes.get(stripe));
            if (stripe == 0) {

                objChild.put("whole", ipArray.clone());
            } else {

                objChild.put("stripe_" + String.valueOf(stripe - 1), ipArray.clone());
            }
            ipArray.clear();
        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        String creationDate = df.format(new Date());

        JSONArray userArray = new JSONArray();
        userArray.add(username);

        objChild.put("type", "file");

        objChild.put("alphanumericName", alphanumericName);

        objChild.put("fileSize", fileSize);

        objChild.put("creationDate", creationDate);

        objChild.put("fileName", fileName);


        JSONObject destination = getItemContents(catalog, itemDestinationLocation);
        JSONArray destinationUsers = (JSONArray) (destination.get("groups"));
        JSONArray destinationAdmins = (JSONArray) (destination.get("admins"));
        objChild = changePermissions(objChild, destinationUsers, destinationAdmins, true);

        catalog = putItemInFolder(catalog, itemDestinationLocation, fileName, objChild);

        writeJSONObject(JSONFilePath, catalog);
    }

    /**
     * This method changes the permissions of a folder or file in the catalog.
     *
     * @param itemLocation  the virtual path of the item in the catalog that is to be changed
     * @param fileUsers     the List of user groups that can view the file
     * @param fileAdmins    the List of user groups that can edit the file  (not implemented yet)
     */
    static void editPermissions(String itemLocation, List<String> fileUsers, List<String> fileAdmins) throws IOException {
        itemLocation = catalogStringFixer(itemLocation);
        JSONObject catalog = getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        String[] folders = itemLocation.split("/");
        JSONObject item = catalog;
        for (String folder : folders) {
            item = (JSONObject) item.get(folder);
        }

        JSONArray groups = new JSONArray();
        groups.addAll(fileUsers);

        JSONArray admins = new JSONArray();
        groups.addAll(fileAdmins);

        item = changePermissions(item, groups, admins, false);

        writeJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json", catalog);
    }

    /**
     * This method adds user groups to a blacklist that prevents them from seeing a folder or file in the catalog.
     *
     * @param itemLocation  the virtual path of the item in the catalog that is to be changed
     * @param userNames     the List of user groups that should be added or removed to the viewing blacklist for the item
     * @param add           if true the users are added to the blacklist, if false they are removed from the blacklist
     */
    static void blacklistUsers(String itemLocation, List<String> userNames, boolean add) {
        itemLocation = catalogStringFixer(itemLocation);
        JSONObject catalog = getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        String[] folders = itemLocation.split("/");
        JSONObject item = catalog;
        for (String folder : folders) {
            item = (JSONObject) item.get(folder);
        }

        JSONArray blacklist = (JSONArray) item.get("blacklist");
        if (add) {
            blacklist.addAll(userNames);
        } else {
            blacklist.removeAll(userNames);
        }
        try {
            writeJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json", catalog);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a new reference of the given item within the catalog.
     *
     * @param itemLocation the source virtual path of the item within the catalog
     */
    static void duplicateItem(String itemLocation) throws IOException {
        JSONObject catalog = getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        System.out.println(catalog);
        writeJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json", copyFile(catalog, itemLocation, itemLocation.substring(0, itemLocation.lastIndexOf("/")), null, true));
        System.out.println(getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json"));
    }

    /**
     * This method removes the given item from the catalog the catalog.
     *
     * @param itemLocation  the source virtual path of the item within the catalog
     * @param smart         if true, if no other references of the file exists, the file is removed from all nodes. If false, other references
     */
    static void deleteItem(String itemLocation, boolean smart) throws IOException, MalformedRequestException {
        JSONObject catalog = getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        deleteItem(catalog, itemLocation, smart);
    }

    /**
     * This method copies the given item to the designated path within the JSONObject.
     *
     * @param itemLocation        the source virtual path within the JSONObject
     * @param destinationLocation the destination virtual path within the JSONObject (should not have
     *                            the name of the item in this path)
     * @return updated JSONObject that item was read from
     */
    static void moveItem(String itemLocation, String destinationLocation) {
        moveFile(itemLocation, destinationLocation, itemLocation.substring(itemLocation.lastIndexOf("/") + 1), true);
    }

    /**
     * This method copies the given item to the designated path within the JSONObject.
     *
     * @param itemLocation the source virtual path within the JSONObject
     * @param newName      what the item should be called
     * @return updated JSONObject that item was read from
     */
    static void renameItem(String itemLocation, String newName) {
        System.out.println(itemLocation);
        System.out.println(newName);
        moveFile(itemLocation, itemLocation.substring(0, itemLocation.lastIndexOf("/")), newName, false);
    }

    /**
     * This method writes out a json file from a JSONObject.
     *
     * @param filePath where the json file is to be written to
     * @param obj      the JSONObject the is to be written the json file
     * @throws IOException if the file cannot be written
     */
    static synchronized void writeJSONObject(String filePath, JSONObject obj) throws IOException {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(obj.toJSONString());
        }
    }

    static void createNewFolder(String parentFolderLocation, String folderName) throws IOException {
        parentFolderLocation = catalogStringFixer(parentFolderLocation);
        JSONObject catalog = getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");

        JSONObject folderCreator = new JSONObject();
        folderCreator.put("type", "directory");

        JSONObject destination = getItemContents(catalog, parentFolderLocation);
        JSONArray destinationUsers = (JSONArray) (destination.get("groups"));
        JSONArray destinationAdmins = (JSONArray) (destination.get("admins"));
        folderCreator = changePermissions(folderCreator, destinationUsers, destinationAdmins, true);


        writeJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json", putItemInFolder(catalog, parentFolderLocation, folderName, folderCreator));
    }

    /**
     * This method adds a new temporary file, to the JSONObject.
     *
     * @param itemLocation the source virtual path within the JSONObject
     * @param fileName     the name of the file, the way it appears in the file browser
     * @param username     the account name that uploaded the file
     */
    @SuppressWarnings("unchecked")
    static void addTempFile(String itemLocation, String fileName, String username) throws IOException {
        itemLocation = catalogStringFixer(itemLocation);
        JSONObject jsonFile = getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        JSONObject objChild = new JSONObject();

        objChild.put("type", "tempFile");

        JSONArray users = new JSONArray();
        users.add(username);

        objChild.put("groups", users);
        objChild.put("admins", users);
        objChild.put("blacklist", new JSONArray());
        jsonFile = putItemInFolder(jsonFile, itemLocation, fileName, objChild);

        writeJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json", jsonFile);
    }

    /**
     * This method produces a LinkedHashMap where each element is comprised of the computers' MAC
     * address and its available storage.
     *
     * @param manifestFile The JSONObject that contains the information from the manifest file
     * @return             LinkedHashMap of online computers' MACAddress and available storage, ordered by descending available storage
     */
    @SuppressWarnings("unchecked")
    static LinkedHashMap<String, Long> createStorageMap(JSONObject manifestFile) {
        LinkedHashMap<String, Long> storageMap = new LinkedHashMap();
        for (Object MACAddress : manifestFile.keySet()) {
            if (FileClient.ping(((((JSONObject) manifestFile.get(MACAddress)).get("IP")).toString()), Integer.parseInt(MeshFS.properties.getProperty("portNumber"))) > -1) {
                storageMap.put(MACAddress.toString(), Long.valueOf((((JSONObject) manifestFile.get(MACAddress)).get("FreeSpace")).toString()));
            }
        }
        return sortMapByValue(storageMap, false);
    }

    static LinkedHashMap<String, Long> sortMapByValue(LinkedHashMap<String, Long> storageMap, boolean ascending) {

        LinkedHashMap<String, Long> sortedMap = new LinkedHashMap();

        //put something in the map to compare against
        sortedMap.put("temp", -1L);

        for (String key : storageMap.keySet()) {
            Long storageAmount = storageMap.get(key);
            boolean isBroken = false;

            for (String sortedKey : sortedMap.keySet()) {
                if ((!ascending && storageAmount >= sortedMap.get(sortedKey)) || (ascending && storageAmount <= sortedMap.get(sortedKey))) {
                    //reorder the map when a storage value is larger than one that is already in the map
                    LinkedHashMap<String, Long> reorderStorageMap =
                            (LinkedHashMap<String, Long>) sortedMap.clone();
                    sortedMap.clear();
                    for (String reorderKey : reorderStorageMap.keySet()) {
                        if (reorderKey.equals(sortedKey)) {
                            sortedMap.put(key, storageAmount);
                        }
                        sortedMap.put(reorderKey, reorderStorageMap.get(reorderKey));
                    }
                    isBroken = true;
                    break;
                }
            }

            if (!isBroken) {
                sortedMap.put(key, storageMap.get(key));
            }
        }
        return sortedMap;
    }

    /**
     * This method pulls a file from the file system.
     *
     * @param itemLocation  the source virtual path within the catalog
     * @param path          where the download file is to be saved to
     * @param outFile       the name that the downloaded file is to be saved as
     * @throws IOException  if client cannot connect to server
     * @throws MalformedRequestException if file does not exist
     * @throws PullRequestException if the full file cannot be combined from all the files in the file system
     * @throws FileTransferException if md5 of sending file did not match received file md5
     */
    static void pullFile(String itemLocation, String path, String outFile, boolean download) throws IOException, MalformedRequestException, PullRequestException, FileTransferException {
        itemLocation = catalogStringFixer(itemLocation);
        String outFileDir = path.substring(0, path.lastIndexOf(File.separator));
        File tempManifest = File.createTempFile(".manifest", ".json");
        tempManifest.deleteOnExit();
        FileClient.receiveFile(MeshFS.properties.getProperty("masterIP"), Integer.parseInt(MeshFS.properties.getProperty("portNumber")), ".manifest.json", tempManifest.getAbsolutePath());

        JSONObject compInfoFile = getJSONObject(tempManifest.getAbsolutePath());
        List<String> stripeNames = new ArrayList<>();
        List<Thread> childThreads = new ArrayList<>();
        boolean wholeNecessary = true;

        JSONObject fileInfo = getItemContents(getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog"), itemLocation);
        String fileName = fileInfo.get("alphanumericName").toString();

        for (Object stripe : fileInfo.keySet()) {
            if (stripe.toString().contains("stripe")) {
                wholeNecessary = false;
                String fileNameWNum =
                        fileName + "_s" + stripe.toString().substring(stripe.toString().lastIndexOf("_") + 1);
                JSONArray compsWithStripe = (JSONArray) fileInfo.get(stripe);
                boolean cantContinue = true;

                for (Object MACAddress : compsWithStripe) {
                    if (compInfoFile.containsKey(MACAddress)) {
                        if (((JSONArray) (((JSONObject) compInfoFile.get(MACAddress)).get("RepoContents"))).contains(fileNameWNum)) {
                            String IPAddress = (((JSONObject) compInfoFile.get(MACAddress)).get("IP")).toString();

                            if (FileClient.doesFileExist(IPAddress, Integer.parseInt(MeshFS.properties.getProperty("portNumber")), fileNameWNum)) {
                                if (download) {
                                    Thread child =
                                            new Thread(
                                                    () -> {
                                                        try {
                                                            FileClient.receiveFile(IPAddress, Integer.parseInt(MeshFS.properties.getProperty("portNumber")), fileNameWNum, outFileDir + File.separator + "." + fileNameWNum);
                                                        } catch (IOException | MalformedRequestException | FileTransferException ioe) {
                                                            ioe.printStackTrace();
                                                        }
                                                    });
                                    childThreads.add(child);
                                    child.start();
                                }
                                stripeNames.add(outFileDir + File.separator + "." + fileNameWNum);

                                cantContinue = false;
                                break;
                            }
                        }
                    }
                }
                if (cantContinue) {
                    wholeNecessary = true;
                    break;
                }
            }
        }
        if (wholeNecessary) {
            String fileNameW = fileName + "_w";
            JSONArray compsWithWhole = (JSONArray) fileInfo.get("whole");
            boolean cantContinue = true;

            for (Object MACAddress : compsWithWhole) {
                if (compInfoFile.containsKey(MACAddress)) {
                    if (((JSONArray) (((JSONObject) compInfoFile.get(MACAddress)).get("RepoContents"))).contains(fileNameW)) {
                        String IPAddress = ((JSONObject) compInfoFile.get(MACAddress)).get("IP").toString();
                        if (FileClient.doesFileExist(IPAddress, Integer.parseInt(MeshFS.properties.getProperty("portNumber")), fileNameW)) {
                            if (download) {
                                try {
                                    FileClient.receiveFile(IPAddress, Integer.parseInt(MeshFS.properties.getProperty("portNumber")), fileNameW, outFileDir + File.separator + "." + outFile);
                                } catch (MalformedRequestException e) {
                                    e.printStackTrace();
                                }
                                new File(outFileDir + File.separator + "." + outFile).renameTo(new File(outFileDir + File.separator + outFile));
                            }
                            cantContinue = false;
                            break;
                        }
                    }
                }
            }
            if (cantContinue) {
                throw new PullRequestException();
            }
        } else {
            for (Thread child : childThreads) {
                if (child.isAlive()) {
                    try {
                        child.join();
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
            if (download) {
                FileUtils.combineStripes(numberSorter(stripeNames), outFileDir + File.separator + "." + outFile);

                String outFileNew = outFile;

                if (!(new File(outFileDir + File.separator + outFile)).exists()) {
                    String fileNew;
                    int count = 1;
                    while (true) {
                        fileNew = outFile.substring(0, outFile.lastIndexOf(".")) + " (" + count + ")" + outFile.substring(outFile.lastIndexOf("."));

                        if (!new File(fileNew).exists()) {
                            break;
                        }

                        count++;

                    }
                    outFileNew = fileNew;
                }

                new File(outFileDir + File.separator + "." + outFile).renameTo(new File(outFileDir + File.separator + outFileNew));
            }
        }
        if (download) {
            for (String filePath : stripeNames) {
                Files.deleteIfExists(Paths.get(filePath));
            }
        }
    }

    /**
     * This method produces a partial catalog that only contains the folders and files that the given user can see.
     *
     * @param user  The UserAccount Object of the user
     * @return      the JSONObject of the User's Catalog
     */
    static JSONObject buildUserCatalog(UserAccount user) {
        JSONObject catalog = getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        JSONObject users = (JSONObject) ((JSONObject) catalog.get("root")).get("Users");
        if (!users.containsKey(user.getUsername())) {
            JSONObject folder = new JSONObject();
            JSONArray groups = new JSONArray();

            groups.add(user.getUsername());
            folder.put("type", "directory");
            folder.put("groups", groups);
            folder.put("blacklist", new JSONArray());
            folder.put("admins", groups);
            users.put(user.getUsername(), folder);

            try {
                writeJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json", catalog);
            } catch (IOException e) {
                e.printStackTrace();
            }

            catalog = getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        }
        return catalogBuilder(catalog, user);
    }

    /**
     * This method creates the Object for the JTree based on the user's catalog
     *
     * @param userCatalog   The UserCatalog for the user
     * @param adminFormat   if true, the JTree will be formatted as "root/Users/username". if false, the JTree will be formatted as "root/username"
     * @return              the TreeNode Object for the JTree
     */
    static DefaultMutableTreeNode JTreeBuilder(JSONObject userCatalog, boolean adminFormat) {
        DefaultMutableTreeNode root;
        if (adminFormat) {
            root = JTreeBuilderRecursive((JSONObject) userCatalog.get("root"), new DefaultMutableTreeNode("root"));

        } else {
            root = JTreeBuilderRecursive((JSONObject) ((JSONObject) userCatalog.get("root")).get("Users"), new DefaultMutableTreeNode("root"));
            root.add(JTreeBuilderRecursive((JSONObject) ((JSONObject) userCatalog.get("root")).get("Shared"), new DefaultMutableTreeNode("Shared")));
        }
        return root;
    }

    /**
     * This method fixes a formatting error in a virtual file path that results from creating the filepath from the JTree.
     *
     * @param itemLocationString    the virtual file path string that is to be fixed
     * @return                      the fixed virtual file path string
     */
    static String catalogStringFixer(String itemLocationString) {
        if ((itemLocationString.contains("/")) && ((StringUtils.countMatches(itemLocationString, "/") == 1) || (!(itemLocationString.substring(0, itemLocationString.indexOf("/", 5))).equals("root/Users") && (!(itemLocationString.substring(0, itemLocationString.indexOf("/", 5))).equals("root/Shared"))))) {
            itemLocationString = itemLocationString.substring(0, itemLocationString.indexOf("/") + 1) + "Users" + itemLocationString.substring(itemLocationString.indexOf("/"));
        }

        return itemLocationString;
    }

    /**
     * This method finds the total size and last modification date of a folder.
     *
     * @param catalog           the JSONObject of the catalog
     * @param folderLocation    the virtual location of the specified folder
     * @return                  a pair where the key is the human readable size of the file, and the value is the last modification time of the folder
     */
    static Pair<String, String> folderProperties(JSONObject catalog, String folderLocation) {
        folderLocation = catalogStringFixer(folderLocation);
        String[] folders = folderLocation.split("/");
        JSONObject item = catalog;
        for (String folder : folders) {
            System.out.println("folder: " + folder);
            item = (JSONObject) item.get(folder);
        }

        Pair<Long, Long> properties = folderPropertiesRecursive(item);

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        return new Pair<>(humanReadableByteCount(properties.getKey()), df.format(new Date(properties.getValue())));
    }

    private static Pair<Long, Long> folderPropertiesRecursive(JSONObject folder) {
        Long folderSize = 0L;
        Long epochDate = 0L;
        Pair<Long, Long> properties = new Pair<>(folderSize, epochDate);

        LinkedHashMap<String, String> folderContents = getMapOfFolderContents(folder, null);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        for (String item : folderContents.keySet()) {
            if (folderContents.get(item).equals("directory")) {
                Pair<Long, Long> newProperties = folderPropertiesRecursive((JSONObject) folder.get(item));
                if (newProperties.getValue() > epochDate) {
                    epochDate = newProperties.getValue();
                }
                properties = new Pair<>(properties.getKey() + newProperties.getKey(), epochDate);
            } else if (folderContents.get(item).equals("file")) {

                String date = (String) ((JSONObject) folder.get(item)).get("creationDate");
                try {
                    long epochDateNew = df.parse(date).getTime();
                    if (epochDateNew > epochDate) {
                        epochDate = epochDateNew;
                    }
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }

                properties = new Pair<>(properties.getKey() + (long) ((JSONObject) folder.get(item)).get("fileSize"), epochDate);


            }
        }
        return properties;
    }

    private static DefaultMutableTreeNode JTreeBuilderRecursive(JSONObject jsonObject, DefaultMutableTreeNode branch) {
        Map<String, String> folderContents = getMapOfFolderContents(jsonObject, null);
        if (folderContents.keySet().isEmpty()) {
            try{
               if(folderContents.get("generated").equals("false")){
                   DefaultMutableTreeNode leaf = new DefaultMutableTreeNode("(loading...)");
                   branch.add(leaf);
               }
            }catch(NullPointerException npe){
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode("(no files)");
                branch.add(leaf);
            }
        } else {
            for (String name : folderContents.keySet()) {
                DefaultMutableTreeNode leaf = new DefaultMutableTreeNode(name);
                leaf.setAllowsChildren(folderContents.get(name).equals("directory"));
                if (leaf.getAllowsChildren()) {
                    leaf = JTreeBuilderRecursive((JSONObject) jsonObject.get(name), leaf);
                }
                branch.add(leaf);
            }
        }
        return branch;
    }

    private static JSONObject catalogBuilder(JSONObject jsonObject, UserAccount user) {
        LinkedHashMap<String, String> items = getMapOfFolderContents(jsonObject, user);
        JSONObject catalog = new JSONObject();
        for (String item : items.keySet()) {
            if (items.get(item).equals("directory")) {
                catalog.put(item, catalogBuilder((JSONObject) jsonObject.get(item), user));
            } else {
                catalog.put(item, jsonObject.get(item));
            }
        }
        if (jsonObject.containsKey("groups")) {
            catalog.put("type", "directory");
            catalog.put("groups", jsonObject.get("groups"));
            catalog.put("admins", jsonObject.get("admins"));
            catalog.put("blacklist", jsonObject.get("blacklist"));
        }

        return catalog;
    }

    @SuppressWarnings("unchecked")
    private static JSONObject putItemInFolder(JSONObject catalog, String destinationLocation, String fileName, JSONObject itemContents) {
        String[] folders = destinationLocation.split("/");
        JSONObject folderToRead = catalog;
        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        folderToRead.put(fileName, itemContents);
        return catalog;
    }

    private static JSONObject copyFile(JSONObject catalog, String itemLocation, String destinationLocation, String newName, boolean updatePermissions) {
        itemLocation = catalogStringFixer(itemLocation);
        destinationLocation = catalogStringFixer(destinationLocation);
        JSONObject itemContents = getItemContents(catalog, itemLocation);

        if (updatePermissions) {
            JSONObject destination = getItemContents(catalog, destinationLocation);
            JSONArray destinationUsers = (JSONArray) (destination.get("groups"));
            JSONArray destinationAdmins = (JSONArray) (destination.get("admins"));
            itemContents = changePermissions(itemContents, destinationUsers, destinationAdmins, true);
        }

        if (newName == null) {
            DateFormat df = new SimpleDateFormat("h:mm:ss a");
            Date dateObj = new Date();
            catalog = putItemInFolder(catalog, destinationLocation, itemLocation.substring(itemLocation.lastIndexOf("/") + 1) + " (" + df.format(dateObj) + ")", itemContents);
        } else {
            catalog = putItemInFolder(catalog, destinationLocation, newName, itemContents);
        }

        return catalog;
    }

    private static void deleteItem(JSONObject catalog, String itemLocation, boolean smart) throws IOException, MalformedRequestException {
        itemLocation = catalogStringFixer(itemLocation);
        JSONObject folderToRead = catalog;
        String item;
        if (itemLocation.contains("/")) {
            item = itemLocation.substring(itemLocation.lastIndexOf("/") + 1);
            String[] folders = itemLocation.substring(0, itemLocation.lastIndexOf("/")).split("/");

            for (String folder : folders) {
                folderToRead = (JSONObject) folderToRead.get(folder);
            }
        } else {
            item = itemLocation;
        }

        if (smart) {
            List<JSONObject> filesToRemove = smartDelete(folderToRead);
            folderToRead.remove(item);

            JSONObject manifest = getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");
            String catalogString = catalog.toString();
            for (JSONObject file : filesToRemove) {
                if (!catalogString.contains(file.get("alphanumericName").toString())) {
                    // actually delete file
                    for (Object infoKey : file.keySet()) {
                        if (infoKey.toString().contains("_")) {
                            for (Object MACAddress : (JSONArray) file.get(infoKey)) {
                                FileClient.deleteFile(((JSONObject) manifest.get(MACAddress)).get("IP").toString(), Integer.valueOf(MeshFS.properties.getProperty("portNumber")), file.get("alphanumericName").toString() + infoKey.toString().substring(infoKey.toString().indexOf("_")), true);
                            }
                        }
                    }
                }
            }
        } else {
            folderToRead.remove(item);
        }
        writeJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json", catalog);
    }

    private static void moveFile(String itemLocation, String destinationLocation, String newName, boolean updatePermissions) {
        JSONObject catalog = getJSONObject(MeshFS.properties.getProperty("repository") + ".catalog.json");
        catalog = copyFile(catalog, itemLocation, destinationLocation, newName, updatePermissions);
        try {
            deleteItem(catalog, itemLocation, false);
        } catch (IOException | MalformedRequestException e) {
            e.printStackTrace();
        }
    }

    private static List<JSONObject> smartDelete(JSONObject itemToRemove) {
        List<JSONObject> removedFiles = new ArrayList<>();

        if (itemToRemove.get("type").toString().equals("directory")) {
            LinkedHashMap<String, String> children = getMapOfFolderContents(itemToRemove, null);
            for (String childName : children.keySet()) {
                removedFiles.addAll(smartDelete((JSONObject) itemToRemove.get(childName)));
            }
        } else if (itemToRemove.get("type").toString().equals("file")) {
            removedFiles.add(itemToRemove);
        }
        return removedFiles;
    }

    /**
     * This method turns a number of bytes into the a format that humans can read, uses base 1024
     *
     * @param bytes The number of bytes
     * @return      The string of how humans read file size, rounded to ______ decimal places
     */
    static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("KMGTPE").charAt(exp - 1) + ("i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private static JSONObject changePermissions(JSONObject jsonObject, JSONArray newUserArray, JSONArray newAdminArray, boolean removeBlacklist) {
        jsonObject.put("groups", newUserArray);
        jsonObject.put("admins", newAdminArray);
        if (removeBlacklist) {
            jsonObject.put("blacklist", new JSONArray());
        }

        LinkedHashMap<String, String> children = getMapOfFolderContents(jsonObject, null);
        for (String child : children.keySet()) {
            jsonObject.replace(child, changePermissions((JSONObject) jsonObject.get(child), newUserArray, newAdminArray, removeBlacklist));
        }
        return jsonObject;
    }

    private static List<String> numberSorter(List<String> unsorted) {
        String name = unsorted.get(0).substring(0, unsorted.get(0).lastIndexOf("s") + 1);
        int[] numbers = new int[unsorted.size()];
        int index = 0;
        for (String nameWithNum : unsorted) {
            numbers[index] = Integer.parseInt(nameWithNum.substring(nameWithNum.lastIndexOf("s") + 1));
            index++;
        }
        Arrays.sort(numbers);
        unsorted.clear();
        for (int number : numbers) {
            unsorted.add(name + String.valueOf(number));
        }
        return unsorted;
    }


}
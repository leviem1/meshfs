import javafx.util.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The JSONManipulator class has the ability to read, write, and change JSON files and objects.
 *
 * @author Aaron Duran
 * @version 1.0.0
 */
class JSONManipulator {

    private JSONManipulator() {}

    /**
     * This method is returns the JSONObject stored in a file.
     *
     * @param filePath the file path of the file that is to be read
     * @return JSONObject read from file
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

    static String catalogStringFixer(String itemLocationString){
        if ((! (itemLocationString.substring(0,itemLocationString.indexOf("/",1))).equals("root/Users")) && (! (itemLocationString.substring(0,itemLocationString.indexOf("/",1))).equals("root/Shared"))){            itemLocationString = itemLocationString.substring(0,itemLocationString.indexOf("/") + 1) + "Users/" + itemLocationString.substring(itemLocationString.indexOf("/",1));
        }
        return itemLocationString;
    }

    /**
     * This method is returns a LinkedHashMap of the contents of a folder. The contents are returned
     * in the format of "itemName", "itemType".
     *
     * @param jsonObject     the JSONObject that is being read
     * @param folderLocation the virtual path within the catalog file
     * @param userAccount    the file path of the file that is to be read
     * @return a map of the folder's contents
     */

    @SuppressWarnings("unchecked")
    static LinkedHashMap<String, String> getMapOfFolderContents(
            JSONObject jsonObject, String folderLocation, String userAccount) {
        if (folderLocation.equals(userAccount)) {
            folderLocation += "/";
        }
        String[] Tree = folderLocation.split("/");
        JSONObject folderToRead = jsonObject;
        if (!folderLocation.equals(null)) {
            JSONObject folderToReadNew;
            for (String folder : Tree) {
                folderToReadNew = (JSONObject) folderToRead.get(folder);
                if (folderToReadNew == null) {
                    JSONObject folderCreator = new JSONObject();

                    folderCreator.put("type", "directory");

                    folderToRead.put(folder, folderCreator);
                    folderToRead = (JSONObject) folderToRead.get(folder);
                } else {
                    folderToRead = folderToReadNew;
                }
            }
        }
        LinkedHashMap<String, String> contents = new LinkedHashMap<>();
        for (Object key : folderToRead.keySet()) {
            String keyStr = key.toString();
            if (userAccount.equals(null) || ((((JSONObject) folderToRead.get(keyStr)).get("owner")).toString().equals(userAccount)) || (((JSONArray)(((JSONObject) folderToRead.get(keyStr)).get("users"))).contains(userAccount))){
                String type = (((JSONObject) folderToRead.get(keyStr)).get("type")).toString();
                contents.put(keyStr, type);
            }
        }

        return contents;
    }

    static LinkedHashMap<String, String> getMapOfFolderContents(
            JSONObject jsonObject, String folderLocation) {
        return getMapOfFolderContents(jsonObject, folderLocation, null);
    }

    /**
     * This method deletes the designated item within the JSONObject.
     *
     * @param jsonObject   the JSONObject that is being read
     * @param itemLocation the virtual path within the JSONObject
     * @return JSONObject in which the item was removed
     */
    static JSONObject removeItem(JSONObject jsonObject, String itemLocation) {
        JSONObject folderToRead = jsonObject;
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

        List<String> filesToRemove = smartRemove(folderToRead);
        folderToRead.remove(item);
        JSONObject manifest = getJSONObject(MeshFS.properties.getProperty("repository") + ".manifest.json");
        for (String fileName : filesToRemove){
            ((JSONObject) (((JSONObject) jsonObject.get("fileInfo")).get(fileName))).replace(
                    "references",
                    Integer.valueOf(((JSONObject) (((JSONObject) jsonObject.get("fileInfo")).get(fileName))).get("references").toString()) - 1);
            if (Integer.valueOf(((JSONObject) (((JSONObject) jsonObject.get("fileInfo")).get(fileName))).get("references").toString()) < 1){
                // actually delete file
                JSONObject fileInfo = getItemContents(jsonObject, "fileInfo/" + fileName);
                for (Object infoKey : fileInfo.keySet()){
                    if (infoKey.toString().contains("_")){
                        for (Object MACAddress : (JSONArray) fileInfo.get(infoKey)){
                            try{
                                FileClient.deleteFile(((JSONObject) manifest.get(MACAddress)).get("IP").toString(),Integer.valueOf(MeshFS.properties.getProperty("portNumber")),infoKey.toString(),true);
                            }catch (IOException ignored){}                        }
                    }
                }
                ((JSONObject) jsonObject.get("fileInfo")).remove(fileName);
            }
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
    static JSONObject getItemContents(JSONObject jsonObject, String itemLocation) {
        String[] folders = itemLocation.split("/");
        JSONObject folderToRead = jsonObject;
        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        return folderToRead;
    }

    /**
     * This method copies the given item to the designated path within the JSONObject.
     *
     * @param jsonObject          the JSONObject that is being read
     * @param itemLocation        the source virtual path within the JSONObject
     * @param destinationLocation the destination virtual path within the JSONObject (should not have
     *                            the name of the item in this path)
     * @param showDate            this determines if the time of the copy should be reflected in the file name
     * @param newName             what the new file should be called.
     * @return updated JSONObject that the item was read from
     */
    static JSONObject copyFile(
            JSONObject jsonObject,
            String itemLocation,
            String destinationLocation,
            boolean showDate,
            String newName) {
        return copyFile(jsonObject, itemLocation, destinationLocation, showDate, newName, true);
    }
    private static JSONObject copyFile(
            JSONObject jsonObject,
            String itemLocation,
            String destinationLocation,
            boolean showDate,
            String newName,
            boolean smartCopy) {
        JSONObject itemContents = getItemContents(jsonObject, itemLocation);

        if (smartCopy){
            JSONObject destinationInfo = getItemContents(jsonObject,destinationLocation);
            String owner = destinationInfo.get("owner").toString();
            JSONArray users;

            try{
                users = (JSONArray) destinationInfo.get("users");
            }
            catch (Exception e){
                users = null;
            }
            Pair<JSONObject, List<String>> info = smartCopy(itemContents, owner, users);
            itemContents = info.getKey();
            for (String fileName : info.getValue()){
                ((JSONObject) (((JSONObject) jsonObject.get("fileInfo")).get(fileName))).replace(
                        "references",
                        Integer.valueOf((((JSONObject) (((JSONObject) jsonObject.get("fileInfo")).get(fileName))).get("references").toString())) + 1);
            }
        }

        if (showDate) {
            DateFormat df = new SimpleDateFormat("h:mm:ss a");
            Date dateObj = new Date();
            jsonObject =
                    putItemInFolder(
                            jsonObject,
                            destinationLocation,
                            newName + " (" + df.format(dateObj) + ")",
                            itemContents);
        } else {
            jsonObject = putItemInFolder(jsonObject, destinationLocation, newName, itemContents);
        }

        return jsonObject;
    }

    /**
     * This method adds a directory within the designated folder within the JSONObject.
     *
     * @param jsonObject    the JSONObject that is being read
     * @param jsonPath      the virtual path within the JSONObject
     * @param directoryName the name of the new folder
     * @param userAccount   the account to add the folder for
     * @return updated JSONObject that the folder was read from
     */

    @SuppressWarnings("unchecked")
    static JSONObject addFolder(
            JSONObject jsonObject, String jsonPath, String directoryName, String userAccount) {
        JSONObject type = new JSONObject();

        type.put("type", "directory");

        type.put("owner", userAccount);
        jsonObject = putItemInFolder(jsonObject, jsonPath, directoryName, type);
        return jsonObject;
    }

    /**
     * This method renames the given item within the JSONObject.
     *
     * @param jsonObject   the JSONObject that is being read
     * @param itemLocation the virtual path within the JSONObject
     * @param newName      the new name for the file
     * @return updated JSONObject that the file was read from
     */
    static JSONObject renameFile(JSONObject jsonObject, String itemLocation, String newName) {
        return moveFile(
                jsonObject,
                itemLocation,
                itemLocation.substring(0, itemLocation.lastIndexOf("/")),
                newName);
    }

    /**
     * This method moves the given item to the designated path within the JSONObject.
     *
     * @param jsonObject          the JSONObject that is being read
     * @param itemLocation        the source virtual path within the JSONObject
     * @param destinationLocation the destination virtual path within the JSONObject (should not have
     *                            the name of the item in this path)
     * @return Updated JSONObject that the file was read from
     */
    static JSONObject moveFile(
            JSONObject jsonObject, String itemLocation, String destinationLocation) {
        String fileName = itemLocation.substring(itemLocation.lastIndexOf("/") + 1);
        return (moveFile(jsonObject, itemLocation, destinationLocation, fileName));
    }

    /**
     * This method adds a new file, with all of its properties, to the JSONObject.
     *
     * @param stripes          the list of computers the store each stripe and whole copy
     * @param itemLocation     the source virtual path within the JSONObject
     * @param fileName         the name of the file, the way it appears in the file browser
     * @param JSONFilePath     the file path of the catalog file
     * @param alphanumericName the name of the file, as it is stored on the node computers
     * @param userAccount      the account name person that uploaded the file
     * @param fileSize         the size of the file, in bytes
     */

    @SuppressWarnings("unchecked")
    static void addToIndex(
            List<List<String>> stripes,
            String itemLocation,
            String fileName,
            String JSONFilePath,
            String alphanumericName,
            String userAccount,
            long fileSize) {

        JSONObject jsonFile = JSONManipulator.getJSONObject(JSONFilePath);

        jsonFile.replace("currentName", alphanumericName);

        JSONObject objChildInfo = new JSONObject();
        JSONObject objChild = new JSONObject();
        JSONArray ipArray = new JSONArray();

        for (int stripe = 0; stripe < stripes.size(); stripe++) {
            ipArray.addAll(stripes.get(stripe));
            if (stripe == 0) {

                objChildInfo.put("whole", ipArray.clone());
            } else {

                objChildInfo.put("stripe" + "_" + String.valueOf(stripe - 1), ipArray.clone());
            }
            ipArray.clear();
        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        String creationDate = df.format(new Date());

        objChild.put("owner", userAccount);

        objChild.put("type", "file");

        objChild.put("fileName", alphanumericName);

        objChildInfo.put("fileSize", humanReadableByteCount(fileSize));

        objChildInfo.put("creationDate", creationDate);

        objChildInfo.put("references", 1);

        jsonFile = putItemInFolder(jsonFile, itemLocation, fileName, objChild);
        jsonFile = putItemInFolder(jsonFile, "fileInfo", alphanumericName, objChildInfo);

        try {
            writeJSONObject(JSONFilePath, jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method adds a new file, with all of its properties, to the JSONObject.
     *
     * @param itemLocation the source virtual path within the JSONObject
     * @param fileName     the name of the file, the way it appears in the file browser
     * @param JSONFilePath the file path of the catalog file
     * @param userAccount  the account name person that uploaded the file
     */

    @SuppressWarnings("unchecked")
    static void addToIndex(
            String itemLocation, String fileName, String JSONFilePath, String userAccount) {
        JSONObject jsonFile = JSONManipulator.getJSONObject(JSONFilePath);
        JSONObject objChild = new JSONObject();

        objChild.put("type", "tempFile");

        objChild.put("owner", userAccount);
        jsonFile = JSONManipulator.putItemInFolder(jsonFile, itemLocation, fileName, objChild);
        try {
            writeJSONObject(JSONFilePath, jsonFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * This method adds a new file, with all of its properties, to the JSONObject.
     *
     * @param itemLocation  the source virtual path within the JSONObject
     * @param path          where the download file is to be saved to
     * @param outFile       the name that the download file is to be saved as
     * @param serverAddress the address of the the master server
     * @param port          the port that MeshFS uses
     * @param catalog       the catalog file to read from
     * @return true on success, false on failure
     * @throws IOException if a socket cannot be initialized
     */
    static boolean pullFile(
            String itemLocation,
            String path,
            String outFile,
            String serverAddress,
            int port,
            File catalog)
            throws IOException {
        String outFileDir = path.substring(0, path.lastIndexOf(File.separator));
        File tempManifest = File.createTempFile(".manifest", ".json");
        tempManifest.deleteOnExit();
        FileClient.receiveFile(
                serverAddress, port, ".manifest.json", tempManifest.getAbsolutePath());
        JSONObject compInfoFile = getJSONObject(tempManifest.getAbsolutePath());
        String[] folders = itemLocation.split("/");
        JSONObject jsonObject = JSONManipulator.getJSONObject(catalog.getAbsolutePath());
        List<String> stripeNames = new ArrayList<>();
        List<Thread> childThreads = new ArrayList<>();
        boolean wholeNecessary = true;

        JSONObject itemToRead = getItemContents(jsonObject, itemLocation);

        String fileName = itemToRead.get("fileName").toString();
        JSONObject fileInfo = getItemContents(jsonObject, "fileInfo/" + fileName);

        for (Object stripe : fileInfo.keySet()) {
            if (stripe.toString().contains("stripe")) {
                wholeNecessary = false;
                String fileNameWNum =
                        fileName + "_s" + stripe.toString().substring(stripe.toString().lastIndexOf("_") + 1);
                JSONArray compsWithStripe = (JSONArray) fileInfo.get(stripe);
                boolean cantContinue = true;

                for (Object MACAddress : compsWithStripe) {
                    if (compInfoFile.containsKey(MACAddress)) {
                        if (((JSONArray) (((JSONObject) compInfoFile.get(MACAddress)).get("RepoContents")))
                                .contains(fileNameWNum)) {
                            String IPAddress = (((JSONObject) compInfoFile.get(MACAddress)).get("IP")).toString();

                            Thread child =
                                    new Thread(
                                            () -> {
                                                try {
                                                    FileClient.receiveFile(
                                                            IPAddress,
                                                            port,
                                                            fileNameWNum,
                                                            outFileDir + File.separator + "." + fileNameWNum);
                                                } catch (IOException ioe) {
                                                    ioe.printStackTrace();
                                                }
                                            });

                            childThreads.add(child);
                            child.start();
                            stripeNames.add(outFileDir + File.separator + "." + fileNameWNum);

                            cantContinue = false;
                            break;
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
                    if (((JSONArray) (((JSONObject) compInfoFile.get(MACAddress)).get("RepoContents")))
                            .contains(fileNameW)) {
                        String IPAddress = ((JSONObject) compInfoFile.get(MACAddress)).get("IP").toString();
                        FileClient.receiveFile(
                                IPAddress, port, fileNameW, outFileDir + File.separator + "." + outFile);
                        new File(outFileDir + File.separator + "." + outFile)
                                .renameTo(new File(outFileDir + File.separator + outFile));
                        cantContinue = false;
                        break;
                    }
                }
            }
            if (cantContinue) {
                return false;
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
            FileUtils.combineStripes(
                    numberSorter(stripeNames), outFileDir + File.separator + "." + outFile);
            new File(outFileDir + File.separator + "." + outFile)
                    .renameTo(new File(outFileDir + File.separator + outFile));
        }
        for (String filePath : stripeNames) {
            Files.deleteIfExists(Paths.get(filePath));
        }
        return true;
    }

    /**
     * This method produces a LinkedHashMap where each element is comprised of the computers' MAC
     * address and its available storage, with all of its properties, to the JSONObject.
     *
     * @param manifestFile The JSONObject that contains the information from the manifest file
     * @return map of computers' properties
     */

    @SuppressWarnings("unchecked")
    static LinkedHashMap<String, Long> createStorageMap(JSONObject manifestFile) {
        LinkedHashMap<String, Long> storageMap = new LinkedHashMap();
        for (Object MACAddress : manifestFile.keySet()) {
            storageMap.put(
                    MACAddress.toString(),
                    Long.valueOf((((JSONObject) manifestFile.get(MACAddress)).get("FreeSpace")).toString()));
        }
        return storageMap;
    }

    @SuppressWarnings("unchecked")
    static JSONObject shareFolder(JSONObject jsonObject, String itemLocation, List<String> userNames){
        jsonObject = moveFile(jsonObject, itemLocation, "root/Shared");
        String[] folders = itemLocation.split("/");
        JSONObject folderToRead = jsonObject;
        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        folderToRead.put("users", folderToRead.get("owner"));
        return addUsers(jsonObject, itemLocation, userNames);
    }

    @SuppressWarnings("unchecked")
    static JSONObject addUsers(JSONObject jsonObject, String itemLocation, List<String> userNames){
        String[] folders = itemLocation.split("/");
        JSONObject folderToRead = jsonObject;
        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        for (String username : userNames) {
            ((JSONArray) folderToRead.get("users")).add(username);
        }
        if (folderToRead.get("type").toString().equals("directory")){
            LinkedHashMap<String, String> children = getMapOfFolderContents(jsonObject,itemLocation);
            for (String childName : children.keySet()) {
                jsonObject = addUsers(jsonObject,itemLocation + "/" + childName, userNames);
            }
        }
        return jsonObject;
    }

    static JSONObject removeUsers(JSONObject jsonObject, String itemLocation, List<String> userNames){
        String[] folders = itemLocation.split("/");
        JSONObject folderToRead = jsonObject;
        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        for (String username : userNames) {
            ((JSONArray) folderToRead.get("users")).remove(username);
        }
        if (folderToRead.get("type").toString().equals("directory")){
            LinkedHashMap<String, String> children = getMapOfFolderContents(jsonObject,itemLocation);
            for (String childName : children.keySet()) {
                jsonObject = removeUsers(jsonObject,itemLocation + "/" + childName, userNames);
            }
        }
        return jsonObject;
    }

    static JSONObject deleteUsers(JSONObject jsonObject, List<String> users){
        for (String user : users) {
            ((JSONObject) ((JSONObject) jsonObject.get("root")).get("User")).remove(user);
        }
        return removeUsers(jsonObject, "root/Shared", users);
    }


    /**
     * This method copies the given item to the designated path within the JSONObject.
     *
     * @param jsonObject              the JSONObject that is being read
     * @param itemDestinationLocation the source virtual path within the JSONObject (the name of the
     *                                item should not be in this path)
     * @param fileName                the destination virtual path within the JSONObject
     * @param itemContents            this is the JSONObject the contains all the information about the file
     * @return JSONObject that item is written to
     */

    @SuppressWarnings("unchecked")
    private static JSONObject putItemInFolder(
            JSONObject jsonObject,
            String itemDestinationLocation,
            String fileName,
            JSONObject itemContents) {
        String[] folders = itemDestinationLocation.split("/");
        JSONObject folderToRead = jsonObject;
        JSONObject folderToReadNew;
        for (String folder : folders) {
            folderToReadNew = (JSONObject) folderToRead.get(folder);
            if (folderToReadNew == null) {
                JSONObject folderCreator = new JSONObject();

                folderCreator.put("type", "directory");

                folderToRead.put(folder, folderCreator);
                folderToRead = (JSONObject) folderToRead.get(folder);
            } else {
                folderToRead = folderToReadNew;
            }
        }

        folderToRead.put(fileName, itemContents);
        return jsonObject;
    }

    /**
     * This method copies the given item to the designated path within the JSONObject.
     *
     * @param jsonObject          the JSONObject that is being read.
     * @param itemLocation        the source virtual path within the JSONObject
     * @param destinationLocation the destination virtual path within the JSONObject (should not have
     *                            the name of the item in this path)
     * @param newName             what the moved file should be called
     * @return updated JSONObject that item was read from
     */
    private static JSONObject moveFile(
            JSONObject jsonObject, String itemLocation, String destinationLocation, String newName) {
        jsonObject = copyFile(jsonObject, itemLocation, destinationLocation, false, newName, false);
        jsonObject = removeItem(jsonObject, itemLocation);
        return jsonObject;
    }

    private static String humanReadableByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("KMGTPE").charAt(exp - 1) + ("i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
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

    private static Pair<JSONObject, List<String>> smartCopy(JSONObject itemToChange, String owner, JSONArray users){
        itemToChange.replace("owner", owner);
        if (users == null){
            itemToChange.remove("users");
        }
        else{
            itemToChange.put("users", users);
        }

        List<String> copiedFiles = new ArrayList<>();

        if (itemToChange.get("type").toString().equals("directory")){
            LinkedHashMap<String, String> children = getMapOfFolderContents(itemToChange,null);
            for (String childName : children.keySet()) {
                Pair<JSONObject, List<String>> copyInfo = smartCopy((JSONObject) itemToChange.get(childName), owner, users);
                itemToChange.replace(childName, copyInfo.getKey());
                copiedFiles.addAll(copyInfo.getValue());
            }
        }
        else if (itemToChange.get("type").toString().equals("file")){
            copiedFiles.add(itemToChange.get("fileName").toString());
        }
        return new Pair<>(itemToChange, copiedFiles);
    }

    private static List<String> smartRemove(JSONObject itemToRemove){
        List<String> removedFiles = new ArrayList<>();

        if (itemToRemove.get("type").toString().equals("directory")){
            LinkedHashMap<String, String> children = getMapOfFolderContents(itemToRemove,null);
            for (String childName : children.keySet()) {
                removedFiles.addAll(smartRemove((JSONObject) itemToRemove.get(childName)));
            }
        }
        else if (itemToRemove.get("type").toString().equals("file")){
            removedFiles.add(itemToRemove.get("fileName").toString());
        }
        return removedFiles;
    }

}

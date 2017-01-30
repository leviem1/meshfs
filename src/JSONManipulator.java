import org.json.simple.parser.JSONParser;
import org.json.simple.*;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Levi Muniz on 10/19/16.
 */

/**
 * The JSONManipulator class has the
 * ability to read, write, and change
 * JSON files and objects.
 *
 * @author Aaron Duran
 * @version 1.0.0
 */

class JSONManipulator {

    /**
     * This method is returns the JSONObject stored in a file.
     *
     * @param filePath    the file path of the file that is to be read.
     */

    static JSONObject getJSONObject(String filePath) {
        File file = new File(filePath);
        JSONObject jsonObject = null;
        while (true) {
            try {
                FileOutputStream fos = new FileOutputStream(file);
                FileLock fl = fos.getChannel().lock();

                FileReader fr = new FileReader(file);
                JSONParser reader = new JSONParser();
                Object obj = reader.parse(fr);
                jsonObject = (JSONObject) obj;

                fl.release();
                fos.close();
                break;
            } catch (ParseException | IOException e) {
                e.printStackTrace();
                break;
            } catch (OverlappingFileLockException e) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    break;
                }
            }
        }
        return jsonObject;
    }

    /**
     * This method is returns a LinkedHashMap of the contents of a folder.
     * The contents are returned in the format of "itemName", "itemType"
     *
     * @param jsonObject        the JSONObject that is being read.
     * @param folderLocation    the virtual path within the catalog file.
     * @param userAccount       the file path of the file that is to be read.
     */

    static LinkedHashMap<String,String> getMapOfFolderContents(JSONObject jsonObject, String folderLocation, String userAccount){
        if (folderLocation.equals(userAccount)){
            folderLocation += "/";
        }
        String[] Tree = folderLocation.split("/");
        JSONObject folderToRead = jsonObject;
        JSONObject folderToReadNew;
        for (String folder : Tree) {
            folderToReadNew = (JSONObject) folderToRead.get(folder);
            if (folderToReadNew == null){
                JSONObject folderCreator = new JSONObject();
                folderCreator.put("type", "directory");
                folderToRead.put(folder, folderCreator);
                folderToRead = (JSONObject) folderToRead.get(folder);
            }
            else{
                folderToRead = folderToReadNew;
            }
        }
        LinkedHashMap<String,String> contents = new LinkedHashMap<>();
        for (Object key : folderToRead.keySet()) {
            String keyStr = key.toString();
            try{
                if ((((JSONObject) folderToRead.get(keyStr)).get("owner")).toString().equals(userAccount)) {
                    String type = (((JSONObject) folderToRead.get(keyStr)).get("type")).toString();
                    contents.put(keyStr, type);
                }
            }
            catch (Exception ignored){}
        }
        
        return contents;
    }

    /**
     * This method deletes the designated item within the JSONObject.
     *
     * @param jsonObject        the JSONObject that is being read.
     * @param itemLocation      the virtual path within the JSONObject.
     */

    static JSONObject removeItem(JSONObject jsonObject, String itemLocation){
        String item = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        String[] folders = itemLocation.substring(0,itemLocation.lastIndexOf("/")).split("/");
        JSONObject folderToRead = jsonObject;

        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        try {
            folderToRead.remove(item);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return jsonObject;
    }

    /**
     * This method returns the JSONObject associated with the designated item.
     *
     * @param jsonObject        the JSONObject that is being read.
     * @param itemLocation      the virtual path within the JSONObject.
     */

    static JSONObject getItemContents(JSONObject jsonObject, String itemLocation){
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
     * @param jsonObject            the JSONObject that is being read.
     * @param itemLocation          the source virtual path within the JSONObject.
     * @param destinationLocation   the destination virtual path within the JSONObject
     *                              should not have the name of the item in this path
     */

    static JSONObject copyFile(JSONObject jsonObject, String itemLocation, String destinationLocation, boolean showDate) {
        String fileName = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        return(copyFile(jsonObject, itemLocation, destinationLocation, showDate, fileName));
    }

    /**
     * This method adds a directory within the designated folder within the JSONObject.
     *
     * @param jsonObject      the JSONObject that is being read.
     * @param jsonPath        the virtual path within the JSONObject.
     * @param directoryName   the name of the new folder.
     */

    static JSONObject addFolder(JSONObject jsonObject, String jsonPath, String directoryName, String userAccount){
        JSONObject type = new JSONObject();
        type.put("type", "directory");
        type.put("owner", userAccount);
        jsonObject = putItemInFolder(jsonObject, jsonPath, directoryName, type);
        return jsonObject;
    }

    /**
     * This method rename the given item within the JSONObject.
     *
     * @param jsonObject      the JSONObject that is being read.
     * @param itemLocation    the virtual path within the JSONObject.
     * @param newName         the new name for the file.
     */

    static JSONObject renameFile(JSONObject jsonObject, String itemLocation, String newName){
        return moveFile(jsonObject,itemLocation,itemLocation.substring(0,itemLocation.lastIndexOf("/")),newName);
    }

    /**
     * This method moves the given item to the designated path within the JSONObject.
     *
     * @param jsonObject            the JSONObject that is being read.
     * @param itemLocation          the source virtual path within the JSONObject.
     * @param destinationLocation   the destination virtual path within the JSONObject
     *                              should not have the name of the item in this path
     */

    static JSONObject moveFile(JSONObject jsonObject, String itemLocation, String destinationLocation){
        String fileName = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        return (moveFile(jsonObject,itemLocation,destinationLocation,fileName));
    }

    /**
     * This method adds a new file, with all of its properties, to the JSONObject.
     *
     * @param stripes               The list of computers the store each stripe and whole copy.
     * @param itemLocation          The source virtual path within the JSONObject.
     * @param fileName              The name of the file, the way it appears in the file browser.
     * @param JSONFilePath          The file path of the catalog file.
     * @param alphanumericName      The name of the file, as it is stored on the slave computers.
     * @param userAccount           The account name person that uploaded the file.
     * @param fileSize              The size of the file, in Bytes.
     */

    static void addToIndex(List<List<String>> stripes, String itemLocation, String fileName, String JSONFilePath, String alphanumericName, String userAccount, long fileSize) {

        JSONObject jsonFile = JSONManipulator.getJSONObject(JSONFilePath);
        jsonFile.replace("currentName", alphanumericName);


        JSONObject objChild = new JSONObject();
        JSONArray ipArray = new JSONArray();



        for (int stripe = 0; stripe < stripes.size(); stripe++){
            for (int copy = 0; copy < (stripes.get(stripe)).size(); copy++){
                ipArray.add(stripes.get(stripe).get(copy));
            }
            if (stripe == 0){
                objChild.put("whole", ipArray.clone());
            }
            else{
                objChild.put("stripe" + "_" + String.valueOf(stripe-1), ipArray.clone());
            }
            ipArray.clear();

        }
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy h:mm a");
        String creationDate = df.format(new Date());

        objChild.put("owner", userAccount);
        objChild.put("type", "file");
        objChild.put("fileName", alphanumericName);
        objChild.put("fileSize", humanReadableByteCount(fileSize, true));
        objChild.put("creationDate", creationDate);

        jsonFile = JSONManipulator.putItemInFolder(jsonFile, itemLocation, fileName,objChild);


        try{
            writeJSONObject(JSONFilePath, jsonFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method adds a new file, with all of its properties, to the JSONObject.
     *
     * @param itemLocation          The source virtual path within the JSONObject.
     * @param fileName              The name of the file, the way it appears in the file browser.
     * @param JSONFilePath          The file path of the catalog file.
     * @param userAccount           The account name person that uploaded the file.
     *
     */

    static void addToIndex(String itemLocation, String fileName, String JSONFilePath, String userAccount) {
        JSONObject jsonFile = JSONManipulator.getJSONObject(JSONFilePath);
        JSONObject objChild = new JSONObject();
        objChild.put("type", "tempFile");
        objChild.put("owner", userAccount);
        jsonFile = JSONManipulator.putItemInFolder(jsonFile, itemLocation, fileName, objChild);
        try{
            writeJSONObject(JSONFilePath, jsonFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method writes out a json file from a JSONObject.
     *
     * @param filePath      where the json file is to be written to.
     * @param obj           The JSONObject the is to be written the json file
     * @throws IOException  if the file cannot be written.
     *
     */

    static void writeJSONObject(String filePath, JSONObject obj) throws IOException {
        File file = new File(filePath);
        FileOutputStream fos = new FileOutputStream(file);
        FileWriter fw = new FileWriter(file);

        while (true) {
            try {
                FileLock fl = fos.getChannel().lock();

                fw.write(obj.toJSONString());

                fl.release();
                fos.close();
                break;
            } catch (OverlappingFileLockException ofle) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    break;
                }
            }
        }
    }

    /**
     * This method adds a new file, with all of its properties, to the JSONObject.
     *
     * @param itemLocation      The source virtual path within the JSONObject.
     * @param path              Where the download file is to be saved to.
     * @param outFile           The name that the download file is to be saved as.
     * @param serverAddress     The address of the the master server
     * @param port              The port that MeshFS uses
     * @throws IOException if a socket cannot be initialized
     *
     */

    static boolean pullFile(String itemLocation, String path, String outFile, String serverAddress, int port) throws IOException {
        String outFileDir = path.substring(0, path.lastIndexOf(File.separator));
        String catalogFileLocation = ".catalog.json";
        String manifestFileLocation = ".manifest.json";
        FileClient.receiveFile(serverAddress, port, manifestFileLocation, manifestFileLocation);
        JSONObject compInfoFile = getJSONObject(manifestFileLocation);
        String[] folders = itemLocation.split("/");
        JSONObject itemToRead = JSONManipulator.getJSONObject(catalogFileLocation);
        List<String> stripeNames = new ArrayList<>();
        List<Thread> childThreads = new ArrayList<>();
        boolean wholeNecessary = true;

        for (String folder : folders) {
            itemToRead = (JSONObject) itemToRead.get(folder);
        }

        String fileName = itemToRead.get("fileName").toString();

        for (Object stripe: itemToRead.keySet() ) {
            if (stripe.toString().contains("stripe")) {
                wholeNecessary = false;
                String fileNameWNum = fileName + "_s" + stripe.toString().substring(stripe.toString().lastIndexOf("_")+1);
                JSONArray compsWithStripe = (JSONArray) itemToRead.get(stripe);
                boolean cantContinue = true;

                for (Object MACAddress : compsWithStripe) {
                    if (compInfoFile.containsKey(MACAddress)) {
                        if (((JSONArray)(((JSONObject)compInfoFile.get(MACAddress)).get("RepoContents"))).contains(fileNameWNum)){
                            String IPAddress = (((JSONObject)compInfoFile.get(MACAddress)).get("IP")).toString();

                            Thread child = new Thread (() -> {
                                try {
                                    FileClient.receiveFile(IPAddress, port, fileNameWNum, outFileDir + File.separator + "." + fileNameWNum);
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
        if (wholeNecessary){
            String fileNameW = fileName +"_w";
            JSONArray compsWithWhole = (JSONArray) itemToRead.get("whole");
            boolean cantContinue = true;

            for (Object MACAddress : compsWithWhole) {
                if (compInfoFile.containsKey(MACAddress)) {
                    if (((JSONArray)(((JSONObject)compInfoFile.get(MACAddress)).get("RepoContents"))).contains(fileNameW)){
                        String IPAddress = ((JSONObject)compInfoFile.get(MACAddress)).get("IP").toString();
                        FileClient.receiveFile(IPAddress, port,fileNameW, outFileDir + File.separator + "." + outFile);
                        new File(outFileDir + File.separator + "." + outFile).renameTo(new File(outFileDir + File.separator + outFile));
                        cantContinue = false;
                        break;
                    }
                }
            }
            if (cantContinue){
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
            FileUtils.combineStripes(numberSorter(stripeNames),outFileDir  + File.separator + "." + outFile);
            new File(outFileDir + File.separator + "." + outFile).renameTo(new File(outFileDir + File.separator + outFile));
        }
        for (String filePath:stripeNames){
            Files.deleteIfExists(Paths.get(filePath));
        }
        return true;
    }

    static LinkedHashMap<String, Long> createStorageMap(JSONObject manifestFile){
        LinkedHashMap<String,Long> storageMap = new LinkedHashMap();
        for (Object MACAddress: manifestFile.keySet()){
            storageMap.put(MACAddress.toString(),Long.valueOf((((JSONObject)manifestFile.get(MACAddress)).get("FreeSpace")).toString()));
        }
        return storageMap;
    }

    static JSONObject putItemInFolder(JSONObject jsonObject, String itemDestinationLocation, String fileName, JSONObject itemContents){
        String[] folders = itemDestinationLocation.split("/");
        JSONObject folderToRead = jsonObject;
        JSONObject folderToReadNew;
        for (String folder : folders) {
            folderToReadNew = (JSONObject) folderToRead.get(folder);
            if (folderToReadNew == null){
                JSONObject folderCreator = new JSONObject();
                folderCreator.put("type", "directory");
                folderToRead.put(folder, folderCreator);
                folderToRead = (JSONObject) folderToRead.get(folder);
            }
            else{
                folderToRead = folderToReadNew;
            }
        }
        folderToRead.put(fileName, itemContents);
        return jsonObject;
    }

    static JSONObject moveFile(JSONObject jsonObject, String itemLocation, String destinationLocation, String NewName){
        jsonObject = copyFile(jsonObject, itemLocation, destinationLocation, false, NewName);
        jsonObject = removeItem(jsonObject, itemLocation);
        return jsonObject;
    }

    static JSONObject copyFile(JSONObject jsonObject, String itemLocation, String destinationLocation, boolean showDate, String newName){
        JSONObject itemContents = getItemContents(jsonObject,itemLocation);

        if(showDate){
            DateFormat df = new SimpleDateFormat("h:mm:ss a");
            Date dateObj = new Date();
            jsonObject = putItemInFolder(jsonObject, destinationLocation, newName+" ("+ df.format(dateObj)+")", itemContents);
        }else{
            jsonObject = putItemInFolder(jsonObject, destinationLocation, newName, itemContents);
        }

        return jsonObject;
    }

    private static String humanReadableByteCount(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    private static List<String> numberSorter (List<String> unsorted){
        String name = unsorted.get(0).substring(0,unsorted.get(0).lastIndexOf("s")+1);
        int[] numbers = new int[unsorted.size()];
        int index = 0;
        for(String nameWithNum: unsorted){
            numbers[index] = Integer.parseInt(nameWithNum.substring(nameWithNum.lastIndexOf("s")+1));
            index++;
        }
        Arrays.sort(numbers);
        unsorted.clear();
        for(int number : numbers){
            unsorted.add(name + String.valueOf(number));
        }
        return unsorted;
    }
}
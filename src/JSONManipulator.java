import org.json.simple.parser.JSONParser;
import org.json.simple.*;

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
 * Created by Levi Muniz on 10/19/16.
 */
public class JSONManipulator {

    public static JSONObject getJSONObject(String filePath) {
        JSONParser reader = new JSONParser();
        JSONObject jsonObject = null;
        try {
            Object obj = reader.parse(new FileReader(filePath));
            jsonObject = (JSONObject) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONArray getJSONArray(String filePath) {
        JSONParser reader = new JSONParser();
        JSONArray jsonArray = null;
        try {
            Object obj = reader.parse(new FileReader(filePath));

            jsonArray = (JSONArray) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public static Map<String,String> getMapOfFolderContents(JSONObject jsonObject, String folderLocation, String userAccount){
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
        Map<String,String> contents = new HashMap<>();
        for (Object key : folderToRead.keySet()) {
            String keyStr = key.toString();
            try{
                if ((((JSONObject) folderToRead.get(keyStr)).get("owner")).toString().equals(userAccount)) {
                    String type = (((JSONObject) folderToRead.get(keyStr)).get("type")).toString();
                    contents.put(keyStr, type);
                }
            }
            catch (Exception e){}
        }
        return contents;
    }

    public static JSONObject removeItem(JSONObject jsonObject, String itemLocation){
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

    public static JSONObject putItemInFolder(JSONObject jsonObject, String itemDestinationLocation, String fileName, JSONObject itemContents){
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

    public static JSONObject getItemContents(JSONObject jsonObject, String itemLocation){
        String[] folders = itemLocation.split("/");
        JSONObject folderToRead = jsonObject;
        for (String folder : folders) {
            folderToRead = (JSONObject) folderToRead.get(folder);
        }
        return folderToRead;
    }

    public static JSONObject copyFile(JSONObject jsonObject, String itemLocation, String destinationLocation, boolean showDate) {
        String fileName = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        return(copyFile(jsonObject, itemLocation, destinationLocation, showDate, fileName));
    }

    public static JSONObject copyFile(JSONObject jsonObject, String itemLocation, String destinationLocation, boolean showDate, String newName){
        JSONObject itemContents = getItemContents(jsonObject,itemLocation);

        DateFormat df = new SimpleDateFormat("h:mm:ss a");
        Date dateObj = new Date();
        if(showDate){
            jsonObject = putItemInFolder(jsonObject, destinationLocation, newName+" ("+ df.format(dateObj)+")", itemContents);
        }else{
            jsonObject = putItemInFolder(jsonObject, destinationLocation, newName, itemContents);
        }

        return jsonObject;
    }

    public static JSONObject addFolder(JSONObject jsonObject, String jsonPath, String directoryName, String userAccount){
        JSONObject type = new JSONObject();
        type.put("type", "directory");
        type.put("owner", userAccount);
        jsonObject = putItemInFolder(jsonObject, jsonPath, directoryName, type);
        return jsonObject;
    }

    public static JSONObject renameFile(JSONObject jsonObject, String itemLocation, String newName){
        return moveFile(jsonObject,itemLocation,itemLocation.substring(0,itemLocation.lastIndexOf("/")),newName);
    }

    public static JSONObject moveFile(JSONObject jsonObject, String itemLocation, String destinationLocation){
        String fileName = itemLocation.substring(itemLocation.lastIndexOf("/")+1);
        return (moveFile(jsonObject,itemLocation,destinationLocation,fileName));
    }

    public static JSONObject moveFile(JSONObject jsonObject, String itemLocation, String destinationLocation, String NewName){
        jsonObject = copyFile(jsonObject, itemLocation, destinationLocation, false, NewName);
        jsonObject = removeItem(jsonObject, itemLocation);
        return jsonObject;
    }

    public static void addToIndex(List<List<String>> stripes, String itemLocation, String fileName, String JSONFilePath, String alphanumericName, String userAccount, long fileSize) {

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
        objChild.put("fileSizeActual", Math.toIntExact(fileSize));
        objChild.put("fileSize", humanReadableByteCount(fileSize, true));
        objChild.put("creationDate", creationDate);

        jsonFile = JSONManipulator.putItemInFolder(jsonFile, itemLocation, fileName,objChild);


        try{
            writeJSONObject(JSONFilePath, jsonFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void addToIndex(String itemLocation, String fileName, String JSONFilePath) {

        JSONObject jsonFile = JSONManipulator.getJSONObject(JSONFilePath);

        JSONObject objChild = new JSONObject();


        objChild.put("type", "file");
        objChild.put("fileName", fileName);

        jsonFile = JSONManipulator.putItemInFolder(jsonFile, itemLocation, fileName,objChild);


        try{
            writeJSONObject(JSONFilePath, jsonFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void addToIndex(String itemLocation, String fileName, String JSONFilePath, String userAccount, boolean tempFile) {
        if(tempFile){
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
    }

    public static void writeJSONObject(String filePath, JSONObject obj) throws IOException {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(obj.toJSONString());
        }
    }

    public static boolean pullFile(String itemLocation, String path, String outFile, String serverAddress, int portNumber) throws IOException {
        String outFileDir = path.substring(0, path.lastIndexOf(File.separator));
        int port = Integer.parseInt(MeshFS.properties.getProperty("portNumber"));
        String catalogFileLocation = ".catalog.json";
        String manifestFileLocation = ".manifest.json";
        String[] folders = itemLocation.split("/");
        JSONObject itemToRead = JSONManipulator.getJSONObject(catalogFileLocation);
        JSONObject compInfoFile = getJSONObject(manifestFileLocation);
        List<String> stripeNames = new ArrayList<>();
        boolean wholeNecessary = false;

        FileClient.receiveFile(serverAddress, portNumber, ".manifest.json", ".manifest.json");

        for (String folder : folders) {
            itemToRead = (JSONObject) itemToRead.get(folder);
        }

        String fileName = itemToRead.get("fileName").toString();

        for (Object stripe: itemToRead.keySet() ) {
            if (stripe.toString().contains("stripe")) {
                String fileNameWNum = fileName + "_s" + stripe.toString().substring(stripe.toString().lastIndexOf("_")+1);
                JSONArray compsWithStripe = (JSONArray) itemToRead.get(stripe);
                boolean cantContinue = true;

                for (Object MACAddress : compsWithStripe) {
                    if (compInfoFile.containsKey(MACAddress)) {
                        if (((JSONArray)(((JSONObject)compInfoFile.get(MACAddress)).get("RepoContents"))).contains(fileNameWNum)){
                            String IPAddress = (((JSONObject)compInfoFile.get(MACAddress)).get("IP")).toString();

                            Thread child = new Thread (() -> {
                                try {
                                    FileClient.receiveFile(IPAddress, port, fileNameWNum, outFileDir + File.separator + fileNameWNum);
                                } catch (IOException ioe) {
                                    ioe.printStackTrace();
                                }
                            });


                            stripeNames.add(outFileDir + File.separator + fileNameWNum);

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
                        String IPAddress = (((JSONObject)compInfoFile.get(MACAddress)).get("IP")).toString();
                        System.out.println("attemting to recieve stripe " + fileNameW + "from " + IPAddress);
                        FileClient.receiveFile(IPAddress, port,fileNameW, outFileDir + File.separator + outFile);
                        System.out.println("recieved stripe " + fileNameW);
                        cantContinue = false;
                        break;
                    }
                }
            }
            if (cantContinue){
                return false;
            }
        }

        else {
            FileUtils.combineStripes(numberSorter(stripeNames),outFileDir  + File.separator + path.substring(path.lastIndexOf("/")));

        }for (String filePath:stripeNames){
            Files.deleteIfExists(Paths.get(filePath));
        }
        return true;
    }

    public static LinkedHashMap<String, Long> createStorageMap(JSONObject manifestFile){
        LinkedHashMap<String,Long> storageMap = new LinkedHashMap();
        for (Object MACAddress: manifestFile.keySet()){
            storageMap.put(MACAddress.toString(),Long.valueOf((((JSONObject)manifestFile.get(MACAddress)).get("FreeSpace")).toString()));
        }
        return storageMap;
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
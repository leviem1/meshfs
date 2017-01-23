/**
 * Created by Levi Muniz on 10/13/16.
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Reporting {

    public static String getSystemOS() {
        return System.getProperty("os.name");
    }

    public static long getSystemStorage() {
        String os = System.getProperty("os.name");
        File file;
        if (os.startsWith("Windows")) {
            file = new File("c:");
        } else {
            file = new File("/");
        }
        return file.getUsableSpace();
    }

    public static List<String> getIpAddress() {
        List<String> ip = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp())
                    continue;
                List<InterfaceAddress> addresses = iface.getInterfaceAddresses();
                for (InterfaceAddress currIface : addresses) {
                    if ((currIface.getAddress()) instanceof Inet4Address) {
                        ip.add(currIface.toString().substring(1, (currIface.toString().substring(1).indexOf("/")) + 1));
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return ip;
    }

    public static long getUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    public static String getJavaVersion(){
        return System.getProperty("java.version");
    }

    public static String getUserName(){
        return System.getProperty("user.name");
    }

    public static String getSystemDate(){
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    public static String getMacAddress(){
        String macAddress = null;
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while(networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();
                if(mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    macAddress = sb.toString();
                }
            }
        } catch (SocketException e){
            e.printStackTrace();
        }
        return macAddress;
    }

    public static String getRepositoryContents() {
        return Arrays.toString(new File(MeshFS.properties.getProperty("repository")).listFiles());
    }

    public static String generate() {
        return getMacAddress() + "|IP:" + getIpAddress() + ";OS:" + getSystemOS() + ";JavaVersion:" + getJavaVersion() + ";FreeSpace:" + getSystemStorage() + ";Uptime:" + getUptime() + ";Username:" + getUserName() + ";RepoContents:" + getRepositoryContents();
    }

    public static JSONArray splitter(String report) {
        JSONObject jsonObject = new JSONObject();
        JSONArray mainArray = new JSONArray();
        String[] reportArray = report.split("\\|");
        String[] reportObjects = reportArray[1].split(";");

        for (String reportSet : reportObjects) {
            String[] reportSetData = reportSet.split(":");
            if (reportSetData[0].equals("RepoContents")){
                JSONArray Contents = new JSONArray();
                String[] files = reportSetData[1].substring(0,reportSetData[1].length() -1).split(",");
                for (String file : files){
                    Contents.add(file.substring(file.lastIndexOf("/")+1));
                }
                jsonObject.put(reportSetData[0],Contents);
            }
            else{
                jsonObject.put(reportSetData[0],reportSetData[1]);
            }
        }
        mainArray.add(reportArray[0]);
        mainArray.add(jsonObject);
        return mainArray;
    }
}
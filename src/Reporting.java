/**
 * Created by Levi Muniz on 10/13/16.
 */
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Reporting {

    static String getSystemOS() {
        return System.getProperty("os.name");
    }

    static long getSystemStorage() {
        String os = System.getProperty("os.name");
        File file;
        if (os.startsWith("Windows")) {
            file = new File("c:");
        } else {
            file = new File("/");
        }
        return file.getUsableSpace() - (Integer.parseInt(MeshFS.properties.getProperty("minSpace")) * 1073741824);
    }

    static List<String> getIpAddresses() {
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

    static long getUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    static String getJavaVersion(){
        return System.getProperty("java.version");
    }

    static String getUserName(){
        return System.getProperty("user.name");
    }

    static String getSystemDate(){
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    static String getMacAddress(){
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

    static String getRepositoryContents() {
        File[] repoContents = new File(MeshFS.properties.getProperty("repository")).listFiles();
        List<String> contents = new ArrayList<>();
        for (File file : repoContents){
            contents.add(file.getName());
        }
        return contents.toString();
    }

    static String generate() {
        return getMacAddress() + "|IP:" + getIpAddresses().get(0) + ";OS:" + getSystemOS() + ";JavaVersion:" + getJavaVersion() + ";FreeSpace:" + getSystemStorage() + ";Uptime:" + getUptime() + ";Username:" + getUserName() + ";RepoContents:" + getRepositoryContents();
    }

    static JSONArray splitter(String report) {
        JSONObject jsonObject = new JSONObject();
        JSONArray mainArray = new JSONArray();
        String[] reportArray = report.split("\\|");
        String[] reportObjects = reportArray[1].split(";");

        for (String reportSet : reportObjects) {
            String key = reportSet.substring(0,reportSet.indexOf(":"));
            String value = reportSet.substring(reportSet.indexOf(":")+1);
            if (key.equals("RepoContents")){
                JSONArray Contents = new JSONArray();
                String[] files = value.substring(1,value.length() -1).split(", ");
                for (String file : files){
                    Contents.add(file);
                }
                jsonObject.put(key,Contents);
            }
            else{
                String[] reportSetData = reportSet.split(":");
                jsonObject.put(reportSetData[0],reportSetData[1]);
            }
        }
        mainArray.add(reportArray[0]);
        mainArray.add(jsonObject);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Date dateObj = new Date();
        System.out.println(reportArray[0] + " checked in at " + df.format(dateObj));
        return mainArray;
    }
}
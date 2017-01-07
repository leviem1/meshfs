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

    public static List<List> getIpAddress() {
        List<String> ip = new ArrayList<>();
        List<List> ipRefined = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();

                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                
                while(addresses.hasMoreElements()) {
                    ip.add(addresses.nextElement().getHostAddress());
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        for(int x = 0; x < ip.size(); x++){
            if(ip.get(x).contains("%")){
                try {
                    if (ip.get(x + 1).contains(".")) {
                        List<String> tempList = new ArrayList<>();
                        tempList.add(ip.get(x).substring(ip.get(x).indexOf("%") + 1));
                        tempList.add(ip.get(x + 1));
                        ipRefined.add(tempList);
                    }
                } catch (IndexOutOfBoundsException ae) {
                }
            }
        }


        return ipRefined;
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

    public static JSONObject splitter(String report) {
        JSONArray jsonArray = new JSONArray();
        JSONObject mainObj = new JSONObject();

        String[] reportArray = report.split("\\|");
        String[] reportObjects = reportArray[1].split(";");

        for (String reportSet : reportObjects) {
            String[] reportSetData = reportSet.split(":");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(reportSetData[0],reportSetData[1]);
            jsonArray.add(jsonObject);
        }

        mainObj.put(reportArray[0],jsonArray);

        return mainObj;
    }
}
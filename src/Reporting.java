/**
 * Created by Levi Muniz on 10/13/16.
 */
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;

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

    public static String getIpAddress() {
        String ip = null;
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
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

    public static String getSystemTime(){
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

    public static String getMacAddress(){
        String macAddress = "FF-FF-FF-FF-FF-FF-FF-FF";
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

    public static String getRepositoryContents(File repository) {
        return Arrays.toString(repository.listFiles());
    }

    public static String generate(File repository) {
        String report = "MAC:" + getMacAddress() + "\nIP:" + getIpAddress() + "\nOS:" + getSystemOS() + "\nJavaVersion:" + getJavaVersion() + "\nFreeSpace:" + getSystemStorage() + "\nUptime:" + getUptime() + "\nUsername:" + getUserName() + "\nRepoContents:" + getRepositoryContents(repository);
        return report;
    }

    public static HashMap<String, String> splitter(String report) {
        HashMap<String, String> map = new HashMap<>();
        String[] reportLines = report.split("\n");

        for (String line : reportLines){
            String[] lineSplit = line.split(":");
            map.put(lineSplit[0], lineSplit[1]);
        }

        return map;
    }
}
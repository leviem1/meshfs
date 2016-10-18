/**
 * Created by Levi Muniz on 10/13/16.
 */
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;

public class Reporting {

    public String getSystemOS() {
        return System.getProperty("os.name");
    }

    public long getSystemStorage() {
        String os = System.getProperty("os.name");
        File file;
        if (os.startsWith("Windows")) {
            file = new File("c:");
        } else {
            file = new File("/");
        }
        return file.getUsableSpace();
    }
    public String getIpAddress() {
        String ip = null;
        try {
            ip = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }
    public long getUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }
    public String getJavaVersion(){
        return System.getProperty("java.version");
    }
    public String getUserName(){
        return System.getProperty("user.name");
    }
    public String getSystemDate(){
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
    public String getSystemTime(){
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME);
    }
    public String getMacAddress(){
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
}
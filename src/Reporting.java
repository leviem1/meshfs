/**
 * Created by Levi Muniz on 10/13/16.
 */
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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

}

/**
 * Created by Levi Muniz on 10/13/16.
 */

import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class Reporting {

    private final String os = System.getProperty("os.name");

    public String getSystemOS() {
        return os;
    }

    public long getSystemStorage() {

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

}

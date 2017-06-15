import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class is used get system information.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

class Reporting {

    private Reporting() {
    }

    /**
     * This method is used to get the operating system's name that the JVM is being run on.
     *
     * @return the operating system name
     */

    static String getSystemOS() {
        return System.getProperty("os.name");
    }

    /**
     * This method is used to get the operating system's name that the JVM is being run on.
     *
     * @return the number of bytes available on the disk minus the space allocated in
     * .config.properties
     */

    static long getSystemStorage() {
        String os = System.getProperty("os.name");
        File file;
        if (os.startsWith("Windows")) {
            file = new File("c:");
        } else {
            file = new File("/");
        }

        return file.getUsableSpace()
                - (Long.parseLong(MeshFS.properties.getProperty("minSpace")) * 1073741824L);
    }

    /**
     * This method is used to get the IPv4 addresses of the host machine.
     *
     * @return list of IPv4 addresses
     */

    static List<String> getIpAddresses() {
        List<String> ip = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) continue;
                List<InterfaceAddress> addresses = iface.getInterfaceAddresses();
                for (InterfaceAddress currIface : addresses) {
                    if ((currIface.getAddress()) instanceof Inet4Address) {
                        ip.add(
                                currIface
                                        .toString()
                                        .substring(1, (currIface.toString().substring(1).indexOf("/")) + 1));
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return ip;
    }

    /**
     * This method is used to get the local date and time of the host machine.
     *
     * @return local date and time
     */

    static String getSystemDate() {
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    private static long getUptime() {
        return ManagementFactory.getRuntimeMXBean().getUptime();
    }

    private static String getJavaVersion() {
        return System.getProperty("java.version");
    }

    private static String getUserName() {
        return System.getProperty("user.name");
    }

    /**
     * This method is used to get the MAC address of the host computer
     *
     * @return the MAC address
     */

    static String getMacAddress() {
        String macAddress = null;
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] mac = network.getHardwareAddress();
                if (mac != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++) {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }
                    macAddress = sb.toString();
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return macAddress;
    }

    /**
     * This method is used to get the repository contents of the computer
     *
     * @return the repository contents
     */

    static String getRepositoryContents() {
        File[] repoContents = new File(MeshFS.properties.getProperty("repository")).listFiles();
        List<String> contents = new ArrayList<>();
        if (repoContents != null) {
            for (File file : repoContents) {
                contents.add(file.getName());
            }
        }
        return contents.toString();
    }

    /**
     * This method is used to generate a string of system information.
     *
     * @return report of system information
     */

    static String generate() {
        return getMacAddress()
                + "|IP:"
                + getIpAddresses().get(0)
                + ";OS:"
                + getSystemOS()
                + ";JavaVersion:"
                + getJavaVersion()
                + ";FreeSpace:"
                + getSystemStorage()
                + ";Uptime:"
                + getUptime()
                + ";Username:"
                + getUserName()
                + ";RepoContents:"
                + getRepositoryContents();
    }

    /**
     * This method is used to convert a generated string report to a JSONArray.
     *
     * @param report the report to convert to JSONArray
     * @return JSONArray of report
     */
    @SuppressWarnings("unchecked")

    static JSONArray splitter(String report) {
        JSONObject jsonObject = new JSONObject();
        JSONArray mainArray = new JSONArray();
        String[] reportArray = report.split("\\|");
        String[] reportObjects = reportArray[1].split(";");

        for (String reportSet : reportObjects) {
            String key = reportSet.substring(0, reportSet.indexOf(":"));
            String value = reportSet.substring(reportSet.indexOf(":") + 1);
            if (key.equals("RepoContents")) {
                JSONArray Contents = new JSONArray();
                String[] files = value.substring(1, value.length() - 1).split(", ");

                Collections.addAll(Contents, files);

                jsonObject.put(key, Contents);
            } else {
                String[] reportSetData = reportSet.split(":");

                jsonObject.put(reportSetData[0], reportSetData[1]);
            }
        }

        jsonObject.put("checkInTimestamp", new Date().getTime());

        mainArray.add(reportArray[0]);

        mainArray.add(jsonObject);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Date dateObj = new Date();
        System.out.println(reportArray[0] + " checked in at " + df.format(dateObj));
        return mainArray;
    }
}

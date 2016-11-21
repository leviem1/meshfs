import java.io.*;
import java.util.Properties;

/**
 * Created by Levi Muniz on 10/29/16.
 */

public class ConfigParser {

    public static Properties loadDefaultProperties() {
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty("numStripes","3");
        defaultProperties.setProperty("numStripeCopy", "2");
        defaultProperties.setProperty("numWholeCopy", "2");
        defaultProperties.setProperty("minSpace", "0");
        defaultProperties.setProperty("masterIP","127.0.0.1");
        defaultProperties.setProperty("preferredInterface", "");
        defaultProperties.setProperty("portNumber","5704");
        defaultProperties.setProperty("repository", ("repo" + File.separator));
        defaultProperties.setProperty("serverThreads", "16");
        defaultProperties.setProperty("serverTimeout", "90");
        return defaultProperties;
    }

    public static void write(Properties props) {
        OutputStream output = null;

        try {
            output = new FileOutputStream("config.properties");
            props.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Properties reader(String path) throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream(path);
        prop.load(input);
        return prop;
    }

    public static Properties loadProperties() {

        Properties properties;

        try {
            properties = reader("config.properties");
            Properties defaultProperties = loadDefaultProperties();

            if (!properties.stringPropertyNames().equals(defaultProperties.stringPropertyNames())) {
                for (String key : defaultProperties.stringPropertyNames()) {
                    if (properties.getProperty(key) == null) {
                        properties.setProperty(key, defaultProperties.getProperty(key));
                    }
                }

                write(properties);
            }

        } catch (IOException io) {
            properties = loadDefaultProperties();
            write(properties);
        }

        return properties;
    }

    public static void backup(Properties props, String destination) {
        OutputStream output = null;
        try {
            output = new FileOutputStream(destination + "/backup_config_" + Reporting.getSystemDate() + ".properties");
            props.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

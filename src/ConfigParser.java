import java.io.*;
import java.util.Properties;
import java.util.UUID;

/**
 * The ConfigParser class is used to manipulate the .config.properties file.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

class ConfigParser {

    private ConfigParser() {
    }

    /**
     * This method is used to load the default values of properties of MeshFS.
     *
     * @return the default property values
     */

    static Properties loadDefaultProperties() {
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty("numStripes", "3");
        defaultProperties.setProperty("numStripeCopy", "2");
        defaultProperties.setProperty("numWholeCopy", "2");
        defaultProperties.setProperty("minSpace", "0");
        defaultProperties.setProperty("masterIP", "127.0.0.1");
        defaultProperties.setProperty("portNumber", "5704");
        defaultProperties.setProperty("multicastGroup", "239.91.93.42");
        defaultProperties.setProperty("multicastPort", "5703");
        defaultProperties.setProperty("repository", ("repo" + File.separator));
        defaultProperties.setProperty("serverThreads", "16");
        defaultProperties.setProperty("timeout", "5");
        defaultProperties.setProperty("uuid", UUID.randomUUID().toString());

        return defaultProperties;
    }

    /**
     * This method is used to load the properties saved in .config.properties.
     *
     * @return the saved property values
     */

    static Properties loadProperties() {
        Properties properties;

        try {
            properties = reader();
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

    /**
     * This method is write the set properties to the .config.properties.
     *
     * @param props the properties to write out
     */

    static void write(Properties props) {
        OutputStream output = null;

        try {
            output = new FileOutputStream(".config.properties");
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

    private static Properties reader() throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream(".config.properties");
        prop.load(input);
        if (!(prop.getProperty("repository").substring(prop.getProperty("repository").length() - 1))
                .equals(File.separator)) {
            prop.setProperty("repository", prop.getProperty("repository") + File.separator);
            write(prop);
        }

        if (prop.getProperty("timeout") == null) prop.setProperty("timeout", "5");
        input.close();
        return prop;
    }
}

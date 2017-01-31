import java.io.*;
import java.util.Properties;

/**
 * Created by Levi Muniz on 10/29/16.
 */

class ConfigParser {

    static Properties loadDefaultProperties() {
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty("numStripes","3");
        defaultProperties.setProperty("numStripeCopy", "2");
        defaultProperties.setProperty("numWholeCopy", "2");
        defaultProperties.setProperty("minSpace", "0");
        defaultProperties.setProperty("masterIP","127.0.0.1");
        defaultProperties.setProperty("portNumber","5704");
        defaultProperties.setProperty("repository", ("repo" + File.separator));
        defaultProperties.setProperty("serverThreads", "16");
        defaultProperties.setProperty("timeout", "5");
        return defaultProperties;
    }

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
        if (!(prop.getProperty("repository").substring(prop.getProperty("repository").length()-1)).equals(File.separator)) {
            prop.setProperty("repository", prop.getProperty("repository") + File.separator);
            write(prop);
        }

        if (prop.getProperty("timeout") == null) prop.setProperty("timeout", "5");
        input.close();
        return prop;
    }

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
}

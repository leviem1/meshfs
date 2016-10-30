import java.io.*;
import java.util.Properties;

/**
 * Created by Levi Muniz on 10/29/16.
 */
public class ConfigParser {
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

    public static Properties reader() throws IOException {
        Properties prop = new Properties();
        InputStream input = new FileInputStream("config.properties");
        prop.load(input);
        return prop;
    }

}

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Levi Muniz on 1/12/17.
 */
public class FileUtils {

    public static long getSize(String filePath) {
        return (new File(filePath).length());
    }

    public static void writeStripe(String filePath, String outFile, long off, long len) throws IOException {
        int br = 0;
        int read;
        byte[] data = new byte[4096];
        FileInputStream fis = new FileInputStream(filePath);
        FileOutputStream fos = new FileOutputStream(outFile);

        fis.skip(off);

        while ((br != -1) && (len > 0)) {
            if (len <= 4096) {
                System.out.println("Go!");
                read = Math.toIntExact(len);
            } else {
                read = data.length;
            }

            br = fis.read(data, 0, read);
            fos.write(data, 0, br);
            fos.flush();

            len -= br;
        }
    }
}

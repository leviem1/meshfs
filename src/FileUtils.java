import java.io.*;
import java.util.List;

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
                read = Math.toIntExact(len);
            } else {
                read = data.length;
            }

            br = fis.read(data, 0, read);
            fos.write(data, 0, br);
            fos.flush();

            len -= br;
        }

        fis.close();
        fos.close();
    }

    static void combineStripes(List<String> stripes, String outFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(outFile);

        for (String stripe : stripes) {
            int br;
            byte[] data = new byte[4096];
            FileInputStream fis = new FileInputStream(stripe);

            while ((br = fis.read(data, 0, data.length)) != -1) {
                fos.write(data, 0, br);
                fos.flush();
            }

            fis.close();
        }

        fos.close();
    }

    static boolean removeFile(String path) {
        return (new File(path)).delete();
    }
}
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * The FileUtils class handles the manipulation of files.
 *
 * @author Levi Muniz
 * @version 1.0.0
 */
class FileUtils {

    private FileUtils() {
    }

    /**
     * This method returns the size of a file in bytes.
     *
     * @param filePath the path to the file
     * @return the size of a file in bytes
     */
    static long getSize(String filePath) {
        return new File(filePath).length();
    }

    /**
     * This method writes a file depending on the offset and length of its source.
     *
     * @param filePath the path to the source file
     * @param outFile  the path to the output file
     * @param off      the offset of the file to read
     * @param len      the total mount of bytes to read
     * @throws IOException on error reading or writing files
     */
    static void writeStripe(String filePath, String outFile, long off, long len) throws IOException {
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

    /**
     * This method writes a file with data from a list of supplied files.
     *
     * @param stripes a list of the files to combine
     * @param outFile the path to the output file
     * @throws IOException on error reading or writing files
     */
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

    /**
     * This method deletes a file.
     *
     * @param path path of the file to delete
     * @return true on success, false on failure
     */
    static boolean removeFile(String path) {
        return new File(path).delete();
    }

    static String getMD5Hash(String path) throws IOException, NoSuchAlgorithmException {
        int br;
        MessageDigest md = MessageDigest.getInstance("SHA1");
        FileInputStream fis = new FileInputStream(path);
        byte[] data = new byte[1024];

        while ((br = fis.read(data, 0, data.length)) != -1) {
            md.update(data, 0, br);
        }

        byte[] mdData = md.digest();

        StringBuffer hash = new StringBuffer("");

        for (int i = 0; i < mdData.length; i++) {
            hash.append(Integer.toString((mdData[i] & 0xff) + 0x100, 16).substring(1));
        }

        fis.close();

        return hash.toString();
    }

    static long getModificationDate(String path) {
        return new File(path).lastModified();
    }
}

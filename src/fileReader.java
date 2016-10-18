import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Levi Muniz on 10/17/16.
 */

public class fileReader {
    private BufferedInputStream bis;
    private InputStream inStream;
    private long lengthAvailable;

   public fileReader (String filePath) throws Exception {
       try {
           inStream = new FileInputStream(filePath);
           bis = new BufferedInputStream(inStream);
           lengthAvailable = bis.available();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }

   public byte[] readNext() throws IOException {
       byte[] data = new byte[4096];
       bis.read(data, 0, 4096);
       return data;
   }

   public long getSize(){
       return lengthAvailable;
   }

   public boolean closeFile() throws Exception {
       try {
           if (inStream != null) {
               inStream.close();
           }
           if (bis != null) {
               bis.close();
           }
           return true;
       } catch (Exception e) {
           return false;
       }
   }

}

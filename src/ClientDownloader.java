import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class ClientDownloader {

    private String hostName;
    private int portNumber;
    private int fileSize;
    private String outputPath;

    public void downloadData(String hostName, int portNumber, String outputPath, int fileSize) throws IOException {
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.outputPath = outputPath;
        this.fileSize = fileSize;
        int bytesRead;
        int current;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(hostName, portNumber);

            byte [] fileByteArray  = new byte [fileSize];
            InputStream is = sock.getInputStream();
            fos = new FileOutputStream(outputPath);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(fileByteArray,0,fileByteArray.length);
            current = bytesRead;

            do {
                bytesRead =
                        is.read(fileByteArray, current, (fileByteArray.length-current));
                if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bos.write(fileByteArray, 0 , current);
            bos.flush();
        }
        finally {
            if (fos != null) fos.close();
            if (bos != null) bos.close();
            if (sock != null) sock.close();
        }
    }
}
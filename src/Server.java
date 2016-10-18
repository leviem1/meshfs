/**
 * Created by Levi Muniz on 10/16/16.
 */
import java.net.*;

public class Server {

    public void startServer(String file, int port, int bufferSize[]) {
        try (
                ServerSocket xmlServer = new ServerSocket(port);
                Socket client = xmlServer.accept()
        ) {
        //code goes here...
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

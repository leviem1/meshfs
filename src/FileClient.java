/**
 * Created by Levi Muniz on 12/6/16.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * The FileClient class handles connecting to
 * other servers in the cluster
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

public final class FileClient {

    /*

    private static void processRequest(Socket client, String request) throws IOException {

        String[] requestParts = request.split("\\|");
        if (requestParts[0].equals("101")) {            //101:Get file
            out.println("201");
            sendFile(requestParts[1]);

        } else if (requestParts[0].equals("102")) {     //102:Post file
            out.println("201");
            receiveFile(requestParts[1], out);

        } else if (requestParts[0].equals("103")) {     //103:Move file (virtual only)
            out.println("201");
            //moveFile(requestParts[1], requestParts[2]);

        } else if (requestParts[0].equals("104")) {     //104:Copy file (virtual only)
            out.println("201");
            //copyFile(requestParts[1], requestParts[2]);

        } else if (requestParts[0].equals("105")) {     //105:Delete file (virtual and physical)
            out.println("201");
            //deleteFile(requestParts[1], requestParts[2]);

        } else if (requestParts[0].equals("106")) {     //106:Make directory (virtual)
            out.println("201");
            //makeDir(requestParts[1]);

        } else if (requestParts[0].equals("107")) {     //107:Get report
            out.println("201");
            sendReport(out);

        } else if (requestParts[0].equals("108")) {     //108:Post report
            out.println("201");
            //receiveReport(requestParts[1]);

        } else if (requestParts[0].equals("109")) {     //109:Ping



        } else if (requestParts[0].equals("110")) {     //110:Bind
            out.println("201");
            //bindClient(requestParts[1], requestParts[2]);
        }
    }
    */

    /**
     * This method is used to ping a server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port the port of the server to connect to
     * @throws java.io.IOException on error connecting
     */

    public static boolean ping(String serverAddress, int port) throws IOException{
        Socket client =  new Socket(serverAddress, port);
        client.setSoTimeout(1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try {
            out.println("109\n");
            String response = input.readLine();
            client.close();

            if (response.equals("201")) {
                return true;
            } else {
                return false;
            }

        } catch (SocketTimeoutException ste) {
            client.close();
            return false;
        }
    }
}
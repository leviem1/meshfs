/**
 * Created by Levi Muniz on 12/6/16.
 */

import java.io.*;
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
    public static int timeout;

    /*
        } else if (requestParts[0].equals("103")) {     //103:Move file (virtual only)

        } else if (requestParts[0].equals("104")) {     //104:Copy file (virtual only)

        } else if (requestParts[0].equals("105")) {     //105:Delete file (virtual and physical)

        } else if (requestParts[0].equals("106")) {     //106:Make directory (virtual)
    */

    /**
     * This method is used to ping a server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port the port of the server to connect to
     * @return true on success, false on failure
     */

    public static boolean ping(String serverAddress, int port) {
        try {
            Socket client = new Socket(serverAddress, port);
            try{
                if(MeshFS.properties.getProperty("timeout") != null){
                    timeout = Integer.parseInt(MeshFS.properties.getProperty("timeout"));
                }
                else{
                    timeout = 5;
                }
            }catch(NullPointerException npe){}
            client.setSoTimeout(timeout * 1000);
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out.println("109\n");

            if (input.readLine().trim().equals("201")) {
                client.close();
                return true;
            } else {
                client.close();
                return false;
            }
        } catch (IOException ioe) {
            return false;
        }
    }

    /**
     * This method is used to generate and send a report to a server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port the port of the server to connect to
     * @throws IOException on error connecting
     */

    public static void sendReport(String serverAddress, int port) throws IOException {
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("108\n");

            if (input.readLine().trim().equals("201")) {
                out.println(Reporting.generate() + "\n");
            }
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    /**
     * This method is used to request a report from a server.
     *
     * @param serverAddress the IP address of the server to connect to
     * @param port the port of the server to connect to
     * @throws IOException on error connecting
     */

    public static void receiveReport(String serverAddress, int port) throws IOException {
        String reportPart;
        String reportFull = "";
        Socket client = new Socket(serverAddress, port);
        client.setSoTimeout(Integer.parseInt(MeshFS.properties.getProperty("timeout")) * 1000);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        out.println("107\n");

        if (input.readLine().trim().equals("201")) {
            while (true) {
                reportPart = input.readLine();
                if ((reportPart == null) || (reportPart.equals("\n"))) break;
                reportFull = reportFull + reportPart + "\n";
            }
            reportFull = reportFull.trim();
            JSONManipulator.writeJSONObject("manifest.json", Reporting.splitter(reportFull));
        }
    }

    public static void duplicateFile(String serverAddress, int port, String currFile) throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        try{
            if(MeshFS.properties.getProperty("timeout") != null){
                timeout = Integer.parseInt(MeshFS.properties.getProperty("timeout"));
            }
            else{
                timeout = 5;
            }
        }catch(NullPointerException npe){}
        client.setSoTimeout(timeout * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("104|" + currFile + "\n");

            if ((response = input.readLine().trim()).equals("201")) {
            } else {
                System.err.println(response);
            }

        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    public static void moveFile(String serverAddress, int port, String currFile, String destFile) throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        try{
            if(MeshFS.properties.getProperty("timeout") != null){
                timeout = Integer.parseInt(MeshFS.properties.getProperty("timeout"));
            }
            else{
                timeout = 5;
            }
        }catch(NullPointerException npe){}
        client.setSoTimeout(timeout * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("103|" + currFile + "|" + destFile + "\n");

            if ((response = input.readLine().trim()).equals("201")) {
            } else {
                System.err.println(response);
            }

        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    public static void deleteFile(String serverAddress, int port, String currFile) throws IOException {
        String response;
        Socket client = new Socket(serverAddress, port);
        try{
            if(MeshFS.properties.getProperty("timeout") != null){
                timeout = Integer.parseInt(MeshFS.properties.getProperty("timeout"));
            }
            else{
                timeout = 5;
            }
        }catch(NullPointerException npe){}
        client.setSoTimeout(timeout * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        try {
            out.println("105|" + currFile + "\n");
            if ((response = input.readLine().trim()).equals("201")) {
            } else {
                System.err.println(response);
            }
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }



    public static void sendFile(String serverAddress, int port, String filepath) throws IOException {
        Socket client = new Socket(serverAddress, port);
        try{
            if(MeshFS.properties.getProperty("timeout") != null){
                timeout = Integer.parseInt(MeshFS.properties.getProperty("timeout"));
            }
            else{
                timeout = 5;
            }
        }catch(NullPointerException npe){}
        client.setSoTimeout(timeout * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            out.println("102|" + (new File(filepath)).getName() + "\n");

            if (input.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];

                FileInputStream fis = new FileInputStream(filepath);

                while ((br = fis.read(data, 0, data.length)) != -1) {
                    dos.write(data, 0, br);
                    dos.flush();
                }

                fis.close();
                dos.close();
            }
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    @SuppressWarnings( "deprecation" )
    public static void receiveFile(String serverAddress, int port, String fileName) throws IOException {
        Socket client = new Socket(serverAddress, port);
        try{
            if(MeshFS.properties.getProperty("timeout") != null){
                timeout = Integer.parseInt(MeshFS.properties.getProperty("timeout"));
            }
            else{
                timeout = 5;
            }
        }catch(NullPointerException npe){}
        client.setSoTimeout(timeout * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(client.getInputStream());

        try {
            out.println("101|" + fileName + "\n");

            if (dis.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];
                FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + fileName);

                while ((br = dis.read(data, 0, data.length)) != -1) {
                    fos.write(data, 0, br);
                    fos.flush();
                }

                fos.close();
                dis.close();
            }

        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

    @SuppressWarnings( "deprecation" )
    public static void receiveFile(String serverAddress, int port, String fileName, String fileOut) throws IOException {
        Socket client = new Socket(serverAddress, port);
        try{
            if(MeshFS.properties.getProperty("timeout") != null){
                timeout = Integer.parseInt(MeshFS.properties.getProperty("timeout"));
            }
            else{
                timeout = 5;
            }
        }catch(NullPointerException npe){}
        client.setSoTimeout(timeout * 1000);
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        DataInputStream dis = new DataInputStream(client.getInputStream());

        try {
            out.println("101|" + fileName + "\n");

            if (dis.readLine().trim().equals("201")) {
                int br;
                byte[] data = new byte[4096];

                FileOutputStream fos = new FileOutputStream(fileOut);

                while ((br = dis.read(data, 0, data.length)) != -1){
                    fos.write(data, 0, br);
                    fos.flush();
                }

                fos.close();
                dis.close();
            }
        } catch (SocketTimeoutException ste) {
            client.close();
        }
    }

}

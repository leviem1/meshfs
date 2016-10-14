/**
 * Created by Levi Muniz on 10/13/16.
 */

import java.io.*;

public class Reporting {

    private String os;

    private void Reporting() {
        os = System.getProperty("os.name");
    }

    public long getSystemStorage() {

        File file = null;

        if (os.startsWith("Windows")) {
            file = new File("c:");
        } else {
            file = new File("\\");
        }
        return file.getUsableSpace();
    }
}

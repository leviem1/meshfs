import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by markhedrick on 5/10/17.
 */
class Crypt {
    static String generateEncryptedPass(String username, String password) {
        MessageDigest messageDigest = null;

        StringBuilder passwordBuilder = new StringBuilder(password);
        for (int x = 0; x < username.length() - 1; x += 2) {
            try {
                passwordBuilder.append(username.charAt(x));
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        password = passwordBuilder.toString();

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        assert messageDigest != null;
        messageDigest.update(password.getBytes(), 0, password.length());
        return new BigInteger(1, messageDigest.digest()).toString(256);
    }

    static void writeAuthFile(ArrayList<UserAccount> accountsArrayList) {
        try {
            File repo = new File(MeshFS.properties.getProperty("repository"));
            if (!repo.getAbsoluteFile().exists()) {
                repo.mkdirs();
            }
            FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + ".auth");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(accountsArrayList);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


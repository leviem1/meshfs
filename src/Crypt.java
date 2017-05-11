import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by markhedrick on 5/10/17.
 */
class Crypt {
    public static String generateEncryptedPass(String username, String password) {
        MessageDigest messageDigest = null;

        for (int x = 0; x < username.length() - 1; x += 2) {
            try {
                password += username.charAt(x);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }

        try {
            messageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        assert messageDigest != null;
        messageDigest.update(password.getBytes(), 0, password.length());
        return new BigInteger(1, messageDigest.digest()).toString(256);
    }

    public static void writeAuthFile(ArrayList<UserAccounts> accountsArrayList) {
        try {
            FileOutputStream fos = new FileOutputStream(MeshFS.properties.getProperty("repository") + ".auth");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(accountsArrayList);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


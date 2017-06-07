import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * The Crypt is a window responsible for letting users manipulate and mange their
 * files across MeshFS
 *
 * @author Mark Hedrick
 * @version 1.0.0
 */

class Crypt {

    /**
     * The generateEncryptedPass method is the algorithm used to generate the
     * encrypted hash of a user password
     *
     * @param username The username of the account to generate a password for
     * @param password The password of the account to generate a password for
     */

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
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        assert messageDigest != null;
        messageDigest.update(password.getBytes(), 0, password.length());

        return new BigInteger(1, messageDigest.digest()).toString(512);
    }

    /**
     * The writeAuthFile method created the authentication file used on the server
     *
     * @param accountsArrayList The list of UserAccount objects to write out in
     *                          the authentication file
     */

    @SuppressWarnings("ResultOfMethodCallIgnored")
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


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by markhedrick on 5/10/17.
 */
class Crypt {
    public static String generateEncryptedAuth(String username, String password) {
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

    private String generateFileKey(String username, String password, String uuid){
        generateEncryptedAuth(password, password);
        return "x";
    }

}

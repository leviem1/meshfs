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

    public static String generateFileKey(String username, String password, String uuid) {
        String key1 = generateEncryptedAuth(username, username);
        String key2 = generateEncryptedAuth(password, password);
        String key3 = generateEncryptedAuth(uuid, uuid);
        String key4 = generateEncryptedAuth(key1, key1);
        String key5 = generateEncryptedAuth(key2, key2);
        String key6 = generateEncryptedAuth(key3, key3);
        String key7 = generateEncryptedAuth(key4, key5);
        String key8 = generateEncryptedAuth(key6, key7);
        String key9 = generateEncryptedAuth(key7, key8);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(key9, 8, key9.length());
        stringBuilder.append(key4, 3, key4.length());
        stringBuilder.append(key8, 7, key8.length());
        stringBuilder.append(key5, 4, key5.length());
        stringBuilder.append(key7, 6, key7.length());
        stringBuilder.append(key6, 5, key6.length());

        return stringBuilder.toString();
    }

}

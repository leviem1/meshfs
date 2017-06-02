/**
 * Created by Levi Muniz on 6/1/17.
 */

class IncorrectCredentialException extends Exception {
    IncorrectCredentialException() {
        super("Incorrect username or password");
    }
}

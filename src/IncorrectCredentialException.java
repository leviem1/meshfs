/**
 * @author Levi Muniz
 */

class IncorrectCredentialException extends Exception {
    IncorrectCredentialException() {
        super("Incorrect username or password");
    }
}

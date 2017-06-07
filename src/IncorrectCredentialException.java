/**
 * This exception occurs on credential mismatch
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

class IncorrectCredentialException extends Exception {

    /**
     * Creates the IncorrectCredentialException.
     */

    IncorrectCredentialException() {
        super("Incorrect username or password");
    }
}

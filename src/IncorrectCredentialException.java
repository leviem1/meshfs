/**
 * This exception occurs on credential mismatch
 *
 * @author Levi Muniz
 */

class IncorrectCredentialException extends Exception {

    /**
     * Creates the IncorrectCredentialException.
     */

    IncorrectCredentialException() {
        super("Incorrect username or password");
    }
}

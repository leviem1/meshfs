import java.util.Arrays;
import java.util.List;

/**
 * This exception is thrown when a request does not exist or is invalid
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

class MalformedRequestException extends Exception {

    private List<String> messageParts;

    /**
     * Creates the MalformedRequestException.
     *
     * @param message   the server's response
     */

    MalformedRequestException(String message) {
        super("Request did not exist or was invalid");
        messageParts = Arrays.asList(message.split(";"));
    }

    /**
     * This method returns the reason for the MalformedRequestException
     *
     * @return a list of strings with the request (index 0)
     *         and the cause (index 1)
     */

    List<String> getReason() {
        return messageParts;
    }
}

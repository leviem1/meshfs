import java.util.Arrays;
import java.util.List;

/**
 * The MalformedRequestException class is an exception thrown during
 *
 * @author Levi Muniz
 * @version 1.0.0
 */
class MalformedRequestException extends Exception {

    private List<String> messageParts;

    MalformedRequestException(String message) {
        messageParts = Arrays.asList(message.split("\\|"));
    }

    List<String> getReason() {
        return messageParts;
    }
}

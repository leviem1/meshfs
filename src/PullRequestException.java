/**
 * The PullRequestException class is an exception thrown when
 * a full file cannot be restored with the files on the system
 *
 * @author Aaron Duran
 * @version 1.0.0
 */

class PullRequestException extends Exception {
    PullRequestException() {
        super("Cannot reconstruct file");
    }
}

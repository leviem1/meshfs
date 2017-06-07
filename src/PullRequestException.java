/**
 * @author Aaron Duran
 */

class PullRequestException extends Exception {
    PullRequestException() {
        super("Cannot reconstruct file");
    }
}

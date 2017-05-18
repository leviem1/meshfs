/**
 * Created by Aaron Duran on 5/16/2017.
 */
class PullRequestException extends Exception {
    PullRequestException() {
        super("Cannot reconstruct file");
    }
}

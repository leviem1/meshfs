/**
 * This exception is thrown on an MD5 mismatch of transferred files
 *
 * @author Levi Muniz
 * @version 1.0.0
 */

class FileTransferException extends Exception {

    /**
     * Creates the FileTransferException.
     */

    FileTransferException() {
        super("File transfer failed");
    }

}

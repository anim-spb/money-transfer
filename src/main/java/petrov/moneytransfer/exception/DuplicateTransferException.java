package petrov.moneytransfer.exception;

/**
 *
 * @author petrov
 */
@SuppressWarnings("serial")
public class DuplicateTransferException extends RuntimeException {

    public DuplicateTransferException(String message) {
        super(message);
    }
}
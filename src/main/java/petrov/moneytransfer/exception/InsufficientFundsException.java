package petrov.moneytransfer.exception;

/**
 *
 * @author petrov
 */
@SuppressWarnings("serial")
public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}

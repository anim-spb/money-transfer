package petrov.moneytransfer.exception;

/**
 *
 * @author petrov
 */
@SuppressWarnings("serial")
public class DaoException extends RuntimeException {

    public DaoException(String message) {
        super(message);
    }
}

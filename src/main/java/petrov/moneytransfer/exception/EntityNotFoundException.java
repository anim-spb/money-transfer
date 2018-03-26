package petrov.moneytransfer.exception;

/**
 *
 * @author petrov
 */
@SuppressWarnings("serial")
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}

package petrov.moneytransfer.exception.handler.error;

/**
 *
 * @author petrov
 */
public class ErrorStatus {
    
    private final int status;
    
    private final String message;

    public ErrorStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

package petrov.moneytransfer.model;

/**
 *
 * @author petrov
 */
public class TransferResult {
    
    public static final TransferResult COMPLETED = new TransferResult(TransferState.COMPLETED);
    
    private final TransferState state;   
    private final TransferStateReason reason;

    public TransferResult(TransferState state) {
        this.state = state;
        this.reason = null;
    }

    public TransferResult(TransferState state, TransferStateReason reason) {
        this.state = state;
        this.reason = reason;
    }

    public TransferState getState() {
        return state;
    }

    public TransferStateReason getReason() {
        return reason;
    }    
}

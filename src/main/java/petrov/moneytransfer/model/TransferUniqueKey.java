package petrov.moneytransfer.model;

import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author petrov
 */
public class TransferUniqueKey {
    
    private final String requestId;   
    private final String fromAccountId;   
    private final String toAccountId;   

    public TransferUniqueKey(String requestId, String fromAccountId, String toAccountId) {
        this.requestId = requestId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }  
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(requestId)
                .append(fromAccountId)
                .append(toAccountId)
                .build();
    }

    @Override
    public boolean equals(Object obj) {       
        if (!(obj instanceof TransferUniqueKey)) {
            return false;
        }
        
        TransferUniqueKey other = (TransferUniqueKey) obj;
        return (Objects.equals(requestId, other.requestId)
                && Objects.equals(fromAccountId, other.requestId)
                && Objects.equals(toAccountId, other.requestId) );       
    }  
}

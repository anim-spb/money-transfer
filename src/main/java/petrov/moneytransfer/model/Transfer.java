package petrov.moneytransfer.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author petrov
 */
public class Transfer {
    @Null
    private String id;
    @NotNull
    private String requestId;  
    @NotNull
    private String fromAccountId;   
    @NotNull
    private String toAccountId;   
    @NotNull
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal amount;
    @Null
    private Instant createdAt;
    @Null
    private TransferState state;
    @Null
    private TransferStateReason stateReason;

    public Transfer() {
    }
    
    public Transfer(Transfer transfer) {
        this.id = transfer.id;
        this.requestId = transfer.requestId;
        this.fromAccountId = transfer.fromAccountId;
        this.toAccountId = transfer.toAccountId;
        this.amount = transfer.amount;
        this.createdAt = transfer.createdAt;
        this.state = transfer.state;
        this.stateReason = transfer.stateReason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public TransferState getState() {
        return state;
    }

    public void setState(TransferState state) {
        this.state = state;
    }

    public TransferStateReason getStateReason() {
        return stateReason;
    }
    
    public void setStateReason(TransferStateReason stateReason) {
        this.stateReason = stateReason;
    }
    
    public Transfer copy() {
        return new Transfer(this);
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(requestId)
                .append(fromAccountId)
                .append(toAccountId)
                .build();
    }

    @Override
    public boolean equals(Object obj) {       
        if (!(obj instanceof Transfer)) {
            return false;
        }
        
        Transfer other = (Transfer) obj;
        return (Objects.equals(id,other.id) 
                && Objects.equals(requestId, other.requestId)
                && Objects.equals(fromAccountId, other.fromAccountId)
                && Objects.equals(toAccountId, other.toAccountId)
                && Objects.equals(amount, other.amount)
                && Objects.equals(createdAt, other.createdAt)
                && Objects.equals(state, other.state)
                && Objects.equals(stateReason, other.stateReason));      
    } 
}

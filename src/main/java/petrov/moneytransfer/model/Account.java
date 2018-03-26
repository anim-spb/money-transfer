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
public class Account {
    @Null
    private String id;
    @NotNull
    private String owner;
    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal balance; 
    @Null
    private Instant createdAt;

    public Account() {
    }
    
    public Account(Account account) {
        this.id = account.id;
        this.owner = account.owner;
        this.balance = account.balance;
        this.createdAt = account.createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }   
    
    public void withdraw(BigDecimal amount) {
        balance = balance.subtract(amount);
    }
    
    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }
    
    public Account copy() {
        return new Account(this);
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .append(owner)
                .append(balance)
                .append(createdAt)
                .build();
    }

    @Override
    public boolean equals(Object obj) {       
        if (!(obj instanceof Account)) {
            return false;
        }
        
        Account other = (Account) obj;
        return (Objects.equals(id,other.id) 
                && Objects.equals(owner, other.owner)
                && Objects.equals(balance, other.balance)
                && Objects.equals(createdAt, other.createdAt));      
    } 
    
    public static class LockPair {
    
        private final Object first;
        private final Object second;

        public LockPair(Object first, Object second) {
            this.first = first;
            this.second = second;
        }

        public Object getFirst() {
            return first;
        }

        public Object getSecond() {
            return second;
        }
    }
}

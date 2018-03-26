package petrov.moneytransfer.service;

import java.math.BigDecimal;
import java.util.List;
import petrov.moneytransfer.model.Account;

/**
 *
 * @author petrov
 */
public interface AccountService {
    
    Account createAccount(Account account);   
    
    Account getAccount(String id);
     
    List<Account> getAccounts(); 
    
    void withdraw(Account account, BigDecimal amount);
    
    void deposit(Account account, BigDecimal amount);
       
    Account.LockPair getLocksProperOrder(Account accountOne, Account accountTwo);
}

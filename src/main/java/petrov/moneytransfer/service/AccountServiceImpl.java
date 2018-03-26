package petrov.moneytransfer.service;

import com.google.common.collect.MapMaker;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import javax.inject.Inject;
import petrov.moneytransfer.dao.AccountDao;
import petrov.moneytransfer.exception.EntityNotFoundException;
import petrov.moneytransfer.exception.InsufficientFundsException;
import petrov.moneytransfer.model.Account;

/**
 *
 * @author petrov
 */
public class AccountServiceImpl implements AccountService{

    private final ConcurrentMap<String,Object> accountLocks = new MapMaker().weakValues().makeMap();  
    
    @Inject
    private AccountDao accountDao;
    
    @Override
    public Account createAccount(Account account) {
        account.setCreatedAt(Instant.now());
        String accountId = accountDao.save(account);
            
        return accountDao.getById(accountId);
    }
    
    @Override
    public Account getAccount(String id) {
        Account account = accountDao.getById(id);
        if (account == null) {
            throw new EntityNotFoundException("Account with id = " + id + " is not found");
        }
        
        return account.copy();
    }   
    
    @Override
    public List<Account> getAccounts() {
        return accountDao.getAll();
    }
    
    @Override
    public Account.LockPair getLocksProperOrder(Account accountOne, Account accountTwo) {
        boolean accountOneLess = (accountOne.getId().compareTo(accountOne.getId()) < 0);
        Object fromLock = getLock(accountOne);
        Object toLock = getLock(accountTwo);
        Object first = (accountOneLess) ? fromLock : toLock;
        Object second = (!accountOneLess) ? toLock : fromLock;
                        
        return new Account.LockPair(first, second);      
    }
    
    @Override
    public void withdraw(Account account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Cannot withdraw amount "
                    + "(" + amount + ") from account (" + account.getId() + ")."
                    + " Insufficient funds");
        }
        
        account.withdraw(amount);
        accountDao.update(account);
    }
    
    @Override
    public void deposit(Account account, BigDecimal amount) {
        account.deposit(amount);
        accountDao.update(account);
    }
    
    private Object getLock(Account account) {
        Object newLock = new Object();
        Object oldLock = accountLocks.putIfAbsent(account.getId(), newLock);
        
        return (oldLock == null) ? newLock : oldLock;
    }      
}

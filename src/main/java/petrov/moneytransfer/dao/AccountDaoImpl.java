package petrov.moneytransfer.dao;

import petrov.moneytransfer.dao.datasource.DataSourceProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import petrov.moneytransfer.exception.DaoException;
import petrov.moneytransfer.model.Account;

/**
 *
 * @author petrov
 */
public class AccountDaoImpl implements AccountDao {
   
    private final Map<String,Account> dataSource;

    @Inject public AccountDaoImpl(DataSourceProvider dsProvider) {
        this.dataSource = dsProvider.getAccounts();
    }  
   
    @Override
    public String save(Account account) {
        return save(account, true);
    } 

    @Override
    public void update(Account account) {
        if (getById(account.getId()) == null) {
            throw new DaoException("Cannot update account. No such id: " + account.getId());
        }
        save(account, false);
    }
    
    @Override
    public Account getById(String id) {
        Account account = dataSource.get(id);
        return (account == null) ? null : account.copy();
    }

    @Override
    public List<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        
        dataSource.entrySet().forEach((entry) -> {
            accounts.add(entry.getValue().copy());
        });
        
        return accounts;
    }
    
    private String save(Account account, boolean generateKey) {
       if (account == null) return null;
       Account savingAccount = account.copy(); 

       if (generateKey) 
           savingAccount.setId(UUID.randomUUID().toString());
       dataSource.put(savingAccount.getId(), savingAccount);
       return savingAccount.getId();
    }
}

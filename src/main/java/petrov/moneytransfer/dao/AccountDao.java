package petrov.moneytransfer.dao;

import java.util.List;
import petrov.moneytransfer.model.Account;

/**
 *
 * @author petrov
 */
public interface AccountDao {   
    
    String save(Account account);
    
    void update(Account account);
    
    Account getById(String id);
    
    List<Account> getAll();              
}

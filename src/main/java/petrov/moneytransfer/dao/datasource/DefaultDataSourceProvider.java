package petrov.moneytransfer.dao.datasource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import petrov.moneytransfer.model.Account;
import petrov.moneytransfer.model.Transfer;

/**
 *
 * @author petrov
 */
public class DefaultDataSourceProvider implements DataSourceProvider {
    
    protected Map<String,Account> accounts = new ConcurrentHashMap<>();   
    protected Map<String,Transfer> transfers = new ConcurrentHashMap<>();

    public DefaultDataSourceProvider() {
    }
         
    @Override
    public Map<String,Account> getAccounts() {
        return accounts;        
    }
    
    @Override
    public Map<String,Transfer> getTransfers() {
        return transfers;        
    }  
}

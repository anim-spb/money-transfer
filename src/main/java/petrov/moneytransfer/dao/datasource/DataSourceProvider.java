package petrov.moneytransfer.dao.datasource;

import java.util.Map;
import petrov.moneytransfer.model.Account;
import petrov.moneytransfer.model.Transfer;

/**
 *
 * @author petrov
 */
public interface DataSourceProvider {

    Map<String,Account> getAccounts();
    
    Map<String,Transfer> getTransfers();   
}

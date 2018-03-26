package petrov.moneytransfer.dao;

import java.util.List;
import petrov.moneytransfer.model.Transfer;

/**
 *
 * @author petrov
 */
public interface TransferDao {
    
    String save(Transfer transfer);
    
    void update(Transfer Transfer);
        
    Transfer getById(String id);
    
    Transfer findBy(String requestId, String fromAccountId, String toAccountId);
    
    List<Transfer> getAll();          
}

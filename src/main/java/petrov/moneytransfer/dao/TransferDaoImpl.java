package petrov.moneytransfer.dao;

import petrov.moneytransfer.dao.datasource.DataSourceProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import petrov.moneytransfer.exception.DaoException;
import petrov.moneytransfer.model.Transfer;

/**
 *
 * @author petrov
 */
public class TransferDaoImpl implements TransferDao {

    private final Map<String,Transfer> dataSource;

    @Inject 
    public TransferDaoImpl(DataSourceProvider dsProvider) {
        this.dataSource = dsProvider.getTransfers();
    }  

    @Override
    public Transfer getById(String id) {
        Transfer transfer = dataSource.get(id);
        return (transfer == null) ? null : transfer.copy();
    }

    @Override
    public String save(Transfer transfer) {
        return save(transfer, true);
    }  
    
    @Override
    public void update(Transfer transfer) {
        if (getById(transfer.getId()) == null) {
            throw new DaoException("Cannot update transfer. No such id: " + transfer.getId());
        }
        save(transfer, false);
    }

    @Override
    public Transfer findBy(String requestId, String fromAccountId, String toAccountId) {
        for (Transfer transfer : dataSource.values()) {
            if (requestId.equals(transfer.getRequestId()) 
                && fromAccountId.equals(transfer.getFromAccountId())
                && toAccountId.equals(transfer.getToAccountId()) )
                return transfer.copy();
        }
        
        return null;
    } 

    @Override
    public List<Transfer> getAll() {
        List<Transfer> transfers = new ArrayList<>();
        
        dataSource.entrySet().forEach((entry) -> {
            transfers.add(entry.getValue().copy());
        });
        
        return transfers;
    }
    
    private String save(Transfer transfer, boolean generateKey) {
        if (transfer == null) return null;
        Transfer savingTransfer = transfer.copy(); 
        
        if (generateKey) 
            savingTransfer.setId(UUID.randomUUID().toString());
        dataSource.put(savingTransfer.getId(), savingTransfer);
        return savingTransfer.getId();
    }
}

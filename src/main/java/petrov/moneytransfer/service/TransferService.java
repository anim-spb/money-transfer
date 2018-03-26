package petrov.moneytransfer.service;

import java.util.List;
import petrov.moneytransfer.model.Transfer;

/**
 *
 * @author petrov
 */
public interface TransferService {
        
    Transfer createTransfer(Transfer transfer);
    
    Transfer getTransfer(String id);
    
    List<Transfer> getTransfers();     
}

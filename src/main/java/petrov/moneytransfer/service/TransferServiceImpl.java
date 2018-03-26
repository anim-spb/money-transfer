package petrov.moneytransfer.service;

import com.google.common.collect.MapMaker;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import javax.inject.Inject;
import petrov.moneytransfer.dao.TransferDao;
import petrov.moneytransfer.exception.DuplicateTransferException;
import petrov.moneytransfer.exception.EntityNotFoundException;
import petrov.moneytransfer.model.Account;
import petrov.moneytransfer.model.Transfer;
import petrov.moneytransfer.model.TransferResult;
import petrov.moneytransfer.model.TransferState;
import petrov.moneytransfer.model.TransferStateReason;
import petrov.moneytransfer.model.TransferUniqueKey;

/**
 *
 * @author petrov
 */
public class TransferServiceImpl implements TransferService{

    @Inject
    private TransferDao transferDao;
    @Inject
    private AccountService accountService;
    
    private final ConcurrentMap<TransferUniqueKey,Object> transferLocks = new MapMaker().weakValues().makeMap();     
      
    @Override
    public Transfer createTransfer(Transfer transfer) {
        synchronized(getLock(transfer)) {
            Transfer duplicate = transferDao.findBy(transfer.getRequestId(), 
                transfer.getFromAccountId(), 
                transfer.getToAccountId());
            
            if (duplicate != null) {
                throw new DuplicateTransferException("Duplicate transfer:"
                    + "fromAccountId = " + transfer.getFromAccountId() + ", "
                    + "toAccountId = " + transfer.getToAccountId() + ", "
                    + "requestId = " + transfer.getRequestId());
            }
            
            transfer.setCreatedAt(Instant.now());
            transfer.setState(TransferState.PROCESSING);
            String transferId = transferDao.save(transfer);
            transfer = transferDao.getById(transferId);
        }         
        
        TransferResult result = transfer(
            transfer.getFromAccountId(), 
            transfer.getToAccountId(), 
            transfer.getAmount());

        transfer.setState(result.getState());    
        transfer.setStateReason(result.getReason());
        transferDao.update(transfer);                      
        return transferDao.getById(transfer.getId());                        
    }
    
    @Override
    public Transfer getTransfer(String id) {
        Transfer transfer = transferDao.getById(id);
        if (transfer == null) {
            throw new EntityNotFoundException("Transfer with id = " + id + " is not found");
        }
        
        return transfer.copy();
    }
      
    @Override
    public List<Transfer> getTransfers() {
        return transferDao.getAll();
    }
    
    private TransferResult transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        Account fromAccount;
        Account toAccount;
        try {
            fromAccount = accountService.getAccount(fromAccountId);
            toAccount = accountService.getAccount(toAccountId);
        }
        catch (EntityNotFoundException ex) {
            return new TransferResult(
                TransferState.FAILED, 
                TransferStateReason.NO_SUCH_ACCOUNT);
        }

        Account.LockPair locks = accountService.getLocksProperOrder(fromAccount, toAccount);

        synchronized(locks.getFirst()) {
            synchronized(locks.getSecond()) {
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    return new TransferResult(
                        TransferState.FAILED, 
                        TransferStateReason.INSUFFICIENT_FUNDS);
                }
                accountService.withdraw(fromAccount, amount);
                accountService.deposit(toAccount, amount);

                return TransferResult.COMPLETED;
            }
        }       
    }
    
     private Object getLock(Transfer transfer) {
        Object newLock = new Object();
        Object oldLock = transferLocks.putIfAbsent(
            new TransferUniqueKey(
                transfer.getRequestId(), 
                transfer.getFromAccountId(), 
                transfer.getToAccountId()
            ), newLock);
        
        return (oldLock == null) ? newLock : oldLock;
    } 
}

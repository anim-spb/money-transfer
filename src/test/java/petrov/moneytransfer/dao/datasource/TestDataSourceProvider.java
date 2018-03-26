package petrov.moneytransfer.dao.datasource;

import java.math.BigDecimal;
import java.time.Instant;
import petrov.moneytransfer.model.Account;
import petrov.moneytransfer.model.Transfer;
import petrov.moneytransfer.model.TransferState;
import petrov.moneytransfer.model.TransferStateReason;

/**
 *
 * @author petrov
 */
public class TestDataSourceProvider extends DefaultDataSourceProvider {

    {
        initAccounts();
        initTransfers();     
    }
    
    private void initAccounts() {       
        Account account1 = new Account();
        account1.setId("6f29921b-33ec-4b55-a413-7697b47776f8");
        account1.setOwner("Mandy Moo");
        account1.setCreatedAt(Instant.parse("2017-03-23T15:37:25.00Z"));
        account1.setBalance(new BigDecimal("2000.50"));
                
        Account account2 = new Account();
        account2.setId("c485c061-bf1d-4813-9c8e-9964caf900ce");
        account2.setOwner("Johny Bee");
        account2.setCreatedAt(Instant.parse("2017-03-24T18:29:30.00Z"));
        account2.setBalance(new BigDecimal("3500.50"));     
        
        accounts.put(account1.getId(), account1);
        accounts.put(account2.getId(), account2);
    }
    
    private void initTransfers() {       
        Transfer transfer1 = new Transfer();
        transfer1.setId("2fd959ba-392b-439e-8091-05991b1839af");
        transfer1.setRequestId("564929eb6164");
        transfer1.setFromAccountId("6f29921b-33ec-4b55-a413-7697b47776f8");
        transfer1.setToAccountId("c485c061-bf1d-4813-9c8e-9964caf900ce");
        transfer1.setAmount(new BigDecimal("1250.42"));
        transfer1.setCreatedAt(Instant.parse("2017-03-25T12:37:20.00Z"));      
        transfer1.setState(TransferState.FAILED);
        transfer1.setStateReason(TransferStateReason.INSUFFICIENT_FUNDS);
               
        Transfer transfer2 = new Transfer();
        transfer2.setId("cb2a99a2-1cde-4ed1-addc-ac53c4f3d37f");
        transfer2.setRequestId("9b335bfcbb90");
        transfer2.setFromAccountId("c485c061-bf1d-4813-9c8e-9964caf900ce");
        transfer2.setToAccountId("6f29921b-33ec-4b55-a413-7697b47776f8");
        transfer2.setAmount(new BigDecimal("687.50"));
        transfer2.setCreatedAt(Instant.parse("2017-03-24T13:10:45.00Z"));
        transfer2.setState(TransferState.COMPLETED);
              
        transfers.put(transfer1.getId(), transfer1);
        transfers.put(transfer2.getId(), transfer2);     
    }
}



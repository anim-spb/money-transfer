package petrov.moneytransfer;

import java.math.BigDecimal;
import petrov.moneytransfer.dao.datasource.TestDataSourceProvider;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.test.JerseyTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import petrov.moneytransfer.context.AppConfig;
import petrov.moneytransfer.dao.datasource.DataSourceProvider;
import petrov.moneytransfer.dao.datasource.DefaultDataSourceProvider;
import petrov.moneytransfer.model.Account;
import petrov.moneytransfer.model.Transfer;
import petrov.moneytransfer.model.TransferState;
import petrov.moneytransfer.model.TransferStateReason;

/**
 *
 * @author petrov
 */
public class TransferTest extends JerseyTest {
    
    private static final String PATH = "transfers";
    
    private static DefaultDataSourceProvider dsProvider;
      
    @Override
    protected Application configure() {      
        dsProvider = new TestDataSourceProvider();
   
        return new AppConfig().register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(dsProvider).to(DataSourceProvider.class);
            }
        });
    }
    
    @Test
    public void testTransferCreate() {  
        String fromAccountId = "6f29921b-33ec-4b55-a413-7697b47776f8";
        String toAccountId = "c485c061-bf1d-4813-9c8e-9964caf900ce";
        BigDecimal amount = new BigDecimal("450.25");
        
        Transfer transferSent = new Transfer();
        transferSent.setRequestId("967929eb6164");
        transferSent.setFromAccountId(fromAccountId);
        transferSent.setToAccountId(toAccountId);
        transferSent.setAmount(amount);
        
        Account fromAccountBefore = dsProvider.getAccounts().get(fromAccountId).copy();
        Account toAccountBefore = dsProvider.getAccounts().get(toAccountId).copy();
        
        Response response = target().path(PATH)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(transferSent));        
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        Transfer transferReceived = response.readEntity(Transfer.class);
        assertEquals(transferSent.getRequestId(), transferReceived.getRequestId());
        assertEquals(transferSent.getFromAccountId(), transferReceived.getFromAccountId());
        assertEquals(transferSent.getToAccountId(), transferReceived.getToAccountId());
        assertEquals(transferSent.getAmount(), transferReceived.getAmount());
               
        Account fromAccountAfter = dsProvider.getAccounts().get(fromAccountId);
        Account toAccountAfter = dsProvider.getAccounts().get(toAccountId);
        
        assertEquals(fromAccountBefore.getBalance().subtract(amount), fromAccountAfter.getBalance());
        assertEquals(toAccountBefore.getBalance().add(amount), toAccountAfter.getBalance());       
    }
    
    @Test
    public void testTransferCreateDuplicate() {   
        Transfer transferSent = new Transfer();
        transferSent.setRequestId("967929eb6164");
        transferSent.setFromAccountId("6f29921b-33ec-4b55-a413-7697b47776f8");
        transferSent.setToAccountId("c485c061-bf1d-4813-9c8e-9964caf900ce");
        transferSent.setAmount(new BigDecimal("450.25"));
              
        Response response = target().path(PATH)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(transferSent));        
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        
        Response newResponse = target().path(PATH)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(transferSent.copy())); 
        assertEquals(Response.Status.CONFLICT.getStatusCode(), newResponse.getStatus());
    }
    
    @Test
    public void testTransferCreateInsufficientFunds() {      
        Transfer transferSent = new Transfer();
        transferSent.setRequestId("967929eb6164");
        transferSent.setFromAccountId("6f29921b-33ec-4b55-a413-7697b47776f8");
        transferSent.setToAccountId("c485c061-bf1d-4813-9c8e-9964caf900ce");
        transferSent.setAmount(new BigDecimal("3450.25"));
        
        Response response = target().path(PATH)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(transferSent)); 
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        
        Transfer transferReceived = response.readEntity(Transfer.class);
        assertEquals(transferSent.getRequestId(), transferReceived.getRequestId());
        assertEquals(transferSent.getFromAccountId(), transferReceived.getFromAccountId());
        assertEquals(transferSent.getToAccountId(), transferReceived.getToAccountId());
        assertEquals(transferSent.getAmount(), transferReceived.getAmount()); 
        assertEquals(TransferState.FAILED, transferReceived.getState());
        assertEquals(TransferStateReason.INSUFFICIENT_FUNDS, transferReceived.getStateReason());
    }
    
    @Test
    public void testTransferCreateNoSuchAccount() {      
        Transfer transferSent = new Transfer();
        transferSent.setRequestId("967929eb6164");
        transferSent.setFromAccountId("6f29921b-33ec-4b55-a413-7697b47776f8");
        transferSent.setToAccountId("cccccccc-bf1d-4813-9c8e-9964caf900ce");
        transferSent.setAmount(new BigDecimal("3450.25"));
        
        Response response = target().path(PATH)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(transferSent)); 
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        
        Transfer transferReceived = response.readEntity(Transfer.class);
        assertEquals(transferSent.getRequestId(), transferReceived.getRequestId());
        assertEquals(transferSent.getFromAccountId(), transferReceived.getFromAccountId());
        assertEquals(transferSent.getToAccountId(), transferReceived.getToAccountId());
        assertEquals(transferSent.getAmount(), transferReceived.getAmount());
        assertEquals(TransferState.FAILED, transferReceived.getState());
        assertEquals(TransferStateReason.NO_SUCH_ACCOUNT, transferReceived.getStateReason());
    }
      
    @Test
    public void testTransferGet() {
        String id = "2fd959ba-392b-439e-8091-05991b1839af";
        Transfer transferExp = dsProvider.getTransfers().get(id);
       
        Response response = 
            target().path(PATH + "/" + id).request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        Transfer transferReceived = response.readEntity(Transfer.class);      
        assertEquals(transferExp, transferReceived); 
    }
    
    @Test
    public void testTransfersGet() {      
        Response response = 
            target().path(PATH).request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        
        List<Transfer> transfersReceived = response.readEntity(new GenericType<List<Transfer>>() {});
        assertEquals(new ArrayList<>(dsProvider.getTransfers().values()), transfersReceived);
    }
    
    @Test
    public void testTransferGetNotFound() {
        String id = "aaaaaaaa-33ec-4b55-a413-7697b47776f8";
       
        Response response = 
            target().path(PATH + "/" + id).request().get();
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }   
}

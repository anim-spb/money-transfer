package petrov.moneytransfer;

import petrov.moneytransfer.dao.datasource.TestDataSourceProvider;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import petrov.moneytransfer.context.AppConfig;
import petrov.moneytransfer.dao.datasource.DataSourceProvider;
import petrov.moneytransfer.dao.datasource.DefaultDataSourceProvider;
import petrov.moneytransfer.model.Account;

public class AccountTest extends JerseyTest {
    
    private static final String PATH = "accounts";
    
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
    public void testAccountCreate() {
        Account accSent = new Account();
        accSent.setOwner("John Smith");
        accSent.setBalance(new BigDecimal("1230.55"));
                           
        Response response = target().path(PATH)
                .request(MediaType.APPLICATION_JSON).post(Entity.json(accSent));        
        assertEquals(Status.CREATED.getStatusCode(), response.getStatus());
        
        Account accReceived = response.readEntity(Account.class);
        assertEquals(accSent.getOwner(), accReceived.getOwner());
        assertEquals(accSent.getBalance(), accReceived.getBalance());              
    }
    
    @Test
    public void testAccountGet() {
        String id = "6f29921b-33ec-4b55-a413-7697b47776f8";
        Account accExp = dsProvider.getAccounts().get(id);
       
        Response response = target().path(PATH + "/" + id).request().get();
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        
        Account accReceived = response.readEntity(Account.class);      
        assertEquals(accExp, accReceived); 
    }
    
    @Test
    public void testAccountsGet() {      
        Response response = target().path(PATH).request().get();
        assertEquals(Status.OK.getStatusCode(), response.getStatus());
        
        List<Account> accountsReceived = response.readEntity(new GenericType<List<Account>>() {});
        assertEquals(new ArrayList<>(dsProvider.getAccounts().values()), accountsReceived);
    }
    
    @Test
    public void testAccountGetNotFound() {
        String id = "aaaaaaaa-33ec-4b55-a413-7697b47776f8";
       
        Response response = target().path(PATH + "/" + id).request().get();
        assertEquals(Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }   
}

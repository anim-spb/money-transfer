package petrov.moneytransfer.resource;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import petrov.moneytransfer.model.Account;
import petrov.moneytransfer.service.AccountService;

/**
 *
 * @author petrov
 */
@Path("accounts")
@Singleton
public class AccountResource {
    
    @Inject
    private AccountService accountService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Account get(@PathParam("id") String id) {
        return accountService.getAccount(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Account> get() {
        return accountService.getAccounts();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Valid Account account) {
        Account createdAccount = accountService.createAccount(account);                          
        return Response.status(Response.Status.CREATED).entity(createdAccount)
            .build();
    }
}

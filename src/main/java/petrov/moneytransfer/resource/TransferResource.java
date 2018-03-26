package petrov.moneytransfer.resource;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import petrov.moneytransfer.model.Transfer;
import petrov.moneytransfer.model.TransferState;
import petrov.moneytransfer.service.TransferService;

/**
 *
 * @author petrov
 */
@Path("transfers")
@Singleton
public class TransferResource {
    
    @Inject
    private TransferService transferService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Transfer get(@PathParam("id") String id) {
        return transferService.getTransfer(id);
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Transfer> get() {
        return transferService.getTransfers();
    }
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response post(@Valid Transfer transfer) {        
        transfer = transferService.createTransfer(transfer);
        
        Status status = (transfer.getState() == TransferState.COMPLETED) 
            ? Status.CREATED
            : Status.ACCEPTED;
 
        return Response.status(status).entity(transfer)
            .build();
    }
}

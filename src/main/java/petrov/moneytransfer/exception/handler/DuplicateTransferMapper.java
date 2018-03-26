package petrov.moneytransfer.exception.handler;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import petrov.moneytransfer.exception.DuplicateTransferException;
import petrov.moneytransfer.exception.handler.error.ErrorStatus;

/**
 *
 * @author petrov
 */
@Provider
public class DuplicateTransferMapper implements ExceptionMapper<DuplicateTransferException> {
    
    private static final Logger LOGGER = Logger.getLogger(
            DuplicateTransferMapper.class.getCanonicalName()); 
    
    @Override
    public Response toResponse(DuplicateTransferException ex) {
        LOGGER.log(Level.INFO, ex.getMessage(), ex);
        return Response.status(Status.CONFLICT)
                .entity(new ErrorStatus(Status.CONFLICT.getStatusCode(), ex.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

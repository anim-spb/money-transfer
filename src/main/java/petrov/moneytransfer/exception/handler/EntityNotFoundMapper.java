package petrov.moneytransfer.exception.handler;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import petrov.moneytransfer.exception.EntityNotFoundException;
import petrov.moneytransfer.exception.handler.error.ErrorStatus;

/**
 *
 * @author petrov
 */
@Provider
public class EntityNotFoundMapper implements ExceptionMapper<EntityNotFoundException> {
    
    private static final Logger LOGGER = Logger.getLogger(
            EntityNotFoundMapper.class.getCanonicalName()); 
    
    @Override
    public Response toResponse(EntityNotFoundException ex) {
        LOGGER.log(Level.INFO, ex.getMessage(), ex);
        return Response.status(Status.NOT_FOUND)
                .entity(new ErrorStatus(Status.NOT_FOUND.getStatusCode(), ex.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

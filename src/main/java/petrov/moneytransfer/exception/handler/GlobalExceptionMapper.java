package petrov.moneytransfer.exception.handler;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import petrov.moneytransfer.exception.handler.error.ErrorStatus;

/**
 *
 * @author petrov
 */
@Provider
public class GlobalExceptionMapper  implements ExceptionMapper<Throwable> {
    
    private static final Logger LOGGER = Logger.getLogger(
            GlobalExceptionMapper.class.getCanonicalName()); 
    
    @Override
    public Response toResponse(Throwable ex) {
        LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        return Response.status(Status.NOT_FOUND)
                .entity(new ErrorStatus(Status.INTERNAL_SERVER_ERROR.getStatusCode(), 
                        Status.INTERNAL_SERVER_ERROR.getReasonPhrase()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
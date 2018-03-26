package petrov.moneytransfer.exception.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.commons.lang3.StringUtils;
import petrov.moneytransfer.exception.handler.error.ErrorStatus;

/**
 *
 * @author petrov
 */
@Provider
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException ex) {   
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        List<String> messages = new ArrayList<>();
        violations.forEach((violation) -> {
            String message = 
                String.format("Field: %s. Value: '%s'. Message: %s.", 
                    violation.getPropertyPath(),
                    violation.getInvalidValue(), 
                    violation.getMessage());
            messages.add(message); 
        });
        return Response.status(Status.BAD_REQUEST)
                    .entity(new ErrorStatus(Status.BAD_REQUEST.getStatusCode(), StringUtils.join(messages, "\n")))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
    }
}

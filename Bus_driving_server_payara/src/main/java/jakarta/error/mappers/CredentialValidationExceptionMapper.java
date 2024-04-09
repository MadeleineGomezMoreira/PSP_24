package jakarta.error.mappers;

import domain.exception.CredentialValidationException;
import jakarta.error.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class CredentialValidationExceptionMapper implements ExceptionMapper<CredentialValidationException> {

    public Response toResponse(CredentialValidationException exception) {
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return Response.status(Response.Status.BAD_REQUEST).entity(apiError)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}

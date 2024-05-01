package jakarta.error.mappers;

import domain.exception.CredentialValidationFailedException;
import jakarta.error.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class CredentialValidationFailedExceptionMapper {

    public Response toResponse(CredentialValidationFailedException exception) {
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return Response.status(Response.Status.BAD_REQUEST).entity(apiError)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }

}

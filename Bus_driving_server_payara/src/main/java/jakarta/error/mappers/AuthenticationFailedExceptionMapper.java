package jakarta.error.mappers;

import domain.exception.AuthenticationFailedException;
import jakarta.error.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import java.time.LocalDateTime;

public class AuthenticationFailedExceptionMapper implements ExceptionMapper<AuthenticationFailedException> {

    public Response toResponse(AuthenticationFailedException exception) {
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return Response.status(Response.Status.UNAUTHORIZED).entity(apiError)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}

package jakarta.error.mappers;

import domain.exception.AccountNotActivatedException;
import domain.exception.UnauthorizedAccessException;
import jakarta.error.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class UnauthorizedAccessExceptionMapper implements ExceptionMapper<UnauthorizedAccessException> {

    public Response toResponse(UnauthorizedAccessException exception) {
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return Response.status(Response.Status.UNAUTHORIZED).entity(apiError)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}

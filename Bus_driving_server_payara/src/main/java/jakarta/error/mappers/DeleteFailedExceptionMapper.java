package jakarta.error.mappers;

import domain.exception.DeleteFailedException;
import jakarta.error.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.time.LocalDateTime;

@Provider
public class DeleteFailedExceptionMapper implements ExceptionMapper<DeleteFailedException> {

    public Response toResponse(DeleteFailedException exception) {
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(apiError)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}

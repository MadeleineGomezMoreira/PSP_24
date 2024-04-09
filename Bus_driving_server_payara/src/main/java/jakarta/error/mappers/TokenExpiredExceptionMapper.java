package jakarta.error.mappers;

import domain.exception.TokenExpiredException;
import jakarta.error.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import java.time.LocalDateTime;

public class TokenExpiredExceptionMapper implements ExceptionMapper<TokenExpiredException> {

    public Response toResponse(TokenExpiredException exception) {
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return Response.status(Response.Status.UNAUTHORIZED).entity(apiError)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}

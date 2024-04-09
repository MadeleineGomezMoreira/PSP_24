package jakarta.error.mappers;

import domain.exception.AccountNotActivatedException;
import jakarta.error.ApiError;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

import java.time.LocalDateTime;

public class AccountNotActivatedExceptionMapper implements ExceptionMapper<AccountNotActivatedException> {

    public Response toResponse(AccountNotActivatedException exception) {
        ApiError apiError = new ApiError(exception.getMessage(), LocalDateTime.now());
        return Response.status(Response.Status.UNAUTHORIZED).entity(apiError)
                .type(MediaType.APPLICATION_JSON_TYPE).build();
    }
}

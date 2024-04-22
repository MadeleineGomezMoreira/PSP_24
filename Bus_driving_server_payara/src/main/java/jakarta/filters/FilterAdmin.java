package jakarta.filters;

import common.Constants;
import jakarta.error.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@RoleAdmin
public class FilterAdmin implements ContainerRequestFilter {

    @Context
    private HttpServletRequest request;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String role = (String) request.getSession().getAttribute(Constants.ROLE);
        if (request.getSession().getAttribute(Constants.LOGIN) == null) {
            containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity(ApiError.builder().message(Constants.USER_NOT_LOGGED_IN_ERROR).build())
                    .type(MediaType.APPLICATION_JSON_TYPE).build());

        } else if (!role.equals(Constants.ADMIN)) {
            containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
                    .entity(ApiError.builder().message(Constants.ACCESS_DENIED_ONLY_ADMIN).build())
                    .type(MediaType.APPLICATION_JSON_TYPE).build());
        }
    }
}

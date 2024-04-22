package jakarta.controllers;

import common.Constants;
import domain.dto.LoginDTO;
import domain.usecases.credentials.LoginAndGetRole;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(Constants.EMPTY_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {

    private final LoginAndGetRole loginAndGetRole;


    @Inject
    public LoginController(LoginAndGetRole loginAndGetRole) {
        this.loginAndGetRole = loginAndGetRole;
    }

    @POST
    @Path(Constants.LOGIN_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO credential, @Context HttpServletRequest request) {
        String role = loginAndGetRole.login(credential);

        //to log a user in we can just set a "LOGIN" session attribute to true
        request.getSession().setAttribute(Constants.LOGIN, true);
        request.getSession().setAttribute(Constants.ROLE, role);
        return Response.ok().build();
    }

    @POST
    @Path(Constants.LOGOFF_PATH)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logOff(@Context HttpServletRequest request) {
        //we set the LOGIN attribute to null
        request.getSession().setAttribute(Constants.LOGIN, null);
        //to log a user off we can just invalidate the session (but we do the above as well, given this may not be 100% reliable)
        request.getSession().invalidate();
        return Response.ok().build();
    }
}

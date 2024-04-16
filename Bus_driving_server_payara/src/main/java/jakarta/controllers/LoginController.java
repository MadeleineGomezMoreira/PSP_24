package jakarta.controllers;

import common.Constants;
import domain.dto.LoginDTO;
import domain.model.DriverCredential;
import domain.usecases.credentials.GetCredential;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginController {


    private final GetCredential getCredential;
    private final Pbkdf2PasswordHash passwordHash;

    @Inject
    public LoginController(GetCredential getCredential, Pbkdf2PasswordHash passwordHash) {
        this.getCredential = getCredential;
        this.passwordHash = passwordHash;
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginDTO credential, @Context HttpServletRequest request) {
        DriverCredential trueCredential = getCredential.getCredential(credential.getUsername());

        String inputPassword = credential.getPassword();
        String storedPasswordWithSalt = trueCredential.getPassword();

        boolean isPasswordCorrect = passwordHash.verify(inputPassword.toCharArray(), storedPasswordWithSalt);
        String role = trueCredential.getRole().getRoleName();

        if (isPasswordCorrect) {
            //to log a user in we can just set a "LOGIN" session attribute to true
            request.getSession().setAttribute("LOGIN", true);
            request.getSession().setAttribute("ROLE", role);
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).entity(Constants.AUTHENTICATION_FAILED_PASSWORD_ERROR).build();
        }
    }

    @POST
    @Path("/logoff")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response logOff(@Context HttpServletRequest request) {
        //we set the LOGIN attribute to null
        request.getSession().setAttribute("LOGIN", null);
        //to log a user off we can just invalidate the session (but we do the above as this may not be 100% reliable)
        request.getSession().invalidate();
        return Response.ok().build();
    }
}

package jakarta.controllers;

import common.Constants;
import domain.usecases.credentials.ActivateAccount;
import domain.usecases.email.SendActivationEmail;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path(Constants.ACTIVATE_PATH)
public class ActivateController {

    private final ActivateAccount activate;
    private final SendActivationEmail sendActivationEmail;

    @Inject
    public ActivateController(ActivateAccount activate, SendActivationEmail sendActivationEmail) {
        this.activate = activate;
        this.sendActivationEmail = sendActivationEmail;
    }

    //'/activate'
    @GET
    public Response activateAccount(@QueryParam(Constants.EMAIL) String email, @QueryParam(Constants.CODE) String code) {
        if (activate.activateAccount(email, code)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(Constants.ACTIVATION_LINK_EXPIRED).build();
        }
    }

    //'activate/resend-code'
    @PUT
    @Path(Constants.RESEND_CODE_PATH)
    public Response resendActivationCode(@QueryParam(Constants.EMAIL) String email) {
        try {
            sendActivationEmail.sendEmail(email);
        } catch (MessagingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Constants.FAILED_TO_SEND_EMAIL_ERROR).build();
        }
        return Response.ok(Constants.REGISTRATION_WAS_SUCCESSFUL).build();
    }

}

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
import jakarta.ws.rs.core.MediaType;
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
            //notifies the user that the account was activated
            return Response.ok(Constants.ACTIVATION_WAS_SUCCESSFUL).build();
        } else {
            String htmlContent = "<html><body><h1>Activation Link Expired</h1>"
                    + "<p>The activation link has expired. Click the button below to resend the activation link.</p>"
                    + "<input id='resendButton' type='button' value='Resend Activation Link' onclick='resendActivationLink(this)'/>"
                    + "<script>"
                    + "function resendActivationLink(button) {"
                    + "    button.disabled = true;"
                    + "    var email = '" + email + "';"
                    + "    var xhr = new XMLHttpRequest();"
                    + "    xhr.open('PUT', '/Bus_driving_server_payara-1.0-SNAPSHOT/api/activate/resend-code?email=' + encodeURIComponent(email));"
                    + "    xhr.onload = function() {"
                    + "        if (xhr.status === 200) {"
                    + "            alert('Activation link resent successfully.');"
                    + "        } else {"
                    + "            alert('Failed to resend activation link. Please try again.');"
                    + "            button.disabled = false;"
                    + "        }"
                    + "    };"
                    + "    xhr.send();"
                    + "}"
                    + "</script></body></html>";
            return Response.status(Response.Status.EXPECTATION_FAILED).entity(htmlContent).type(MediaType.TEXT_HTML).build();
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

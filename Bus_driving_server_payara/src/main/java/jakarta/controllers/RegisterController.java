package jakarta.controllers;

import common.Constants;
import domain.dto.RegisterDTO;
import domain.usecases.driver.RegisterDriver;
import domain.usecases.email.SendActivationEmail;
import jakarta.inject.Inject;
import jakarta.mail.MessagingException;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(Constants.REGISTER_PATH)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegisterController {

    private final RegisterDriver save;
    private final SendActivationEmail sendActivationEmail;

    @Inject
    public RegisterController(RegisterDriver save, SendActivationEmail sendActivationEmail) {
        this.save = save;
        this.sendActivationEmail = sendActivationEmail;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegisterDTO driver) {
        if (driver.getEmail() == null || driver.getUsername() == null || driver.getPassword() == null || driver.getFirstName() == null || driver.getLastName() == null || driver.getPhone() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Constants.VALIDATION_FAILED_ERROR)
                    .build();
        }

        String email = driver.getEmail();

        save.registerDriver(driver);

        try {
            sendActivationEmail.sendEmail(email);
        } catch (MessagingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Constants.FAILED_TO_SEND_EMAIL_ERROR).build();
        }

        return Response.ok(Constants.REGISTRATION_WAS_SUCCESSFUL).build();
    }
}


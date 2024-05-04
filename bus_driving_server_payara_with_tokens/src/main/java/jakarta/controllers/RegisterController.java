package jakarta.controllers;

import common.Constants;
import jakarta.model.RegisterDTO;
import domain.usecases.credentials.RegisterAccount;
import domain.usecases.email.SendActivationEmail;
import jakarta.inject.Inject;
import jakarta.model.mappers.JakartaDataMappers;
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

    private final RegisterAccount save;
    private final SendActivationEmail sendActivationEmail;
    private final JakartaDataMappers dataMappers;

    @Inject
    public RegisterController(RegisterAccount save, SendActivationEmail sendActivationEmail, JakartaDataMappers dataMappers) {
        this.save = save;
        this.sendActivationEmail = sendActivationEmail;
        this.dataMappers = dataMappers;
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

        save.registerDriver(dataMappers.mapRegisterDTOToBusDriver(driver));
        sendActivationEmail.sendEmail(email);

        return Response.ok(Constants.REGISTRATION_WAS_SUCCESSFUL).build();
    }
}


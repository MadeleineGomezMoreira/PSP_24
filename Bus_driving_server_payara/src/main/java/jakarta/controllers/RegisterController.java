package jakarta.controllers;

import common.Constants;
import domain.dto.RegisterDTO;
import domain.usecases.driver.RegisterDriver;
import domain.usecases.driver.SaveDriver;
import jakarta.inject.Inject;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RegisterController {

    private final Pbkdf2PasswordHash passwordHash;
    private final RegisterDriver save;

    @Inject
    public RegisterController(Pbkdf2PasswordHash passwordHash, RegisterDriver save) {
        this.passwordHash = passwordHash;
        this.save = save;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(RegisterDTO driver) {
        if (driver.getEmail() == null || driver.getUsername() == null || driver.getPassword() == null || driver.getFirstName() == null || driver.getLastName() == null || driver.getPhone() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Validation failed. Please provide all required fields.")
                    .build();
        }

        String email = driver.getEmail();

        //create activation code
        String urlEncodedActivationCode = generateActivationCode();
        driver.setActivationCode(urlEncodedActivationCode);

        save.registerDriver(driver);

        try {
            generateAndSendEmail(email,
                    Constants.CLICK_LINK_TO_ACTIVATE_ACCOUNT + Constants.ACTIVATE_ACCOUNT_LINK + "email=" + email + "&code=" + urlEncodedActivationCode);
        } catch (MessagingException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Constants.FAILED_TO_SEND_EMAIL_ERROR).build();
        }

        return Response.ok(Constants.REGISTRATION_WAS_SUCCESSFUL).build();
    }

    private void generateAndSendEmail(String recipient, String message) throws MessagingException {
        Properties mailServerProperties;
        Session getMailSession;
        MimeMessage generateMailMessage;

        //Step1

        mailServerProperties = System.getProperties();
        mailServerProperties.put("mail.smtp.port", Integer.parseInt("587"));
        mailServerProperties.put("mail.smtp.auth", "true");
        mailServerProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mailServerProperties.put("mail.smtp.starttls.enable", "true");

        //Step2

        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        generateMailMessage.setSubject(Constants.ACTIVATE_YOUR_ACCOUNT);
        generateMailMessage.setContent(message, "text/html");


        //Step3

        Transport transport = getMailSession.getTransport("smtp");

        //TODO: place these in a properties file (for a safer approach)
        transport.connect("smtp.gmail.com",
                "alumnosdamquevedo@gmail.com",
                "uyhqfbbfmszvuykt");

        transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
        transport.close();
    }

    private String generateActivationCode() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        String encodedActivationCode = Base64.getEncoder().encodeToString(randomBytes);
        return URLEncoder.encode(encodedActivationCode, StandardCharsets.UTF_8);
    }
}


package jakarta.controllers;

import common.Constants;
import domain.model.DriverCredential;
import domain.usecases.credentials.ActivateAccount;
import domain.usecases.credentials.UpdateActivationCode;
import jakarta.inject.Inject;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Properties;

@Path("/activate")
public class ActivateController {

    private final ActivateAccount activate;
    private final UpdateActivationCode updateActivationCode;

    @Inject
    public ActivateController(ActivateAccount activate, UpdateActivationCode updateActivationCode) {
        this.activate = activate;
        this.updateActivationCode = updateActivationCode;
    }

    @PUT
    public Response activateAccount(@QueryParam("email") String email, @QueryParam("code") String code) {
        DriverCredential credential = new DriverCredential(
                email,
                //set as 1 hour more cause for some reason it's 1 hour behind
                LocalDateTime.now().plusHours(1),
                code
        );
        if (activate.activateAccount(credential)) {
            return Response.ok().build();
        } else {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }
    }

    @POST
    public Response resendActivationCode(@QueryParam("email") String email) {

        String urlEncodedActivationCode = generateActivationCode();

        DriverCredential credential = new DriverCredential(
                email,
                LocalDateTime.now().plusHours(1),
                urlEncodedActivationCode
        );

        if (updateActivationCode.updateActivationCode(credential)) {
            try {
                generateAndSendEmail(email,
                        Constants.CLICK_LINK_TO_ACTIVATE_ACCOUNT + Constants.ACTIVATE_ACCOUNT_LINK + "email=" + email + "&code=" + urlEncodedActivationCode);
            } catch (MessagingException e) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Constants.FAILED_TO_SEND_EMAIL_ERROR).build();
            }

            return Response.ok(Constants.REGISTRATION_WAS_SUCCESSFUL).build();
        } else {
            return Response.status(Response.Status.EXPECTATION_FAILED).build();
        }
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

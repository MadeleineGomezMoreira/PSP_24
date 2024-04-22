package domain.usecases.email;

import common.Constants;
import common.config.ConfigSettings;
import domain.usecases.credentials.UpdateActivationCode;
import jakarta.inject.Inject;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

public class SendActivationEmail {

    private final UpdateActivationCode updateActivationCode;
    private final ConfigSettings config;

    @Inject
    public SendActivationEmail(UpdateActivationCode updateActivationCode, ConfigSettings config) {
        this.updateActivationCode = updateActivationCode;
        this.config = config;
    }

    public void sendEmail(String recipient) throws MessagingException {
        Properties mailServerProperties;
        Session getMailSession;
        MimeMessage generateMailMessage;

        String activationCode = generateActivationCode();
        String message = Constants.CLICK_LINK_TO_ACTIVATE_ACCOUNT + Constants.ACTIVATE_ACCOUNT_LINK + Constants.EMAIL_URL + recipient + Constants.CODE_URL + activationCode;

        //first, we'll set the properties for the mail server
        mailServerProperties = System.getProperties();
        mailServerProperties.put(Constants.MAIL_PORT, Constants.MAIL_PORT_NUM);
        mailServerProperties.put(Constants.MAIL_AUTH, Constants.TRUE);
        mailServerProperties.put(Constants.MAIL_SSL_TRUST, Constants.STMP_GMAIL);
        mailServerProperties.put(Constants.MAIL_STARTTLS_ENABLE, Constants.TRUE);

        //then, we'll create the message
        getMailSession = Session.getDefaultInstance(mailServerProperties, null);
        generateMailMessage = new MimeMessage(getMailSession);
        generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        generateMailMessage.setSubject(Constants.ACTIVATE_YOUR_ACCOUNT);
        generateMailMessage.setContent(message, Constants.MESSAGE_TEXT_TYPE);

        //before sending the message, we'll update the activation code in the database
        updateActivationCode.updateActivationCode(recipient, activationCode);

        //finally, we'll send the message
        Transport transport = getMailSession.getTransport(Constants.STMP_PROTOCOL);

        transport.connect(config.getEmailHost(),
                config.getEmailUser(),
                config.getEmailPassword());

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

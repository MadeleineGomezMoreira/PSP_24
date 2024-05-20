package com.example.server.domain.usecases.email;

import com.example.server.common.Constants;
import com.example.server.common.config.ConfigSettings;
import com.example.server.domain.exception.MailMessagingException;
import com.example.server.domain.usecases.credentials.UpdateActivationCode;
import com.example.server.ui.security.di.EmailServiceImpl;
import lombok.RequiredArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@RequiredArgsConstructor
public class SendActivationEmail {

    private final UpdateActivationCode updateActivationCode;
    private final ConfigSettings config;
    public EmailServiceImpl emailService;


    public void sendEmail(String recipient) {
        try {
            String activationCode = generateActivationCode();
            String message = Constants.CLICK_LINK_TO_ACTIVATE_ACCOUNT + Constants.ACTIVATE_ACCOUNT_LINK + Constants.EMAIL_URL + recipient + Constants.CODE_URL + activationCode;

            //before sending the message, we'll update the activation code in the database
            updateActivationCode.updateActivationCode(recipient, activationCode);

            //finally, we'll send the message
            emailService.sendSimpleMessage(recipient, Constants.ACTIVATE_YOUR_ACCOUNT_MAIL_SUBJECT, message);

        } catch(Exception e) {
            throw new MailMessagingException(Constants.FAILED_TO_SEND_EMAIL_ERROR);
        }
    }

    private String generateActivationCode() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        String encodedActivationCode = Base64.getEncoder().encodeToString(randomBytes);
        return URLEncoder.encode(encodedActivationCode, StandardCharsets.UTF_8);
    }
}

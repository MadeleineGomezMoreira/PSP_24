package com.example.server.ui.security.di;

import com.example.server.common.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${spring.mail.host}")
    private String mailHost;
    @Value("${spring.mail.port}")
    private int mailPort;
    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mailPropertyAuth;
    @Value("${spring.mail.properties.mail.transport.protocol}")
    private String mailPropertyTransportProtocol;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String mailPropertyStarttls;
    @Value("${spring.mail.properties.mail.debug}")
    private String mailPropertyDebug;

    @Bean(name = "getJavaMailSender")
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);

        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put(Constants.MAIL_TRANSPORT_PROTOCOL, mailPropertyTransportProtocol);
        props.put(Constants.MAIL_STMP_AUTH, mailPropertyAuth);
        props.put(Constants.MAIL_STMP_STARTTLS, mailPropertyStarttls);
        props.put(Constants.MAIL_DEBUG, mailPropertyDebug);

        return mailSender;
    }

}

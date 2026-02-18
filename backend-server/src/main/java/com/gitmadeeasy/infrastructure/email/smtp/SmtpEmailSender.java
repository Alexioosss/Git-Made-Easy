package com.gitmadeeasy.infrastructure.email.smtp;

import com.gitmadeeasy.usecases.email.EmailSender;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class SmtpEmailSender implements EmailSender {
    private final Session session;
    private final String fromEmailAddress;

    public SmtpEmailSender(String host, int port, String username, String password, String fromEmailAddress) {
        this.fromEmailAddress = fromEmailAddress;

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", String.valueOf(port));

        this.session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    @Override
    public void send(String toEmailAddress, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmailAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmailAddress));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
        } catch(Exception e) {
            throw new RuntimeException("Failed to send SMTP mail: " + e.getMessage(), e);
        }
    }
}
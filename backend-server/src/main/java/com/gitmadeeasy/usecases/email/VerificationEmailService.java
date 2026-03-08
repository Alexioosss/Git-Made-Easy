package com.gitmadeeasy.usecases.email;

import com.gitmadeeasy.usecases.auth.UserIdentityProvider;

public class VerificationEmailService {
    private final UserIdentityProvider userIdentityProvider;
    private final EmailSender emailSender;

    public VerificationEmailService(UserIdentityProvider userIdentityProvider, EmailSender emailSender) {
        this.userIdentityProvider = userIdentityProvider;
        this.emailSender = emailSender;
    }

    public void sendVerificationEmail(String emailAddress, String firstName) {
        String link = this.userIdentityProvider.generateVerificationEmail(emailAddress);
        String body = "Hello " + firstName + ",\n\n" +
                "Please verify your email address using the link provided below:\n\n" +
                link + "\n\n" +
                "If you did not request this, you can safely ignore this email.";
        this.emailSender.send(emailAddress, "Verify your email address", body);
    }
}
package com.gitmadeeasy.usecases.auth;

public class SendVerificationEmail {
    private final UserIdentityProvider userIdentityProvider;
    private final EmailSender emailSender;

    public SendVerificationEmail(UserIdentityProvider userIdentityProvider, EmailSender emailSender) {
        this.userIdentityProvider = userIdentityProvider;
        this.emailSender = emailSender;
    }
}
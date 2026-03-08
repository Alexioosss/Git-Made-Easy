package com.gitmadeeasy.usecases.auth;

import com.gitmadeeasy.entities.users.User;
import com.gitmadeeasy.entities.users.UserGateway;
import com.gitmadeeasy.usecases.email.VerificationEmailService;
import com.gitmadeeasy.usecases.shared.exceptions.MissingRequiredFieldException;
import com.gitmadeeasy.usecases.users.exceptions.EmailAlreadyVerifiedException;
import com.gitmadeeasy.usecases.users.exceptions.UserNotFoundWithEmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResendVerificationEmail {
    private final UserGateway userGateway;
    private final UserIdentityProvider userIdentityProvider;
    private final VerificationEmailService verificationEmailService;
    private static final Logger log = LoggerFactory.getLogger(ResendVerificationEmail.class);

    public ResendVerificationEmail(UserGateway userGateway, UserIdentityProvider userIdentityProvider,
                                   VerificationEmailService verificationEmailService) {
        this.userGateway = userGateway;
        this.userIdentityProvider = userIdentityProvider;
        this.verificationEmailService = verificationEmailService;
    }

    public void execute(String emailAddress) {
        if(emailAddress == null || emailAddress.isBlank()) {
            log.warn("ResendVerificationEmail failed: missing emailAddress");
            throw new MissingRequiredFieldException("email address cannot be left blank");
        }
        User user = this.userGateway.getUserByEmailAddress(emailAddress).orElseThrow(() -> new UserNotFoundWithEmailException(emailAddress));
        if(this.userIdentityProvider.isEmailVerified(user.getUserId())) { throw new EmailAlreadyVerifiedException(); }
        this.verificationEmailService.sendVerificationEmail(emailAddress, user.getFirstName());
    }
}
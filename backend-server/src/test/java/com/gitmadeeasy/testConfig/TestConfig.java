package com.gitmadeeasy.testConfig;

import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.email.EmailSender;
import com.google.firebase.FirebaseApp;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.UUID;

import static org.mockito.Mockito.mock;

@Configuration @Profile("test")
public class TestConfig {

    @Bean @Primary
    public JavaMailSender javaMailSender() {
        return mock(JavaMailSender.class);
    }

    @Bean @Primary
    public UserIdentityProvider userIdentityProvider() {
        return new UserIdentityProvider() {
            @Override
            public String createUser(String firstName, String lastName, String email, String password) {
                return UUID.randomUUID().toString();
            }

            @Override
            public String generateVerificationEmail(String emailAddress) {
                return "verification-email@gmail.com";
            }

            @Override
            public boolean isEmailVerified(String userId) {
                return true;
            }

            @Override
            public String login(String emailAddress, String password) {
                return "firebase-" + emailAddress;
            }
        };
    }

    @Bean @Primary
    public FirebaseApp firebaseApp() {
        return mock(FirebaseApp.class);
    }

    @Bean @Primary
    public EmailSender emailSender() {
        return new EmailSender() {
            @Override
            public void send(String toEmailAddress, String subject, String body) {
                System.out.printf("Email to be sent to: %s%n", toEmailAddress);
            }
        };
    }
}
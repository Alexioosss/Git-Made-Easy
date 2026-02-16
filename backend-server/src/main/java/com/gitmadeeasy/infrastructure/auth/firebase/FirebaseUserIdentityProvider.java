package com.gitmadeeasy.infrastructure.auth.firebase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.gitmadeeasy.usecases.users.exceptions.InvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FirebaseUserIdentityProvider implements UserIdentityProvider {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final String apiKey;

    public FirebaseUserIdentityProvider(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String createUser(String firstName, String lastName, String email, String password) {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(firstName + " " + lastName)
                .setEmailVerified(false);
        try {
            return FirebaseAuth.getInstance().createUser(request).getUid();
        } catch(Exception e) {
            throw new RuntimeException("Failed to create Firebase user: " + e.getMessage(), e);
        }
    }

    @Override
    public String sendVerificationEmail(String emailAddress) {
       try {
           HttpRequest request = HttpRequest.newBuilder()
                   .uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=" + apiKey))
                   .header("Content-Type", "application/json")
                   .POST(HttpRequest.BodyPublishers.ofString("""
                            {
                                "requestType": "VERIFY_EMAIL",
                                "email": "%s"
                            }
                            """.formatted(emailAddress)))
                   .build();
           HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
           if(response.statusCode() != 200) {
               throw new RuntimeException("Firebase sendOobCode failed: " + response.body());
           }
           return response.body();
       } catch(Exception e) {
           throw new RuntimeException("Failed to send email verification link: " + e.getMessage(), e);
       }
    }

    @Override
    public boolean isEmailVerified(String userId) {
        try {
            return FirebaseAuth.getInstance().getUser(userId).isEmailVerified();
        } catch(Exception e) {
            throw new RuntimeException("Failed to identify email verification status: " + e.getMessage(), e);
        }
    }

    @Override
    public String login(String emailAddress, String password) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("""
                            {
                                "email": "%s",
                                "password": "%s",
                                "returnSecureToken": true
                            }
                            """.formatted(emailAddress, password)))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200) { throw new InvalidCredentialsException(); }

            ObjectMapper mapper = new ObjectMapper();
            FirebaseLoginResponse loginResponse = mapper.readValue(response.body(), FirebaseLoginResponse.class);
            return loginResponse.getLocalId();
        } catch(Exception e) {
            throw new RuntimeException("Failed to login user: " + e.getMessage(), e);
        }
    }
}
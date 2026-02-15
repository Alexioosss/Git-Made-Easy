package com.gitmadeeasy.infrastructure.auth.firebase;

import com.gitmadeeasy.usecases.auth.UserIdentityProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;

public class FirebaseUserIdentityProvider implements UserIdentityProvider {

    @Override
    public String createUser(String firstName, String lastName, String email, String password) {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(firstName + " " + lastName)
                .setEmailVerified(false);
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            return userRecord.getUid();
        } catch(Exception e) {
            throw new RuntimeException("Failed to create Firebase user: " + e.getMessage(), e);
        }
    }

    @Override
    public String generateEmailVerificationLink(String email) {
       try {
           return FirebaseAuth.getInstance().generateEmailVerificationLink(email);
       } catch(Exception e) {
           throw new RuntimeException("Failed to generate email verification link: " + e.getMessage(), e);
       }
    }

    @Override
    public boolean isEmailVerified(String userId) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(userId);
            return userRecord.isEmailVerified();
        } catch(Exception e) {
            throw new RuntimeException("Failed to identify email verification status: " + e.getMessage(), e);
        }
    }
}
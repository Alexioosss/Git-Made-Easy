package com.gitmadeeasy.infrastructure.configs.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration @Profile("!test")
public class FirebaseConfig {
    @Value("${firebase.service-account}")
    private Resource serviceAccount;

    @Value("#{systemEnvironment['FIREBASE_SERVICE_ACCOUNT_JSON']}")
    private String environmentVariable;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream stream;
        if(environmentVariable != null && !environmentVariable.isEmpty()) {
            stream = new ByteArrayInputStream(environmentVariable.getBytes(StandardCharsets.UTF_8));
        }
        else if (serviceAccount != null && serviceAccount.exists()) { stream = this.serviceAccount.getInputStream(); }
        else {
            throw new IllegalStateException("Firebase service account JSON not provided via resource or environment variable.");
        }
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(stream)).build();
        if(FirebaseApp.getApps().isEmpty()) { return FirebaseApp.initializeApp(options); }
        else { return FirebaseApp.getInstance(); }
    }
}
package com.gitmadeeasy.infrastructure.configs.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration @Profile("!test")
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        String json = System.getenv("FIREBASE_SERVICE_ACCOUNT_JSON");
        if(json == null || json.isBlank()) {
            throw new IllegalStateException("Missing FIREBASE_SERVICE_ACCOUNT_JSON environment variable.");
        }
        InputStream stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(stream)).build();

        if (FirebaseApp.getApps().isEmpty()) { return FirebaseApp.initializeApp(options); }
        else { return FirebaseApp.getInstance(); }
    }
}
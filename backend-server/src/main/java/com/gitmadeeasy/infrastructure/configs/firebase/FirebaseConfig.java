package com.gitmadeeasy.infrastructure.configs.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

@Configuration @Profile("!test")
public class FirebaseConfig {
    @Value("${firebase.service-account}")
    private Resource serviceAccount;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        try(InputStream stream = this.serviceAccount.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(stream)).build();

            if(FirebaseApp.getApps().isEmpty()) { return FirebaseApp.initializeApp(options); }
            else { return FirebaseApp.getInstance(); }
        }
    }
}
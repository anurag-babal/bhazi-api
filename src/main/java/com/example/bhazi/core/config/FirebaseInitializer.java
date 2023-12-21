package com.example.bhazi.core.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FirebaseInitializer {
    @PostConstruct
    public void initiliaze() {
		try {
			GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(
                    new ClassPathResource("firebase_service_key.json")
                        .getInputStream()
                );
        FirebaseOptions firebaseOptions = FirebaseOptions
                .builder()
                .setCredentials(googleCredentials)
                .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
				log.info("Firebase application has been initialized");
            }
		} catch(IOException e) {
			log.error(e.getLocalizedMessage());
		}
    }
}

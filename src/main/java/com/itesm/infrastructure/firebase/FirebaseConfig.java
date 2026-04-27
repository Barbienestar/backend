package com.itesm.infrastructure.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import io.quarkus.arc.profile.UnlessBuildProfile;
import io.quarkus.runtime.Startup;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.FileInputStream;
import java.io.InputStream;

/** FirebaseConfig */
@Startup
@ApplicationScoped
@UnlessBuildProfile("test")
public class FirebaseConfig {
    @ConfigProperty(name = "firebase.service-account")
    String path;

    @ConfigProperty(name = "firebase.storage-bucket")
    String bucket;

    @PostConstruct
    void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = new FileInputStream(path);
                FirebaseOptions options =
                        FirebaseOptions.builder()
                                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                                .setStorageBucket(bucket)
                                .build();
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

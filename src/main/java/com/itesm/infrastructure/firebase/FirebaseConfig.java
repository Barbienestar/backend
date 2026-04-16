
package com.itesm.infrastructure.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.FileInputStream;
import java.io.InputStream;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * FirebaseConfig
 */
@Startup
@ApplicationScoped
public class FirebaseConfig {
    @ConfigProperty(name = "firebase.service-account") String path;

    @PostConstruct
    void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccount = new FileInputStream(path);
                FirebaseOptions options =
                    FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

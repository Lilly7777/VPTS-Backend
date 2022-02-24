package com.lilly.lubenova.vptsrest;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
//@EnableReactiveFirestoreRepositories
public class VptsRestApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(VptsRestApplication.class, args);
        initializeFirebase();
    }

    private static void initializeFirebase() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setDatabaseUrl("<databaseUrl>")
                .build();

        FirebaseApp.initializeApp(options);
    }

}

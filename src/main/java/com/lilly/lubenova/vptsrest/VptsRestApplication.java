package com.lilly.lubenova.vptsrest;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.lilly.lubenova.vptsrest.mqtt.client.listener.MqttMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;

import java.io.IOException;

@SpringBootApplication
public class VptsRestApplication {

    @Autowired
    MqttMessageListener messageListener;

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

    @Bean
    public CommandLineRunner schedulingRunner(TaskExecutor executor) {
        return args -> executor.execute(messageListener);
    }

}

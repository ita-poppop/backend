package com.example.poppop.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service-account-file}")  // application.yml 에 경로
    private String serviceAccountPath;

//    @Bean
//    public FirebaseApp firebaseApp() throws IOException {
//        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);
//
////        FirebaseOptions options = FirebaseOptions.builder()
////                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
////                .build();
////
////        return FirebaseApp.initializeApp(options);
//        GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(credentials)
//                // JSON의 project_id 값을 그대로 넣어 줍니다
//                .setProjectId("poppop-6d345")
//                .build();
//
//        return FirebaseApp.initializeApp(options);
//    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // 서비스 계정 JSON 파일을 ServiceAccountCredentials 로 로드
        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);
        ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(serviceAccount);

        // JSON 안의 project_id 를 꺼내서 setProjectId() 에 넣어 줍니다
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(credentials.getProjectId())
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp app) {
        return FirebaseMessaging.getInstance(app);
    }
}

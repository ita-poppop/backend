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
import java.util.List;

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

//    @Bean
////    public FirebaseApp firebaseApp() throws IOException {
////        // 서비스 계정 JSON 파일을 ServiceAccountCredentials 로 로드
////        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);
////        ServiceAccountCredentials credentials = ServiceAccountCredentials.fromStream(serviceAccount);
////
////        // JSON 안의 project_id 를 꺼내서 setProjectId() 에 넣어 줍니다
////        FirebaseOptions options = FirebaseOptions.builder()
////                .setCredentials(credentials)
////                .setProjectId(credentials.getProjectId())
////                .build();
////
////        return FirebaseApp.initializeApp(options);
////    }

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        // 1) 서비스 계정 JSON 파일 로드
        FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);

        // 2) GoogleCredentials 로 변환하면서 FCM scope 부여
        GoogleCredentials credentials = GoogleCredentials
                .fromStream(serviceAccount)
                .createScoped(List.of(
                        "https://www.googleapis.com/auth/firebase.messaging",
                        "https://www.googleapis.com/auth/cloud-platform"
                ));

        // 3) JSON 안의 project_id 를 꺼내서 프로젝트 ID 로 설정
        String projectId = ((ServiceAccountCredentials)credentials).getProjectId();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(projectId)
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging(FirebaseApp app) {
        return FirebaseMessaging.getInstance(app);
    }
}

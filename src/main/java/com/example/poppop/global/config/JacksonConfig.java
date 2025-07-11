package com.example.poppop.global.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

@Configuration
public class JacksonConfig {

    private static final String DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(DATETIME_PATTERN);

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            // JavaTimeModule 생성 및 formatter 설정
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(
                    LocalDateTime.class, new LocalDateTimeSerializer(FORMATTER)
            );
            javaTimeModule.addDeserializer(
                    LocalDateTime.class, new LocalDateTimeDeserializer(FORMATTER)
            );

            // 모듈 등록
            builder.modules(javaTimeModule);

            // 출력 시 타임존을 Asia/Seoul 으로 맞춤
            builder.timeZone(TimeZone.getTimeZone("Asia/Seoul"));
        };
    }
}

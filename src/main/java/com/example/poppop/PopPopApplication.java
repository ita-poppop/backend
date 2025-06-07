package com.example.poppop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PopPopApplication {

    public static void main(String[] args) {
        SpringApplication.run(PopPopApplication.class, args);
    }

}

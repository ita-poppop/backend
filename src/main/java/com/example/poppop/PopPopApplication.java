package com.example.poppop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PopPopApplication {

    public static void main(String[] args) {

        System.out.println("MYSQLDB_USERNAME = " + System.getenv("MYSQLDB_USERNAME"));
        System.out.println("MYSQLDB_PASSWORD = " + System.getenv("MYSQLDB_PASSWORD"));
        System.out.println("MYSQLDB_ROOT_PASSWORD = " + System.getenv("MYSQLDB_ROOT_PASSWORD"));

        SpringApplication.run(PopPopApplication.class, args);
    }


}

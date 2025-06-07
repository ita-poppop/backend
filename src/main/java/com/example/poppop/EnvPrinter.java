package com.example.poppop;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EnvPrinter {

    @Value("${MYSQLDB_USERNAME}")
    private String username;

    @Value("${MYSQLDB_PASSWORD}")
    private String password;

    @PostConstruct
    public void printCreds() {
        System.out.println("✅ MYSQLDB_USERNAME: " + username);
        System.out.println("✅ MYSQLDB_PASSWORD: " + password);
    }
}

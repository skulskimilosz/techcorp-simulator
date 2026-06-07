package com.example.techcorp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Główna klasa startowa aplikacji Spring Boot.
 * Adnotacja @SpringBootApplication włącza auto-konfigurację i skanowanie komponentów.
 */
@SpringBootApplication
public class TechCorpApplication {

    public static void main(String[] args) {
        SpringApplication.run(TechCorpApplication.class, args);
    }
}

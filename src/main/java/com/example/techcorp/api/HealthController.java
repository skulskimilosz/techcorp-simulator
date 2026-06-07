package com.example.techcorp.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Kontroler sprawdzający czy serwer działa poprawnie.
 * Endpoint /health jest wymagany przez instrukcję i używany przez Render
 * do weryfikacji czy serwis jest dostępny.
 */
@RestController
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "TechCorp API is running";
    }
}

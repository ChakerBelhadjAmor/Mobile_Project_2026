package com.supervision.livraison;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Supervision des Livraisons backend.
 *
 * Exposes a REST API consumed by the Android mobile clients (controller
 * and driver roles) and persists data in PostgreSQL through Spring Data JPA.
 */
@SpringBootApplication
public class SupervisionLivraisonApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupervisionLivraisonApplication.class, args);
    }
}

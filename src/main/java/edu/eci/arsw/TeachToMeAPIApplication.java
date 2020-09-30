package edu.eci.arsw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Clase Main de la aplicación Teach To Me
 */
@SpringBootApplication
@EnableJpaRepositories
public class TeachToMeAPIApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeachToMeAPIApplication.class, args);
    }
}


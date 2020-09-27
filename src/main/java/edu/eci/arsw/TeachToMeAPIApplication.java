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
        //BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        //System.out.println(bc.matches("prueba","$2a$10$fCe9Yf4kQMhi0Y04rnb6HOlb6MAjfUd3O2F/V9rQKdKeW5zl3h2MC"));
    }
}


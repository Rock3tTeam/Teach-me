package edu.eci.arsw.teachtome.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuracion para cifrar claves
 */
@Configuration
public class PasswordConfig {

    /**
     * Obtiene Cifrador para claves
     *
     * @return Cifrador configurado para cifrar claves
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}



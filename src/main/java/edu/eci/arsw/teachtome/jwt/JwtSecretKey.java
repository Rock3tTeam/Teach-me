package edu.eci.arsw.teachtome.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

/**
 * Configuracion de llave secreta para Json Web Token
 */
@Configuration
public class JwtSecretKey {

    private final JwtConfig jwtConfig;

    /**
     * Constructor por defecto de configuracion para llave secreta
     *
     * @param jwtConfig - Configuracion de Json Web Token
     */
    @Autowired
    public JwtSecretKey(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    /**
     * Obtiene la llave secreta para cifrar
     *
     * @return la llave secreta para cifrar
     */
    @Bean
    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtConfig.getSecretKey().getBytes());
    }
}

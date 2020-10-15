package edu.eci.arsw.teachtome.jwt;

import com.google.common.net.HttpHeaders;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de Json Web Token
 */
@Configuration
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String secretKey;
    private String tokenPrefix;
    private Integer tokenExpirationAfter;

    /**
     * Contructor por defecto de la configuracion
     */
    public JwtConfig() {
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public void setTokenPrefix(String tokenPrefix) {
        this.tokenPrefix = tokenPrefix;
    }

    public Integer getTokenExpirationAfter() {
        return tokenExpirationAfter;
    }

    public void setTokenExpirationAfter(Integer tokenExpirationAfter) {
        this.tokenExpirationAfter = tokenExpirationAfter;
    }

    /**
     * Obtiene el Header de retorno en caso de fallo
     *
     * @return Header de retorno en caso de autenticacion fallida
     */
    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}



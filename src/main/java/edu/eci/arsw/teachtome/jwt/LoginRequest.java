package edu.eci.arsw.teachtome.jwt;

/**
 * Representacion de Request para realizar login
 */
public class LoginRequest {

    private String username;
    private String password;

    /**
     * Constructor por defecto de solicitud de login
     */
    public LoginRequest() {
    }

    /**
     * Constructor por defecto de solicitud de login
     *
     * @param username - Email del usuario
     * @param password - Clave del usuario
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}


package edu.eci.arsw.teachtome.auth;

import edu.eci.arsw.teachtome.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Configuración de Detalles de Usuario de Teach To Me
 */
public class UserDetailsImpl implements UserDetails {

    private final String password;
    private final String username;

    /**
     * Constructor por defecto de Detalles de Usuario
     *
     * @param username Direccion de Correo Electrónico del Usuario
     * @param password Clave del Usuario
     */
    public UserDetailsImpl(String username, String password) {
        this.password = password;
        this.username = username;
    }

    /**
     * Constructor Estatico de Detalles de Usuario
     *
     * @param user - Usuario del Modelo de Teach To Me
     * @return Detalles del Usuario
     */
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getEmail(),
                user.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}



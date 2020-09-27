/*package edu.eci.arsw.teachtome.auth;


import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.services.TeachToMeServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private TeachToMeServicesInterface services;

    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = null;
        try {
            user = services.getUser(username);
            user.setPassword(user.getPassword());
        } catch (TeachToMeServiceException e) {
            throw new UsernameNotFoundException("El usuario con el email "+user+" no existe");
        }
        return UserDetailsImpl.build(user);
    }
}

 */

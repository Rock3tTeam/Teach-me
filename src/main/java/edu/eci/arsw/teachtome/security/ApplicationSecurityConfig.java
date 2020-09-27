/*package edu.eci.arsw.teachtome.security;

import edu.eci.arsw.teachtome.JWT.JwtAuthenticationFilter;
import edu.eci.arsw.teachtome.JWT.JwtConfig;
import edu.eci.arsw.teachtome.JWT.JwtTokenVerifier;
import edu.eci.arsw.teachtome.auth.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.crypto.SecretKey;


@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Autowired
    public ApplicationSecurityConfig(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder passwordEncoder, SecretKey secretKey, JwtConfig jwtConfig){
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers("/","/api/v1/")
                .permitAll()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig),JwtAuthenticationFilter.class)
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .permitAll()
                .usernameParameter("username")
                .passwordParameter("password");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Ignore spring security in these paths
        web.ignoring().antMatchers("/login.html","/signup.html","/css/*","/js/","/js/**","/fonts/","/fonts/**","/images/*","/api/v1/users","/favicon.ico");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }
}
*/
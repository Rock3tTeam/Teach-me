package edu.eci.arsw.teachtome.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("*","http://localhost:63342")
                .allowedMethods("GET", "POST","PUT","DELETE","OPTIONS")
                .allowedHeaders("Origin", "Accept", "Content-Type", "Authorization","content-type", "x-userEmail", "x-useremail")
                .allowCredentials(true)
                .maxAge(3600);

    }

}
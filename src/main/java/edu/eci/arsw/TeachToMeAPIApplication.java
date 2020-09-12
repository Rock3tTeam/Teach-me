package edu.eci.arsw;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.persistence.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootApplication
//@EnableJpaRepositories
public class TeachToMeAPIApplication {


    public static void main(String[] args) {
        SpringApplication.run(TeachToMeAPIApplication.class, args);
    }
}


package edu.eci.arsw.teachtome.controllers;


import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;



/**
 * Controlador API REST de la aplicación TeachToMe
 */
@RestController
@CrossOrigin(origins = "*",allowedHeaders="*")
@RequestMapping(value = "/")
public class ValidatorController {

    @GetMapping(value = "validate")
    public String getValidatorPage() {
        return "validate";
    }
}

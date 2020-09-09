package edu.eci.arsw.teachtome.controllers;

import edu.eci.arsw.teachtome.services.TeachToMeServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/api")
public class TeachToMeAPIController {
    @Autowired
    private TeachToMeServicesInterface services;

    @GetMapping
    public ResponseEntity<?> get() {
        try {
            return new ResponseEntity<>(services.getClase(), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    /*@RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> addSomething(@RequestBody Object object){
        try {
            services.post(object);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch ( MyTourneyException ex) {
            Logger.getLogger(TeachMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(),HttpStatus.FORBIDDEN);
        }

    }*/

}


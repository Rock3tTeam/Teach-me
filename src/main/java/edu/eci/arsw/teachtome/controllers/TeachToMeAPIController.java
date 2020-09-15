package edu.eci.arsw.teachtome.controllers;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.services.TeachToMeServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador API REST de la aplicación TeachToMe
 */
@RestController
@RequestMapping(value = "/api")
public class TeachToMeAPIController {
    @Autowired
    private TeachToMeServicesInterface services;

    /**
     * Obtiene la clase con el Id especificado
     *
     * @param classId - Identificador de la clase
     * @return Una entidad de respuesta con la clase o mensaje de excepción y su código de respuesta HTTP
     */
    @GetMapping(value = "/clases/{classId}")
    public ResponseEntity<?> getClass(@PathVariable Long classId) {
        try {
            return new ResponseEntity<>(services.getClase(classId), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping(value = "/users/{userEmail}/clases")
    public ResponseEntity<?> addClase(@RequestBody Clase clase, @PathVariable String userEmail) {
        if (clase.getNombre() == null) return new ResponseEntity<>("JSON Bad Format", HttpStatus.BAD_REQUEST);
        try {
            User user = services.getUser(userEmail);
            services.addClase(clase, user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/draws/{className}")
    public ResponseEntity<?> getDrawsOfAClass(@PathVariable String className) {
        try {
            return new ResponseEntity<>(services.getDrawsOfAClass(className), HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/draws/{className}")
    public ResponseEntity<?> addDraw(@RequestBody Draw draw, @PathVariable String className) {
        try {
            services.addDraw(className, draw);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/requests")
    public ResponseEntity<?> sendRequest(@RequestBody Request request) {
        try {
            services.sendRequest(request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping(value = "/requests")
    public ResponseEntity<?> updateRequest(@RequestBody Request request) {
        try {
            services.updateRequest(request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            //REVISAR CÓDIGO DE RESPUESTA
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/messages")
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        try {
            services.sendMessage(message);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/messages/{className}")
    public ResponseEntity<?> getChat(@PathVariable String className) {
        try {
            return new ResponseEntity<>(services.getChat(className), HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        if (user.getEmail() == null) return new ResponseEntity<>("JSON Bad Format", HttpStatus.BAD_REQUEST);
        try {
            services.addUser(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/users/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        try {
            return new ResponseEntity<>(services.getUser(email), HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}


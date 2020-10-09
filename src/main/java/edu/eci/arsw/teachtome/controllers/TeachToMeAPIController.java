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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Controlador API REST de la aplicación TeachToMe
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping(value = "/api/v1/")
public class TeachToMeAPIController {
    @Autowired
    private TeachToMeServicesInterface services;

    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Obtiene la clase con el Id especificado
     *
     * @param classId - Identificador de la clase
     * @return Una entidad de respuesta con la clase o mensaje de excepción y su código de respuesta HTTP
     */
    @GetMapping(value = "/classes/{classId}")
    public ResponseEntity<?> getClass(@PathVariable Long classId) {
        try {
            return new ResponseEntity<>(services.getClase(classId), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/classes")
    public ResponseEntity<?> getFilteredClassesByName(@RequestParam(value = "name") String nameFilter) {
        try {
            return new ResponseEntity<>(services.getFilteredClassesByName(nameFilter), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/teachingClasses")
    public ResponseEntity<?> getTeachingClasses(@RequestHeader("x-userEmail") String email) {
        try {
            return new ResponseEntity<>(services.getTeachingClassesOfUser(email), HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping(value = "/classes")
    public ResponseEntity<?> addClase(@RequestBody Clase clase, @RequestHeader("x-userEmail") String userEmail) {
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

    @GetMapping(value = "/draws/{classId}")
    public ResponseEntity<?> getDrawsOfAClass(@PathVariable long classId) {
        try {
            return new ResponseEntity<>(services.getDrawsOfAClass(classId), HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/studyingClasses")
    public ResponseEntity<?> getClassesOfAStudent(@RequestHeader("x-userEmail") String email) {
        try {
            return new ResponseEntity<>(services.getClassesOfAStudent(email), HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "/classes/{classId}/requests")
    public ResponseEntity<?> getRequestsOfAClass(@RequestHeader("x-userEmail") String email, @PathVariable long classId) {
        try {
            List<Request> requests = services.getRequestsOfAClass(classId, email);
            List<Request> nonAnswerRequests = new CopyOnWriteArrayList<>();
            for (Request request : requests) {
                if (!request.hasAnswer()) nonAnswerRequests.add(request);
            }
            if (nonAnswerRequests.isEmpty()) {
                return new ResponseEntity<>("No hay solicitudes pendientes para esta clase", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(nonAnswerRequests, HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            if (e.getMessage().equals("No tiene permitido ver los requests a esta clase")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
    }

    @PostMapping(value = "/classes/{classId}/requests")
    public ResponseEntity<?> sendRequest(@RequestHeader("x-userEmail") String email, @PathVariable long classId, @RequestBody Request request) {
        if (request.getRequestId() == null) return new ResponseEntity<>("JSON Bad Format", HttpStatus.BAD_REQUEST);
        try {
            services.sendRequest(request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().equals("El profesor no puede hacer un request a su misma clase")) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
            }
        }
    }

    @PutMapping(value = "/classes/{classId}/requests")
    public ResponseEntity<?> updateRequest(@RequestHeader("x-userEmail") String email, @PathVariable long classId, @RequestBody Request request) {
        if (request.getRequestId() == null) return new ResponseEntity<>("JSON Bad Format", HttpStatus.BAD_REQUEST);
        try {
            services.updateRequest(classId, email, request);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            if (ex.getMessage().equals("No tiene permitido actualizar el request de esta clase")) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
            }
        }
    }

    @PostMapping(value = "/draws/{classId}")
    public ResponseEntity<?> addDraw(@RequestBody Draw draw, @PathVariable long classId) {
        try {
            services.addDraw(classId, draw);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "/messages/{classId}")
    public ResponseEntity<?> sendMessage(@RequestBody Message message,@PathVariable long classId) {
        try {
            services.sendMessage(message,classId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value = "/messages/{classId}")
    public ResponseEntity<?> getChat(@PathVariable long classId) {
        try {
            return new ResponseEntity<>(services.getChat(classId), HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/users")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        if (user.getEmail() == null) return new ResponseEntity<>("JSON Bad Format", HttpStatus.BAD_REQUEST);
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    @GetMapping(value = "/requests/{classId}/{userId}")
    public ResponseEntity<?> getRequest(@PathVariable Long classId, @PathVariable Long userId) {
        try {
            return new ResponseEntity<>(services.getRequest(classId, userId), HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "../../login")
    public String getLoginPage() {
        return "login";
    }
}

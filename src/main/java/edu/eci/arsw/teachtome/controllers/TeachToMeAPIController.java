package edu.eci.arsw.teachtome.controllers;

import edu.eci.arsw.teachtome.controllers.dtos.ClaseDTO;
import edu.eci.arsw.teachtome.controllers.dtos.CreateUserDTO;
import edu.eci.arsw.teachtome.controllers.dtos.DrawDTO;
import edu.eci.arsw.teachtome.controllers.dtos.GetUserDTO;
import edu.eci.arsw.teachtome.controllers.dtos.MessageDTO;
import edu.eci.arsw.teachtome.controllers.dtos.RequestDTO;
import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.services.TeachToMeServicesInterface;
import edu.eci.arsw.teachtome.video.VideoTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private VideoTokenService videoTokenService;


    /**
     * Obtiene la clase con el Id especificado
     *
     * @param classId - Identificador de la clase
     * @return Entidad de respuesta con la clase o mensaje de excepción y su código de respuesta HTTP
     */
    @GetMapping(value = "/classes/{classId}")
    public ResponseEntity<?> getClass(@PathVariable Long classId) {
        try {
            ClaseDTO claseDTO = new ClaseDTO(services.getClase(classId));
            return new ResponseEntity<>(claseDTO, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene las clases cuyo nombre contenga el valor dado
     *
     * @param nameFilter Valor por el cual se van a filtrar las clases
     * @return Entidad de Respuesta con las clases o con el mensaje de error en caso de fallo
     */
    @GetMapping(value = "/classes")
    public ResponseEntity<?> getFilteredClassesByName(@RequestParam(value = "name") String nameFilter) {
        if (nameFilter.equals("")) {
            return new ResponseEntity<>("El valor para filtrar no puede estar vacío", HttpStatus.BAD_REQUEST);
        }
        try {
            List<ClaseDTO> claseDTOS = GetUserDTO.getClassesDTO(services.getFilteredClassesByName(nameFilter));
            return new ResponseEntity<>(claseDTOS, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene las clases que esta dictando un usuario
     *
     * @param email - Email del profesor
     * @return Entidad de Respuesta con las clases o con el mensaje de error en caso de fallo
     */
    @GetMapping(value = "/teachingClasses")
    public ResponseEntity<?> getTeachingClasses(@RequestHeader("x-userEmail") String email) {
        try {
            List<ClaseDTO> claseDTOS = GetUserDTO.getClassesDTO(services.getTeachingClassesOfUser(email));
            return new ResponseEntity<>(claseDTOS, HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene las clases en las que esta participando un usuario
     *
     * @param email - Email del estudiante
     * @return Entidad de Respuesta con las clases o con el mensaje de error en caso de fallo
     */
    @GetMapping(value = "/studyingClasses")
    public ResponseEntity<?> getClassesOfAStudent(@RequestHeader("x-userEmail") String email) {
        try {
            List<ClaseDTO> claseDTOS = GetUserDTO.getClassesDTO(services.getClassesOfAStudent(email));
            return new ResponseEntity<>(claseDTOS, HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene las solicitudes no contestadas de una clase
     *
     * @param email   - Email del profesor
     * @param classId - Identificador de la clase
     * @return Entidad de Respuesta con las solicitudes o con el mensaje de error en caso de fallo
     */
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
            List<RequestDTO> requestDTOS = RequestDTO.getRequestsDTO(nonAnswerRequests);
            return new ResponseEntity<>(requestDTOS, HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            if (e.getMessage().equals("No tiene permitido ver los requests a esta clase")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
    }

    /**
     * Obtiene los mensajes del chat de una clase
     *
     * @param classId - Identificador de la clase
     * @param email   - Email del miembro de la clase
     * @return Entidad de Respuesta con los mensajes o con el mensaje de error en caso de fallo
     */
    @GetMapping(value = "/messages/{classId}")
    public ResponseEntity<?> getChat(@PathVariable long classId, @RequestHeader("x-userEmail") String email) {
        try {
            List<MessageDTO> messageDTOS = MessageDTO.getMessagesDTO(services.getChat(classId, email));
            return new ResponseEntity<>(messageDTOS, HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene la informacion de un usuario por su email
     *
     * @param email - Email del Usuario
     * @return Entidad de Respuesta con la informacion del usuario o con el mensaje de error en caso de fallo
     */
    @GetMapping(value = "/users/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email) {
        try {
            GetUserDTO getUserDTO = new GetUserDTO(services.getUser(email));
            return new ResponseEntity<>(getUserDTO, HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene la informacion de una solicitud de una clase
     *
     * @param classId - Identificador de la clase
     * @param userId  - Identificador del solicitante
     * @return Entidad de Respuesta con la solicitud o con el mensaje de error en caso de fallo
     */
    @GetMapping(value = "/requests/{classId}/{userId}")
    public ResponseEntity<?> getRequest(@PathVariable Long classId, @PathVariable Long userId) {
        try {
            RequestDTO requestDTO = new RequestDTO(services.getRequest(classId, userId));
            return new ResponseEntity<>(requestDTO, HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Obtiene los ultimos dibujos de una clase
     *
     * @param classId Identificador de la clase
     * @return Entidad de Respuesta con la coleccion de clases y en caso de fallo, el mensaje de la excepcion
     */
    /*@GetMapping(value = "/draws/{classId}")
    public ResponseEntity<?> getDrawOfAClass(@PathVariable long classId) {
        try {
            DrawDTO drawDTO = new DrawDTO(services.getDrawsOfAClass(classId));
            return new ResponseEntity<>(drawDTO, HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }*/

    /**
     * Generador del Token para el video
     *
     * @param userId Identificador del Usuario
     * @return Entidad de respuesta con el token o con el mensaje de error
     */
    @GetMapping(value = "/{userId}/token")
    public ResponseEntity<?> generateVideoToken(@PathVariable long userId) {
        String token;
        try {
            token = videoTokenService.generateProvisionToken(userId);
            return new ResponseEntity<>(token, HttpStatus.ACCEPTED);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Error al obtener el token del video", HttpStatus.CONFLICT);
        }
    }

    /**
     * Agrega un nuevo usuario dentro de la app
     *
     * @param createUserDTO Informacion del nuevo usuario
     * @return Entidad de Respuesta con el estado de la solicitud y en caso de fallo, el mensaje de la excepcion
     */
    @PostMapping(value = "/users")
    public ResponseEntity<?> addUser(@RequestBody CreateUserDTO createUserDTO) {
        if (createUserDTO.getEmail() == null)
            return new ResponseEntity<>(TeachToMeServiceException.BAD_FORMAT, HttpStatus.BAD_REQUEST);
        try {
            createUserDTO.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
            services.addUser(new User(createUserDTO));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }

    /**
     * Envia una solicitud para ser parte de una clase
     *
     * @param email      - Email del solicitante
     * @param classId    - Identificador de la clase
     * @param requestDTO - Informacion de la solicitud
     * @return Entidad de Respuesta con el estado de la solicitud y en caso de fallo, el mensaje de la excepcion
     */
    @PostMapping(value = "/classes/{classId}/requests")
    public ResponseEntity<?> sendRequest(@RequestHeader("x-userEmail") String email, @PathVariable long classId, @RequestBody RequestDTO requestDTO) {
        if (requestDTO.getRequestId() == null)
            return new ResponseEntity<>(TeachToMeServiceException.BAD_FORMAT, HttpStatus.BAD_REQUEST);
        try {
            services.sendRequest(new Request(requestDTO));
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

    /**
     * Agrega una clase para dictar
     *
     * @param claseDTO  Informacion basica de la clase
     * @param userEmail - Email del profesor
     * @return Entidad de Respuesta con el estado de la solicitud y en caso de fallo, el mensaje de la excepcion
     */
    @PostMapping(value = "/classes")
    public ResponseEntity<?> addClase(@RequestBody ClaseDTO claseDTO, @RequestHeader("x-userEmail") String userEmail) {
        if (claseDTO.getNombre() == null)
            return new ResponseEntity<>(TeachToMeServiceException.BAD_FORMAT, HttpStatus.BAD_REQUEST);
        try {
            User user = services.getUser(userEmail);
            services.addClase(new Clase(claseDTO), user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }


    /**
     * Agrega dibujos dentro de la sesión de una clase
     *
     * @param drawDTO Dibujos para agregar
     * @param classId Identificador de la clase
     * @return Entidad de Respuesta con el estado de la solicitud y en caso de fallo, el mensaje de la excepcion
     */
    /*@PostMapping(value = "/draws/{classId}")
    public ResponseEntity<?> addDraw(@RequestBody DrawDTO drawDTO, @PathVariable long classId) {
        try {
            services.addDrawToCache(classId, new Draw(drawDTO));
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (TeachToMeServiceException ex) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
        }
    }*/

    /**
     * Actualiza la informacion de una solicitud para una clase
     *
     * @param email      - Email del profesor
     * @param classId    - Identificador de la clase
     * @param requestDTO - Nueva Informacion de la solicitud
     * @return Entidad de Respuesta con el estado de la solicitud y en caso de fallo, el mensaje de la excepcion
     */
    @PutMapping(value = "/classes/{classId}/requests")
    public ResponseEntity<?> updateRequest(@RequestHeader("x-userEmail") String email, @PathVariable long classId, @RequestBody RequestDTO requestDTO) {
        if (requestDTO.getRequestId() == null)
            return new ResponseEntity<>(TeachToMeServiceException.BAD_FORMAT, HttpStatus.BAD_REQUEST);
        try {
            services.updateRequest(classId, email, new Request(requestDTO));
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

    /**
     * Elimina una clase con el Id especificado
     *
     * @param classId - Identificador de la clase
     * @param email   - Email del profesor
     * @return Entidad de Respuesta con el estado de la solicitud y en caso de fallo, el mensaje de la excepcion
     */
    @DeleteMapping(value = "/classes/{classId}")
    public ResponseEntity<?> deleteClass(@PathVariable Long classId, @RequestHeader("x-userEmail") String email) {
        try {
            services.deleteClass(classId, email);
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        } catch (TeachToMeServiceException e) {
            Logger.getLogger(TeachToMeAPIController.class.getName()).log(Level.SEVERE, null, e);
            if (e.getMessage().equals("El usuario no tiene permiso para eliminar esta clase")) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
            } else {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
            }
        }
    }


}

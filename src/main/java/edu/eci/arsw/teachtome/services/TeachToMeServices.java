package edu.eci.arsw.teachtome.services;

import edu.eci.arsw.teachtome.mail.MailSenderInterface;
import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.persistence.TeachToMePersistence;
import edu.eci.arsw.teachtome.persistence.TeachToMePersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Implementación de la capa de servicios de la aplicación TeachToMe
 */
@Service
public class TeachToMeServices implements TeachToMeServicesInterface {

    @Autowired
    private TeachToMePersistence persistence;

    @Autowired
    private MailSenderInterface mailSenderInterface;

    /**
     * Consulta una clase dentro del modelo
     *
     * @param classId - Identificador de la clase
     * @return Un objeto tipo Clase con su respectivo identificador
     * @throws TeachToMeServiceException - Cuando no existe clase con ese identificador
     */
    @Override
    public Clase getClase(Long classId) throws TeachToMeServiceException {
        if (classId == null) throw new TeachToMeServiceException("El id no puede ser nulo");
        try {
            return persistence.getClase(classId);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Permite a un usuario agregar una nueva clase
     *
     * @param clase - La nueva clase que se va a agregar
     * @param user  - El usuario que va a dictar esa clase
     * @throws TeachToMeServiceException - Cuando el usuario no existía o la clase tiene información nula
     */
    @Override
    public void addClase(Clase clase, User user) throws TeachToMeServiceException {
        if (clase == null) throw new TeachToMeServiceException("La clase no puede ser nula");
        if (user == null) throw new TeachToMeServiceException("El usuario no puede ser nulo");
        if (clase.getDateOfInit().before(new Timestamp(new Date().getTime()))) {
            throw new TeachToMeServiceException("No se puede programar una clase antes de la hora actual");
        }
        if (clase.getDateOfInit().after(clase.getDateOfEnd())) {
            throw new TeachToMeServiceException("Una clase no puede iniciar después de su fecha de finalización");
        }
        if (clase.getCapacity() <= 0) {
            throw new TeachToMeServiceException("No se puede insertar una clase con capacidad menor que 1");
        }
        try {
            persistence.addClase(clase, user);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Realiza una request de un estudiante a una clase
     *
     * @param request - La request con el usuario y la clase a la cual quiere unirse
     * @throws TeachToMeServiceException - Cuando el usuario o la clase no existan en la base de datos.
     */
    @Override
    public void sendRequest(Request request) throws TeachToMeServiceException {
        if (request == null) throw new TeachToMeServiceException("La solicitud no puede ser nula");
        if (request.getRequestId() == null) {
            throw new TeachToMeServiceException("La solicitud no puede estar vacía");
        }
        try {
            persistence.sendRequest(request);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Obtiene las requests que se han hecho a una clase
     *
     * @param email   - El mail del usuario del cual se van a obtener las clases
     * @param classId - El id de la clase que se está buscando
     * @return Los requests que se han hecho a esa clase
     * @throws TeachToMeServiceException - Cuando el usuario o la clase no existen en la base de datos
     */
    @Override
    public List<Request> getRequestsOfAClass(long classId, String email) throws TeachToMeServiceException {
        if (email == null) throw new TeachToMeServiceException(TeachToMeServiceException.NULL_EMAIL);
        try {
            return persistence.getRequestsOfAClass(classId, email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Envia un mensaje dentro del chat
     *
     * @param message - Nuevo Mensaje del chat
     * @param classId - Identificador de la clase
     * @param email   - Email del remitente
     * @throws TeachToMeServiceException - Cuando no existe alguna entidad
     */
    @Override
    public void sendMessage(Message message, long classId, String email) throws TeachToMeServiceException {
        try {
            persistence.sendMessage(message, classId, email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Obtiene los mensajes de una clase
     *
     * @param classId - Identificador de la clase
     * @param email   - Email del remitente
     * @return Coleccion con  los mensajes del chat
     * @throws TeachToMeServiceException - Cuando no existe alguna entidad
     */
    @Override
    public List<Message> getChat(long classId, String email) throws TeachToMeServiceException {
        try {
            return persistence.getChat(classId, email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Agrega un nuevo usuario dentro de la app
     *
     * @param user - Informacion del nuevo usuario
     * @throws TeachToMeServiceException - Cuando la entidad ya existia
     */
    @Override
    public void addUser(User user) throws TeachToMeServiceException {
        if (user == null) throw new TeachToMeServiceException("El usuario no puede ser nulo");
        try {
            persistence.addUser(user);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
        if (!user.getEmail().equals(user.getDescription())) {
            mailSenderInterface.sendCreatedUserEmail(user);
        }
    }

    /**
     * Obtiene la informacion de un usuario por su email
     *
     * @param email - Email del Usuario
     * @return Entidad del Usuario con el email dado
     * @throws TeachToMeServiceException - Cuando la entidad no existe
     */
    @Override
    public User getUser(String email) throws TeachToMeServiceException {
        if (email == null) throw new TeachToMeServiceException(TeachToMeServiceException.NULL_EMAIL);
        User user;
        try {
            user = persistence.getUser(email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
        return user;
    }

    /**
     * Obtiene la informacion de una solicitud de una clase
     *
     * @param classId - Identificador de la clase
     * @param userId  - Identificador del solicitante
     * @return Solicitud de una clase por parte de un usuario
     * @throws TeachToMeServiceException - Cuando alguna entidad no existe
     */
    @Override
    public Request getRequest(Long classId, Long userId) throws TeachToMeServiceException {
        if (classId == null || userId == null) {
            throw new TeachToMeServiceException("Los identificadores no pueden ser nulos");
        }
        try {
            return persistence.getRequest(classId, userId);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Obtiene las clases de un usuario
     *
     * @param email - El mail del usuario del cual se van a obtener las clases
     * @return Las clases que dicta el usuario
     * @throws TeachToMeServiceException - Cuando el usuario no exista en la base de datos
     */
    @Override
    public List<Clase> getTeachingClassesOfUser(String email) throws TeachToMeServiceException {
        if (email == null) throw new TeachToMeServiceException(TeachToMeServiceException.NULL_EMAIL);
        try {
            return persistence.getTeachingClassesOfUser(email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Agrega un estudiante dentro de la clase
     *
     * @param clase - Clase a la cual se va a agregar el usuario
     * @param email - Email del usuario que va a ser agregado
     * @throws TeachToMeServiceException - Cuando no existe alguna entidad o la clase no tiene cupo
     */
    @Override
    public void addStudentToAClass(Clase clase, String email) throws TeachToMeServiceException {
        if (email == null) throw new TeachToMeServiceException(TeachToMeServiceException.NULL_EMAIL);
        try {
            persistence.addStudentToAClass(clase, email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Obtiene las clases que aprende un usuario
     *
     * @param email - El email del usuario del cual se van a obtener las clases
     * @return Las clases que dicta el usuario
     * @throws TeachToMeServiceException - Cuando el usuario no exista en la base de datos
     */
    @Override
    public List<Clase> getClassesOfAStudent(String email) throws TeachToMeServiceException {
        if (email == null) throw new TeachToMeServiceException(TeachToMeServiceException.NULL_EMAIL);
        try {
            return persistence.getClassesOfAStudent(email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Actualiza la request de una clase
     *
     * @param email   - El mail del usuario del cual se va a obtener el request
     * @param classId - El id de la clase que se está buscando
     * @throws TeachToMeServiceException - En caso de que un usuario inautorizado intente actualizar el request
     */
    @Override
    public void updateRequest(Long classId, String email, Request request) throws TeachToMeServiceException {
        if (email == null) throw new TeachToMeServiceException("El correo del maestro no debe ser nulo");
        try {
            persistence.updateRequest(classId, email, request);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /**
     * Consulta las clases que contengan cierta palabra
     *
     * @param nameFilter - nombre de la clase
     * @return La lista de clases que contengan esa palabra
     * @throws TeachToMeServiceException - Si la clase no existe en la base de datos
     */
    @Override
    public List<Clase> getFilteredClassesByName(String nameFilter) throws TeachToMeServiceException {
        if (nameFilter == null) {
            throw new TeachToMeServiceException("El nombre no puede ser nulo");
        }
        return persistence.getFilteredClassesByName(nameFilter);
    }

    /**
     * Elimina una clase dentro de la base de datos
     *
     * @param clase - La clase a ser eliminada
     * @param user  - El usuario que eliminará la clase
     * @throws TeachToMeServiceException - Cuando no existe la clase dentro de la base de datos o no la elimina su profesor
     */
    @Override
    public void deleteClass(long clase, String user) throws TeachToMeServiceException {
        try {
            persistence.deleteClass(clase, user);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Draw> getDrawsOfAClass(long classId) throws TeachToMeServiceException {
        try {
            return persistence.getDrawsOfAClass(classId);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    /*@Override
    public void addDraw(long classId, Draw draw) throws TeachToMeServiceException {
        try {
            persistence.addDraw(classId, draw);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }*/
}
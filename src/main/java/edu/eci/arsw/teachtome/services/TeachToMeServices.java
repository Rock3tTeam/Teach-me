package edu.eci.arsw.teachtome.services;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.persistence.TeachToMePersistence;
import edu.eci.arsw.teachtome.persistence.TeachToMePersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación de la capa de servicios de la aplicación TeachToMe
 */
@Service
public class TeachToMeServices implements TeachToMeServicesInterface {

    @Autowired
    private TeachToMePersistence persistence;

    /**
     * Consulta una clase dentro del modelo
     *
     * @param classId - Identificador de la clase
     * @return Un objeto tipo Clase con su respectivo identificador
     * @throws TeachToMeServiceException - Cuando no existe clase con ese identificador
     */
    @Override
    public Clase getClase(Long classId) throws TeachToMeServiceException {
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
        try {
            persistence.addClase(clase, user);
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

    @Override
    public void addDraw(long classId, Draw draw) throws TeachToMeServiceException {
        try {
            persistence.addDraw(classId, draw);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void sendRequest(Request request) throws TeachToMeServiceException {
        try {
            persistence.sendRequest(request);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }


    @Override
    public List<Request> getRequestsOfAClass(long classId, String email) throws TeachToMeServiceException {
        try {
            return persistence.getRequestsOfAClass(classId, email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }


    @Override
    public void sendMessage(Message message, long classId, String email) throws TeachToMeServiceException {
        try {
            persistence.sendMessage(message, classId, email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Message> getChat(long classId, String email) throws TeachToMeServiceException {
        try {
            return persistence.getChat(classId,email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void addUser(User user) throws TeachToMeServiceException {
        try {
            persistence.addUser(user);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public User getUser(String email) throws TeachToMeServiceException {
        User user;
        try {
            user = persistence.getUser(email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
        return user;
    }

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

    @Override
    public List<Clase> getTeachingClassesOfUser(String email) throws TeachToMeServiceException {
        try {
            return persistence.getTeachingClassesOfUser(email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void addStudentToAClass(Clase clase, String email) throws TeachToMeServiceException {
        try {
            persistence.addStudentToAClass(clase, email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Clase> getClassesOfAStudent(String email) throws TeachToMeServiceException {
        try {
            return persistence.getClassesOfAStudent(email);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void updateRequest(Long classId, String email, Request request) throws TeachToMeServiceException {
        try {
            persistence.updateRequest(classId, email, request);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public List<Clase> getFilteredClassesByName(String nameFilter) throws TeachToMeServiceException {
        try {
            return persistence.getFilteredClassesByName(nameFilter);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteClass(Clase clase, User user) throws TeachToMeServiceException {
        try {
            persistence.deleteClass(clase, user);
        } catch (TeachToMePersistenceException e) {
            throw new TeachToMeServiceException(e.getMessage(), e);
        }
    }
}
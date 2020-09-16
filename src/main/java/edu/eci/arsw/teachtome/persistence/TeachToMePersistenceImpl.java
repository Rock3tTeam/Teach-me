package edu.eci.arsw.teachtome.persistence;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.persistence.repositories.ClaseRepository;
import edu.eci.arsw.teachtome.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementación de la capa de persistencia de la aplicación TeachToMe
 */
@Service
public class TeachToMePersistenceImpl implements TeachToMePersistence {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Clase getClase(Long id) throws TeachToMePersistenceException {
        Clase clase = null;
        if (id == null) throw new TeachToMePersistenceException("El id no puede ser nulo");
        if (claseRepository.existsById(id)) {
            clase = claseRepository.findById(id).get();
        }
        if (clase == null) throw new TeachToMePersistenceException("No existe la clase con el id " + id);
        return clase;
    }

    @Override
    public void addClase(Clase clase, User user) throws TeachToMePersistenceException {
        if (clase == null) throw new TeachToMePersistenceException("La clase no puede ser nula");
        if (user == null) throw new TeachToMePersistenceException("El usuario no puede ser nulo");
        user.getTeachingClasses().add(clase);
        clase.setProfessor(user);
        User userSaved = userRepository.save(user);
        long claseId = userSaved.getTeachingClasses().get(0).getId();
        clase.setId(claseId);
    }

    @Override
    public void addStudentToAClass(Clase clase, String email) throws TeachToMePersistenceException {
        User user = getUser(email);
        if (clase == null) throw new TeachToMePersistenceException("La clase no puede ser nula");
        for(User student : clase.getStudents()){
            if(student.getEmail().equals(email)){
                throw new TeachToMePersistenceException("El usuario con el email "+email+" ya se encuentra en la clase");
            }
        }
        clase.getStudents().add(user);
        user.getStudyingClasses().add(clase);
        claseRepository.save(clase);
        userRepository.save(user);
    }

    @Override
    public void addUser(User user) throws TeachToMePersistenceException {
        if (user == null) throw new TeachToMePersistenceException("El usuario no puede ser nulo");
        userRepository.save(user);
    }

    @Override
    public User getUser(String email) throws TeachToMePersistenceException {
        User user = null;
        if (email == null) throw new TeachToMePersistenceException("El email no puede ser nulo");
        if (userRepository.existsById(email)) {
            user = userRepository.findById(email).get();
        }
        if (user == null) throw new TeachToMePersistenceException("No existe el usuario con el email " + email);
        return user;
    }

    @Override
    public List<Clase> getTeachingClassesOfUser(String email)throws TeachToMePersistenceException{
        User user = getUser(email);
        return user.getTeachingClasses();
    }

    @Override
    public List<Clase> getClassesOfAStudent(String email) throws TeachToMePersistenceException {
        User user = getUser(email);
        return user.getStudyingClasses();
    }

    @Override
    public List<Draw> getDrawsOfAClass(String className) throws TeachToMePersistenceException {
        return null;
    }

    @Override
    public void addClase(String className, Draw draw) throws TeachToMePersistenceException {

    }

    @Override
    public void sendRequest(Request request) throws TeachToMePersistenceException {

    }

    @Override
    public void updateRequest(Request request) throws TeachToMePersistenceException {

    }

    @Override
    public void sendMessage(Message message) throws TeachToMePersistenceException {

    }

    @Override
    public List<Message> getChat(String className) throws TeachToMePersistenceException {
        return null;
    }

}

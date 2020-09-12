package edu.eci.arsw.teachtome.persistence;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.persistence.repositories.ClaseRepository;
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

    /**
     * Consulta una clase dentro de la base de datos
     *
     * @param id - El identificador de la clase
     * @return La clase con su respectivo identificador
     * @throws TeachToMePersistenceException - Cuando no existe la clase dentro de la base de datos
     */
    @Override
    public Clase getClase(Long id) throws TeachToMePersistenceException {
        Clase clase = null;
        if (claseRepository.existsById(id)) {
            clase = claseRepository.findById(id).get();
        }
        if (clase == null) {
            throw new TeachToMePersistenceException("No existe la clase con el id " + id);
        }
        return clase;
    }

    @Override
    public void addClase(Clase clase) throws TeachToMePersistenceException {

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
    public void acceptRequest(Request request) throws TeachToMePersistenceException {

    }

    @Override
    public void sendMessage(Message message) throws TeachToMePersistenceException {

    }

    @Override
    public List<Message> getChat(String className) throws TeachToMePersistenceException {
        return null;
    }

    @Override
    public void addUser(User user) throws TeachToMePersistenceException {

    }

    @Override
    public User getUser(String email) throws TeachToMePersistenceException {
        return null;
    }
}

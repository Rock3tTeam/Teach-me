package edu.eci.arsw.teachtome.persistence;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;

import java.util.List;

public interface TeachToMePersistence {
    /**
     * Consulta una clase dentro de la base de datos
     *
     * @param id - El identificador de la clase
     * @return La clase con su respectivo identificador
     * @throws TeachToMePersistenceException - Cuando no existe la clase dentro de la base de datos
     */
    Clase getClase(Long id) throws TeachToMePersistenceException;

    void addClase(Clase clase , User user) throws TeachToMePersistenceException;

    List<Draw> getDrawsOfAClass(String className) throws TeachToMePersistenceException;

    void addClase(String className, Draw draw) throws TeachToMePersistenceException;

    void sendRequest(Request request) throws TeachToMePersistenceException;

    void updateRequest(Request request) throws TeachToMePersistenceException;

    void sendMessage(Message message) throws TeachToMePersistenceException;

    List<Message> getChat(String className) throws TeachToMePersistenceException;

    void addUser(User user) throws TeachToMePersistenceException;

    User getUser(String email) throws TeachToMePersistenceException;
}

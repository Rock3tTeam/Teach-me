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
    public Clase getClase(Long id) throws TeachToMePersistenceException;

    public void addClase(Clase clase) throws TeachToMePersistenceException;

    public List<Draw> getDrawsOfAClass(String className) throws TeachToMePersistenceException;

    public void addClase(String className, Draw draw) throws TeachToMePersistenceException;

    public void sendRequest(Request request) throws TeachToMePersistenceException;

    public void acceptRequest(Request request) throws TeachToMePersistenceException;

    public void sendMessage(Message message) throws TeachToMePersistenceException;

    public List<Message> getChat(String className) throws TeachToMePersistenceException;

    public void addUser(User user) throws TeachToMePersistenceException;

    public User getUser(String email) throws TeachToMePersistenceException;
}

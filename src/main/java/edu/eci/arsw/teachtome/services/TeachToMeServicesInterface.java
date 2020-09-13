package edu.eci.arsw.teachtome.services;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;

import java.util.List;

/**
 * Interfaz de la capa de servicios de la aplicación TeachToMe
 */
public interface TeachToMeServicesInterface {
    /**
     * Consulta una clase dentro del modelo
     *
     * @param classId - Identificador de la clase
     * @return Un objeto tipo Clase con su respectivo identificador
     * @throws TeachToMeServiceException - Cuando no existe clase con ese identificador
     */
    Clase getClase(Long classId) throws TeachToMeServiceException;

    /**
     * Permite a un usuario agregar una nueva clase
     *
     * @param clase - La nueva clase que se va a agregar
     * @param user  - El usuario que va a dictar esa clase
     * @throws TeachToMeServiceException - Cuando el usuario no exista o la clase tiene información nula
     */
    void addClase(Clase clase, User user) throws TeachToMeServiceException;

    List<Draw> getDrawsOfAClass(String className) throws TeachToMeServiceException;

    void addDraw(String className, Draw draw) throws TeachToMeServiceException;

    void sendRequest(Request request) throws TeachToMeServiceException;

    void updateRequest(Request request) throws TeachToMeServiceException;

    void sendMessage(Message message) throws TeachToMeServiceException;

    List<Message> getChat(String className) throws TeachToMeServiceException;

    void addUser(User user) throws TeachToMeServiceException;

    User getUser(String email) throws TeachToMeServiceException;
    //FALTA METODO DE AUTENTICACION

}

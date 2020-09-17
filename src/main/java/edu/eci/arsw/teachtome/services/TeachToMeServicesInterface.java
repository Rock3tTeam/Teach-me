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

    /**
     * Obtiene las clases de un usuario
     *
     * @param email - El mail del usuario del cual se van a obtener las clases
     * @return Las clases que dicta el usuario
     * @throws TeachToMeServiceException - Cuando el usuario no exista en la base de datos
     */
    List<Clase> getTeachingClassesOfUser(String email) throws TeachToMeServiceException;

    /**
     * Agrega una nueva clase de un usuario dentro de la base de datos
     *
     * @param clase - La clase a la cual el usuario se va a agregar
     * @param email - El mail del usuario del cual se van a obtener las clases
     * @throws TeachToMeServiceException - Cuando el usuario no exista en la base de datos o la clase sea nula
     */

    void addStudentToAClass(Clase clase, String email) throws TeachToMeServiceException;

    /**
     * Obtiene las clases que aprende un usuario
     *
     * @param email - El mail del usuario del cual se van a obtener las clases
     * @return Las clases que dicta el usuario
     * @throws TeachToMeServiceException - Cuando el usuario no exista en la base de datos
     */

    List<Clase> getClassesOfAStudent(String email) throws TeachToMeServiceException;

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

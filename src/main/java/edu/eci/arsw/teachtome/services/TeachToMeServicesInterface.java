package edu.eci.arsw.teachtome.services;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.security.LoginRequest;

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

    /**
     * Realiza una request de un estudiante a una clase
     *
     * @param request - La request con el usuario y la clase a la cual quiere unirse
     * @throws TeachToMeServiceException - Cuando el usuario o la clase no existan en la base de datos.
     */

    void sendRequest(Request request) throws TeachToMeServiceException;

    /**
     * Obtiene las requests que se han hecho a una clase
     *
     * @param email   - El mail del usuario del cual se van a obtener las clases
     * @param classId - El id de la clase que se está buscando
     * @return Los requests que se han hecho a esa clase
     * @throws TeachToMeServiceException - Cuando el usuario o la clase no existen en la base de datos
     */


    List<Request> getRequestsOfAClass(long classId, String email) throws TeachToMeServiceException;

    /**
     * Actualiza la request de una clase
     *
     * @param email   - El mail del usuario del cual se va a obtener el request
     * @param classId - El id de la clase que se está buscando
     * @throws TeachToMeServiceException - En caso de que un usuario inautorizado intente actualizar el request
     */
    void updateRequest(Long classId, String email, Request request) throws TeachToMeServiceException;

    /**
     * Consulta las clases que contengan cierta palabra
     *
     * @param nameFilter - nombre de la clase
     * @return La lista de clases que contengan esa palabra
     * @throws TeachToMeServiceException - Si la clase no existe en la base de datos
     */
    List<Clase> getFilteredClassesByName(String nameFilter) throws TeachToMeServiceException;

    List<Draw> getDrawsOfAClass(String className) throws TeachToMeServiceException;

    void addDraw(String className, Draw draw) throws TeachToMeServiceException;


    void sendMessage(Message message) throws TeachToMeServiceException;

    List<Message> getChat(String className) throws TeachToMeServiceException;

    void addUser(User user) throws TeachToMeServiceException;

    User getUser(String email) throws TeachToMeServiceException;

    LoginRequest login(LoginRequest request) throws TeachToMeServiceException;

}

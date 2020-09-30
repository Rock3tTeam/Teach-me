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

    /**
     * Agrega una nueva clase de un usuario dentro de la base de datos
     *
     * @param clase - La nueva clase que se va a agregar
     * @param user  - El usuario que va a dictar esa clase
     * @throws TeachToMePersistenceException - Cuando el usuario no exista en la base de datos o falte información obligatoria de la clase
     */

    void addClase(Clase clase, User user) throws TeachToMePersistenceException;

    /**
     * Obtiene las clases que enseña un usuario
     *
     * @param email - El mail del usuario del cual se van a obtener las clases
     * @return Las clases que dicta el usuario
     * @throws TeachToMePersistenceException - Cuando el usuario no exista en la base de datos
     */
    List<Clase> getTeachingClassesOfUser(String email) throws TeachToMePersistenceException;

    /**
     * Agrega una nueva clase de un usuario dentro de la base de datos
     *
     * @param clase - La clase a la cual el usuario se va a agregar
     * @param email - El mail del usuario del cual se van a obtener las clases
     * @throws TeachToMePersistenceException - Cuando el usuario no exista en la base de datos o la clase sea nula
     */

    void addStudentToAClass(Clase clase, String email) throws TeachToMePersistenceException;

    /**
     * Obtiene las clases que aprende un usuario
     *
     * @param email - El mail del usuario del cual se van a obtener las clases
     * @return Las clases que dicta el usuario
     * @throws TeachToMePersistenceException - Cuando el usuario no exista en la base de datos
     */

    List<Clase> getClassesOfAStudent(String email) throws TeachToMePersistenceException;

    /**
     * Realiza una request de un estudiante a una clase
     *
     * @param request - La request con el usuario y la clase a la cual quiere unirse
     * @throws TeachToMePersistenceException - Cuando el usuario o la clase no existan en la base de datos.
     */

    void sendRequest(Request request) throws TeachToMePersistenceException;

    /**
     * Obtiene las requests que se han hecho a una clase
     *
     * @param email   - El mail del usuario del cual se van a obtener las clases
     * @param classId - El id de la clase que se está buscando
     * @return Los requests que se han hecho a esa clase
     * @throws TeachToMePersistenceException - Cuando el usuario o la clase no existen en la base de datos
     */

    List<Request> getRequestsOfAClass(long classId, String email) throws TeachToMePersistenceException;

    /**
     * Obtiene las request de un usuario a una clase
     *
     * @param emailStudent - El mail del usuario del cual se va a obtener el request
     * @param classId      - El id de la clase que se está buscando
     * @return La request de un estudiante a una clase
     * @throws TeachToMePersistenceException - Cuando el usuario no ha hecho request a esa clase
     */

    Request getRequest(long classId, String emailStudent) throws TeachToMePersistenceException;

    /**
     * Actualiza la request de una clase
     *
     * @param email   - El mail del usuario del cual se va a obtener el request
     * @param classId - El id de la clase que se está buscando
     * @throws TeachToMePersistenceException - En caso de que un usuario inautorizado intente actualizar el request
     */
    void updateRequest(Long classId, String email, Request request) throws TeachToMePersistenceException;

    /**
     * Consulta las clases que contengan cierta palabra
     *
     * @param nameFilter - nombre de la clase
     * @return La lista de clases que contengan esa palabra
     * @throws TeachToMePersistenceException - Si la clase no existe en la base de datos
     */
    List<Clase> getFilteredClassesByName(String nameFilter) throws TeachToMePersistenceException;

    List<Draw> getDrawsOfAClass(String className) throws TeachToMePersistenceException;

    void addClase(String className, Draw draw) throws TeachToMePersistenceException;

    void sendMessage(Message message) throws TeachToMePersistenceException;

    List<Message> getChat(String className) throws TeachToMePersistenceException;

    void addUser(User user) throws TeachToMePersistenceException;

    User getUser(String email) throws TeachToMePersistenceException;

    User getUserById(long id) throws TeachToMePersistenceException;

    /*LoginRequest login(LoginRequest request) throws TeachToMePersistenceException;*/
}
package edu.eci.arsw.teachtome.services;

import edu.eci.arsw.teachtome.model.Clase;
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
     * Elimina una clase dentro de la base de datos
     *
     * @param clase - La clase a ser eliminada
     * @param user  - El usuario que eliminará la clase
     * @throws TeachToMeServiceException - Cuando no existe la clase dentro de la base de datos o no la elimina su profesor
     */
    void deleteClass(long clase, String user) throws TeachToMeServiceException;

    /**
     * Agrega un estudiante dentro de la clase
     *
     * @param clase - Clase a la cual se va a agregar el usuario
     * @param email - Email del usuario que va a ser agregado
     * @throws TeachToMeServiceException - Cuando no existe alguna entidad o la clase no tiene cupo
     */
    void addStudentToAClass(Clase clase, String email) throws TeachToMeServiceException;

    /**
     * Obtiene las clases que aprende un usuario
     *
     * @param email - El email del usuario del cual se van a obtener las clases
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

    /**
     * Envia un mensaje dentro del chat
     *
     * @param message - Nuevo Mensaje del chat
     * @param classId - Identificador de la clase
     * @param email   - Email del remitente
     * @throws TeachToMeServiceException - Cuando no existe alguna entidad
     */
    void sendMessage(Message message, long classId, String email) throws TeachToMeServiceException;

    /**
     * @param classId - Identificador de la clase
     * @param email   - Email del remitente
     * @return Coleccion con  los mensajes del chat
     * @throws TeachToMeServiceException - Cuando no existe alguna entidad
     */
    List<Message> getChat(long classId, String email) throws TeachToMeServiceException;

    /**
     * Agrega un nuevo usuario dentro de la app
     *
     * @param user - Informacion del nuevo usuario
     * @throws TeachToMeServiceException - Cuando la entidad ya existia
     */
    void addUser(User user) throws TeachToMeServiceException;

    /**
     * Obtiene la informacion de un usuario por su email
     *
     * @param email - Email del Usuario
     * @return Entidad del Usuario con el email dado
     * @throws TeachToMeServiceException - Cuando la entidad no existe
     */
    User getUser(String email) throws TeachToMeServiceException;

    /**
     * Obtiene la informacion de una solicitud de una clase
     *
     * @param classId - Identificador de la clase
     * @param userId  - Identificador del solicitante
     * @return Solicitud de una clase por parte de un usuario
     * @throws TeachToMeServiceException - Cuando alguna entidad no existe
     */
    Request getRequest(Long classId, Long userId) throws TeachToMeServiceException;

    /**
     * Obtiene los dibujos de una clase
     *
     * @param classId el id de la clase a la cual el dibujo será añadido
     * @return Una lista con los dibujos de la clase
     * @throws TeachToMeServiceException - Cuando la clase no existe en el cache
     */
    //Draw getDrawsOfAClass(Long classId) throws TeachToMeServiceException;

    /**
     * Añade un dibujo de una clase en Caché
     *
     * @param classId clase a la cual se añadira el dibujo en cache
     * @param draw    el dibujo a ser añadido en cache
     * @throws TeachToMeServiceException Cuando ocurre algún error a la hora de guardar el dibujo en cache
     */
    //void addDrawToCache(long classId, Draw draw) throws TeachToMeServiceException;

    /**
     * Elimina un dibujo de una clase en Caché
     *
     * @param classId clase a la cual se eliminara el dibujo en cache
     * @throws TeachToMeServiceException Cuando ocurre algún error a la hora de eliminar el dibujo en cache
     */
    //void deleteDrawFromCache(Long classId) throws TeachToMeServiceException;
}
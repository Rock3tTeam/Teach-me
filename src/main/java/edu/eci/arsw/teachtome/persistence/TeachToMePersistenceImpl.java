package edu.eci.arsw.teachtome.persistence;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.Point;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.RequestPK;
import edu.eci.arsw.teachtome.model.Session;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.persistence.repositories.ClaseRepository;
import edu.eci.arsw.teachtome.persistence.repositories.RequestRepository;
import edu.eci.arsw.teachtome.persistence.repositories.SessionRepository;
import edu.eci.arsw.teachtome.persistence.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de la capa de persistencia de la aplicación TeachToMe
 */
@Service
public class TeachToMePersistenceImpl implements TeachToMePersistence {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private SessionRepository sessionRepository;

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
        Optional<Clase> optionalClase = claseRepository.findById(id);
        if (optionalClase.isPresent()) {
            clase = optionalClase.get();
        }
        if (clase == null)
            throw new TeachToMePersistenceException(TeachToMePersistenceException.NON_EXISTING_CLASS + id);
        return clase;
    }

    /**
     * Agrega una nueva clase de un usuario dentro de la base de datos
     *
     * @param clase - La nueva clase que se va a agregar
     * @param user  - El usuario que va a dictar esa clase
     * @throws TeachToMePersistenceException - Cuando el usuario no exista en la base de datos o falte información obligatoria de la clase
     */
    @Override
    public void addClase(Clase clase, User user) throws TeachToMePersistenceException {
        Clase nullClass = claseRepository.getClaseByDescription(clase.getDescription());
        if (nullClass != null) {
            throw new TeachToMePersistenceException("No se puede insertar una clase con esa descripcion");
        }
        user.getTeachingClasses().add(clase);
        clase.setProfessor(user);
        userRepository.save(user);
        long claseId = claseRepository.getClaseByDescription(clase.getDescription()).getId();
        clase.setId(claseId);
        Session session = new Session(claseId);
        sessionRepository.save(session);
    }

    /**
     * Agrega una nueva clase de un usuario dentro de la base de datos
     *
     * @param clase - La clase a la cual el usuario se va a agregar
     * @param email - El mail del usuario del cual se van a obtener las clases
     * @throws TeachToMePersistenceException - Cuando el usuario no exista en la base de datos o la clase sea nula
     */
    @Override
    public void addStudentToAClass(Clase clase, String email) throws TeachToMePersistenceException {
        User user = getUser(email);
        if (clase == null) throw new TeachToMePersistenceException("La clase no puede ser nula");
        if (email.equals(clase.getProfessor().getEmail())) {
            throw new TeachToMePersistenceException("El profesor no puede ser añadido a su propia clase");
        }
        for (User student : clase.getStudents()) {
            if (student.getEmail().equals(email)) {
                throw new TeachToMePersistenceException("El usuario con el email " + email + " ya se encuentra en la clase");
            }
        }
        if (clase.isFull()) {
            throw new TeachToMePersistenceException("Esa clase ya no tiene cupos");
        }
        clase.getStudents().add(user);
        clase.increaseAmount();
        user.getStudyingClasses().add(clase);
        Request request = getRequest(clase.getId(), user.getEmail());
        request.setAccepted(true);
        claseRepository.save(clase);
        requestRepository.save(request);
    }

    /**
     * Consulta las clases que contengan cierta palabra
     *
     * @param nameFilter - nombre de la clase
     * @return La lista de clases que contengan esa palabra
     */
    @Override
    public List<Clase> getFilteredClassesByName(String nameFilter) {
        return claseRepository.filterByName(nameFilter);
    }

    /**
     * Agrega un nuevo usuario dentro de la base de datos
     *
     * @param user - Informacion del nuevo usuario
     * @throws TeachToMePersistenceException - Cuando la entidad ya existia en la base de datos
     */
    @Override
    public void addUser(User user) throws TeachToMePersistenceException {
        List<User> users = userRepository.getUserByEmail(user.getEmail());
        if (!users.isEmpty()) {
            throw new TeachToMePersistenceException("Ya existe un usuario con el email " + user.getEmail());
        }
        userRepository.save(user);
        User savedUser = getUser(user.getEmail());
        user.setId(savedUser.getId());
    }

    /**
     * Elimina una clase dentro de la base de datos
     *
     * @param classId - La clase a ser eliminada
     * @param email   - El usuario que eliminará la clase
     * @throws TeachToMePersistenceException - Cuando no existe la clase dentro de la base de datos o no la elimina su profesor
     */
    @Override
    public void deleteClass(long classId, String email) throws TeachToMePersistenceException {
        User user = getUser(email);
        Clase clase = getClase(classId);
        if (user.getId() != clase.getProfessor().getId()) {
            throw new TeachToMePersistenceException("El usuario no tiene permiso para eliminar esta clase");
        }
        claseRepository.deleteClass(clase.getId());
    }

    /**
     * Obtiene la informacion de un usuario de la base de datos por su email
     *
     * @param email - Email del Usuario
     * @return Entidad del Usuario con el email dado
     * @throws TeachToMePersistenceException - Cuando la entidad no existe en la base de datos
     */
    @Override
    public User getUser(String email) throws TeachToMePersistenceException {
        List<User> users = userRepository.getUserByEmail(email);
        if (users.isEmpty()) {
            throw new TeachToMePersistenceException("No existe el usuario con el email " + email);
        }
        return users.get(0);
    }

    /**
     * Obtiene la informacion de un usuario de la base de datos por su identificador
     *
     * @param id - Identificador del usuario
     * @return Entidad del Usuario con el identificador dado
     * @throws TeachToMePersistenceException - Cuando la entidad no existe en la base de datos
     */
    @Override
    public User getUserById(long id) throws TeachToMePersistenceException {
        User user = null;
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }
        if (user == null) throw new TeachToMePersistenceException("No existe el usuario con el id " + id);
        return user;
    }

    /**
     * Obtiene la informacion de una solicitud de una clase
     *
     * @param classId - Identificador de la clase
     * @param userId  - Identificador del solicitante
     * @return Solicitud de una clase por parte de un usuario
     * @throws TeachToMePersistenceException - Cuando alguna entidad no existe en la base de datos
     */
    @Override
    public Request getRequest(long classId, long userId) throws TeachToMePersistenceException {
        User user = getUserById(userId);
        Clase clase = getClase(classId);
        Request request = null;
        RequestPK requestPK = new RequestPK(user.getId(), clase.getId());
        Optional<Request> optionalRequest = requestRepository.findById(requestPK);
        if (optionalRequest.isPresent()) {
            request = optionalRequest.get();
        }
        if (request == null) {
            throw new TeachToMePersistenceException("No existe la solicitud de la clase " + classId + " por parte del usuario " + userId);
        }
        return request;
    }

    /**
     * Obtiene las clases que enseña un usuario
     *
     * @param email - El mail del usuario del cual se van a obtener las clases
     * @return Las clases que dicta el usuario
     * @throws TeachToMePersistenceException - Cuando el usuario no exista en la base de datos
     */
    @Override
    public List<Clase> getTeachingClassesOfUser(String email) throws TeachToMePersistenceException {
        User user = getUser(email);
        return user.getTeachingClasses();
    }

    /**
     * Realiza una request de un estudiante a una clase
     *
     * @param request - La request con el usuario y la clase a la cual quiere unirse
     * @throws TeachToMePersistenceException - Cuando el usuario o la clase no existan en la base de datos.
     */
    @Override
    public void sendRequest(Request request) throws TeachToMePersistenceException {
        User student = getUserById(request.getRequestId().getStudent());
        Clase clase = getClase(request.getRequestId().getClase());
        if (clase.getProfessor().getEmail().equals(student.getEmail())) {
            throw new TeachToMePersistenceException("El profesor no puede hacer un request a su misma clase");
        }
        Timestamp actualDate = new Timestamp(new Date().getTime());
        if (clase.getDateOfInit().before(actualDate)) {
            throw new TeachToMePersistenceException("El período de solicitudes de esta clase ya concluyó");
        }
        if (clase.isFull()) {
            throw new TeachToMePersistenceException("Esa clase ya no tiene cupos");
        }
        request.setClase(clase);
        request.setStudent(student);
        requestRepository.save(request);
    }

    /**
     * Obtiene las requests que se han hecho a una clase
     *
     * @param email   - El mail del usuario del cual se van a obtener las clases
     * @param classId - El id de la clase que se está buscando
     * @return Los requests que se han hecho a esa clase
     * @throws TeachToMePersistenceException - Cuando el usuario o la clase no existen en la base de datos
     */
    @Override
    public List<Request> getRequestsOfAClass(long classId, String email) throws TeachToMePersistenceException {
        Clase clase = getClase(classId);
        User professor = getUser(email);
        if (!(clase.getProfessor().getEmail().equals(professor.getEmail()))) {
            throw new TeachToMePersistenceException("No tiene permitido ver los requests a esta clase");
        }
        return requestRepository.getRequestsByClassId(classId);
    }

    /**
     * Obtiene las clases que aprende un usuario
     *
     * @param email - El mail del usuario del cual se van a obtener las clases
     * @return Las clases que dicta el usuario
     * @throws TeachToMePersistenceException - Cuando el usuario no exista en la base de datos
     */
    @Override
    public List<Clase> getClassesOfAStudent(String email) throws TeachToMePersistenceException {
        User user = getUser(email);
        return user.getStudyingClasses();
    }

    /**
     * Obtiene las request de un usuario a una clase
     *
     * @param emailStudent - El mail del usuario del cual se va a obtener el request
     * @param classId      - El id de la clase que se está buscando
     * @return La request de un estudiante a una clase
     * @throws TeachToMePersistenceException - Cuando el usuario no ha hecho request a esa clase
     */
    @Override
    public Request getRequest(long classId, String emailStudent) throws TeachToMePersistenceException {
        User student = getUser(emailStudent);
        Clase clase = getClase(classId);
        RequestPK requestPK = new RequestPK(student.getId(), clase.getId());
        Optional<Request> request = requestRepository.findById(requestPK);
        if (!(request.isPresent())) {
            throw new TeachToMePersistenceException("El usuario con el email " + emailStudent + " no ha solicitado unirse a la clase con el nombre " + clase.getNombre());
        }
        return request.get();
    }

    /**
     * Actualiza la request de una clase
     *
     * @param email   - El mail del usuario del cual se va a obtener el request
     * @param classId - El id de la clase que se está buscando
     * @throws TeachToMePersistenceException - En caso de que un usuario inautorizado intente actualizar el request
     */
    @Override
    public void updateRequest(Long classId, String email, Request request) throws TeachToMePersistenceException {
        User student = getUserById(request.getRequestId().getStudent());
        Clase clase = getClase(request.getRequestId().getClase());
        if (!(email.equals(clase.getProfessor().getEmail()))) {
            throw new TeachToMePersistenceException("No tiene permitido actualizar el request de esta clase");
        }
        boolean accepted = request.isAccepted();
        if (accepted) {
            addStudentToAClass(clase, student.getEmail());
        } else {
            Request newRequest = getRequest(clase.getId(), student.getEmail());
            newRequest.setAccepted(false);
            requestRepository.save(newRequest);
        }
    }

    /**
     * Inserta un mensaje dentro la base de datos del chat
     *
     * @param message - Nuevo Mensaje del chat
     * @param classId - Identificador de la clase
     * @param email   - Email del remitente
     * @throws TeachToMePersistenceException - Cuando no existe alguna entidad en la base de datos
     */
    @Override
    public void sendMessage(Message message, long classId, String email) throws TeachToMePersistenceException {
        Session session = sessionRepository.getSessionByClassId(classId);
        if (session == null) {
            throw new TeachToMePersistenceException(TeachToMePersistenceException.NON_EXISTING_CLASS + classId);
        }
        User user = getUser(email);
        Clase clase = getClase(classId);
        if (!(clase.getProfessor().equals(user)) && !(clase.hasStudent(user))) {
            throw new TeachToMePersistenceException("Este usuario no tiene acceso para publicar mensajes en este chat");
        }
        message.setActualDate();
        message.setSender(String.format("%s %s", user.getFirstName(), user.getLastName()));
        message.setSession(session);
        session.addMessage(message);
        sessionRepository.save(session);
    }

    /**
     * Obtiene los mensajes de una clase de la base de datos
     *
     * @param classId - Identificador de la clase
     * @param email   - Email del remitente
     * @return Coleccion con  los mensajes del chat
     * @throws TeachToMePersistenceException - Cuando no existe alguna entidad en la base de datos
     */
    @Override
    public List<Message> getChat(long classId, String email) throws TeachToMePersistenceException {
        Session session = sessionRepository.getSessionByClassId(classId);
        if (session == null) {
            throw new TeachToMePersistenceException(TeachToMePersistenceException.NON_EXISTING_CLASS + classId);
        }
        User user = getUser(email);
        Clase clase = getClase(classId);
        if (!(clase.getProfessor().equals(user)) && !(clase.hasStudent(user))) {
            throw new TeachToMePersistenceException("Este usuario no tiene acceso para publicar mensajes en este chat");
        }
        return session.getChat();
    }

    @Override
    public List<Draw> getDrawsOfAClass(long classId) throws TeachToMePersistenceException {
        Session session = sessionRepository.getSessionByClassId(classId);
        if (session == null) {
            throw new TeachToMePersistenceException(TeachToMePersistenceException.NON_EXISTING_CLASS + classId);
        }
        Timestamp dateOfLastDraw = session.getDateOfLastDraw();
        List<Draw> drawsOnTime = new ArrayList<>();
        List<Draw> draws = session.getDraws();
        for (Draw draw : draws) {
            if (draw.getDateOfDraw().equals(dateOfLastDraw)) {
                drawsOnTime.add(draw);
            }
        }
        return drawsOnTime;
    }

    @Override
    public void addPointsToDraw(List<Point> points, Draw draw) {
        for (Point point : points) {
            point.setDraw(draw);
        }
    }

    @Override
    public void addDrawToAClass(long classId, Draw draw, Timestamp date) throws TeachToMePersistenceException {
        Session session = sessionRepository.getSessionByClassId(classId);
        if (session == null) {
            throw new TeachToMePersistenceException(TeachToMePersistenceException.NON_EXISTING_CLASS + classId);
        }
        if(draw.getPoints()==null || draw.getPoints().isEmpty()){
            throw new TeachToMePersistenceException("Dibujo mal construido");
        }
        List<Draw> newDraws = session.getDraws();
        newDraws.add(draw);
        session.setDraws(newDraws);
        session.setDateOfLastDraw(date);
        draw.setSession(session);
        draw.setDateOfDraw(date);
        addPointsToDraw(draw.getPoints(), draw);
        sessionRepository.save(session);
    }
}
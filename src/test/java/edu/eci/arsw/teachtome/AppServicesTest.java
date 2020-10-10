package edu.eci.arsw.teachtome;

import edu.eci.arsw.teachtome.model.*;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.services.TeachToMeServicesInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
public class AppServicesTest implements ClassGenerator {

    @Autowired
    private TeachToMeServicesInterface services;


    @Test
    public void shouldNotGetAUserByEmail() {
        String email = "noexiste@gmail.com";
        try {
            services.getUser(email);
            fail("Debió fallar al buscar un usuario que no existe en la base de datos");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email " + email, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetAUserWithANullEmail() {
        try {
            services.getUser(null);
            fail("Debió fallar la buscar un usuario con email nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddANullUser() {
        try {
            services.addUser(null);
            fail("Debió fallar al intentar agregar un usuario nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El usuario no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldAddAndGetANewUser() throws TeachToMeServiceException {
        User user = new User("nuevo@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        services.addUser(user);
        User databaseUser = services.getUser("nuevo@gmail.com");
        assertEquals(user, databaseUser);
    }

    @Test
    public void shouldNotGetAClassById() {
        long id = 200;
        try {
            services.getClase(id);
            fail("Debió fallar al buscar una clase con id inexistente");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetAClassWithANullId() {
        try {
            services.getClase(null);
            fail("Debió fallar la buscar una clase con id nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El id no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddANullClass() {
        User user = addUser("badteacher@gmail.com");
        try {
            services.addClase(null, user);
            fail("Debió fallar por insertar una clase nula");
        } catch (TeachToMeServiceException e) {
            assertEquals("La clase no puede ser nula", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAClassWithANullTeacher() {
        Clase clase = getClase("Mala clase", "No debería insertar una clase con un profesor nulo");
        try {
            services.addClase(clase, null);
            fail("Debió fallar por insertar una clase con un profesor nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El usuario no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAClassWithAInvalidDate() {
        User user = addUser("nuevoB@gmail.com");
        Clase clase = getClaseAntigua("Clase con mal horario", "Mal horario");
        try {
            services.addClase(clase, user);
            fail("Debió fallar por insertar una clase que inicia antes de la hora actual");
        } catch (TeachToMeServiceException e) {
            assertEquals("No se puede programar una clase antes de la hora actual", e.getMessage());
        }
        clase = getClaseDesfasada("Clase desfasada", "Horario desfasado");
        try {
            services.addClase(clase, user);
            fail("Debió fallar por insertar una clase cuya hora de fin es previa a la hora de inicio");
        } catch (TeachToMeServiceException e) {
            assertEquals("Una clase no puede iniciar después de su fecha de finalización", e.getMessage());
        }
    }


    @Test
    public void shouldAddAndGetAClass() throws TeachToMeServiceException {
        Clase clase = getClase("Nueva Clase", "Prueba Exitosa de Inserción");
        String email = "nuevo@gmail.com";
        User user = addUser(email);
        services.addClase(clase, user);
        Clase clasePrueba = services.getClase(clase.getId());
        assertEquals(clase, clasePrueba);
    }

    @Test
    public void shouldNotGetTheClassesOfATeacherWithANullEmail() {
        try {
            services.getTeachingClassesOfUser(null);
            fail("Debió fallar por enviar un email nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheClassesOfANonExistingTeacher() {
        String email = "noexiste@gmail.com";
        try {
            services.getTeachingClassesOfUser(email);
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email " + email, e.getMessage());
        }
    }

    @Test
    public void shouldGetTheClassesOfATeacher() throws TeachToMeServiceException {
        List<Clase> classes = new ArrayList<>();
        Clase clase;
        String email = "julioprofe@gmail.com";
        User user = addUser(email);
        for (int i = 1; i < 3; i++) {
            clase = getClase("Matemática " + i, "Matemática " + i);
            classes.add(clase);
            services.addClase(clase, user);
        }
        List<Clase> returnedClasses = services.getTeachingClassesOfUser(email);
        IntStream.range(0, 2).forEach(i -> assertEquals(classes.get(i), returnedClasses.get(i)));
    }

    @Test
    public void shouldNotSendANullRequest() {
        try {
            services.sendRequest(null);
            fail("Debió fallar al hacer una solicitud nula");
        } catch (TeachToMeServiceException e) {
            assertEquals("La solicitud no puede ser nula", e.getMessage());
        }
    }

    @Test
    public void shouldNotSendAEmptyRequest() {
        try {
            services.sendRequest(new Request());
            fail("Debió fallar al hacer una solicitud vacía");
        } catch (TeachToMeServiceException e) {
            assertEquals("La solicitud no puede estar vacía", e.getMessage());
        }
    }

    @Test
    public void shouldNotSendARequestOfANonExistingStudent() {
        Clase clase = addClassAndTeacher("teacherG@gmail.com", "Clase G", "Clase G");
        try {
            services.sendRequest(new Request(new RequestPK(200, clase.getId())));
            fail("Debió fallar al enviar una solicitud con un estudiante que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el id " + 200, e.getMessage());
        }
    }

    @Test
    public void shouldNotSendARequestTheTeacherInHisClass() {
        String email = "teacherH@gmail.com";
        User user = addUser(email);
        Clase clase = addClass(user, "Clase H", "Clase H");
        try {
            services.sendRequest(new Request(new RequestPK(user.getId(), clase.getId())));
            fail("Debió fallar por enviar una solicitud como estudiante por parte de su profesor");
        } catch (TeachToMeServiceException e) {
            assertEquals("El profesor no puede hacer un request a su misma clase", e.getMessage());
        }
    }

    @Test
    public void shouldNotSendARequestWithANonExistingClass() {
        String email = "studentF@gmail.com";
        User user = addUser(email);
        long id = 200;
        try {
            services.sendRequest(new Request(new RequestPK(user.getId(), id)));
            fail("Debió fallar al enviar una solicitud a una clase que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotSendARequestOfAFullClass() {
        String email = "studentAI@gmail.com";
        Clase clase = addClassAndTeacher("teacherAI@gmail.com", "Clase AI", "Clase AI", 30, 30);
        User user = addUser(email);
        RequestPK requestPK = new RequestPK(user.getId(), clase.getId());
        ;
        Request request = new Request(requestPK);
        try {
            services.sendRequest(request);
            fail("Debió fallar por enviar a una solicitud a una clase sin cupos");
        } catch (TeachToMeServiceException e) {
            assertEquals("Esa clase ya no tiene cupos", e.getMessage());
        }
    }

    @Test
    public void shouldNotSendARequestOfAnAlreadyStartedClass() {
        String email = "studentAJ@gmail.com";
        Clase clase = addShortClassAndTeacher("teacherAJ@gmail.com", "Clase AJ", "Clase AJ");
        User user = addUser(email);
        RequestPK requestPK = new RequestPK(user.getId(), clase.getId());
        ;
        Request request = new Request(requestPK);
        waitAWhile();
        try {
            services.sendRequest(request);
            fail("Debió fallar por enviar a una solicitud a una clase que ya inició");
        } catch (TeachToMeServiceException e) {
            assertEquals("No se puede solicitar cupo para una clase que ya inició", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheRequestsOfANonExistingClass() {
        String email = "studentI@gmail.com";
        long id = 200;
        addUser(email);
        try {
            services.getRequestsOfAClass(id, email);
            fail("Debió fallar por buscar las solicitudes de una clase que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheRequestsOfANullUser() {
        Clase clase = addClassAndTeacher("teacherI@gmail.com", "Clase I", "Clase I");
        try {
            services.getRequestsOfAClass(clase.getId(), null);
            fail("Debió fallar por buscar las solicitudes con un email nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheRequestsOfANonExistingUser() {
        String email = "noexiste@gmail.com";
        Clase clase = addClassAndTeacher("teacherJ@gmail.com", "Clase J", "Clase J");
        try {
            services.getRequestsOfAClass(clase.getId(), email);
            fail("Debió fallar por buscar las solicitudes con un email no registrado");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email " + email, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheRequestsOfAClassWithoutBeingTheTeacher() {
        String email = "studentK@gmail.com";
        Clase clase = addClassAndTeacher("teacherK@gmail.com", "Clase K", "Clase K");
        addUser(email);
        try {
            services.getRequestsOfAClass(clase.getId(), email);
            fail("Debió fallar por buscar las solicitudes con un email distinto al del profesor");
        } catch (TeachToMeServiceException e) {
            assertEquals("No tiene permitido ver los requests a esta clase", e.getMessage());
        }
    }

    @Test
    public void shouldSendAndGetTheRequestsOfAClass() throws TeachToMeServiceException {
        String email = "studentL@gmail.com";
        Clase clase = addClassAndTeacher("teacherL@gmail.com", "Clase L", "Clase L");
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user.getId(), clase.getId());
        Request request = new Request(requestPK);
        boolean found = false;
        List<Request> requests = services.getRequestsOfAClass(clase.getId(), "teacherL@gmail.com");
        for (Request consultedRequest : requests) {
            if (consultedRequest.equals(request)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }


    @Test
    public void shouldNotAddAStudentToANullClass() {
        String email = "badstudent@gmail.com";
        addUser(email);
        try {
            services.addStudentToAClass(null, email);
            fail("Debió fallar al intentar añadir un estudiante a una clase nula");
        } catch (TeachToMeServiceException e) {
            assertEquals("La clase no puede ser nula", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddANonExistingStudentToAClass() {
        String email = "noexiste@gmail.com";
        Clase clase = addClassAndTeacher("sadteacher@gmail.com", "Clase vacía", "Clase sin alumnos");
        try {
            services.addStudentToAClass(clase, email);
            fail("Debió fallar al intentar añadir un estudiante que no existe a una clase");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email " + email, e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAStudentWithNullEmailToAClass() {
        Clase clase = addClassAndTeacher("teacherA@gmail.com", "Clase A", "Clase A");
        try {
            services.addStudentToAClass(clase, null);
            fail("Debió fallar al intentar añadir un estudiante que no existe a una clase");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddTheTeacherAsAStudentToAClass() {
        Clase clase = addClassAndTeacher("teacherB@gmail.com", "Clase B", "Clase B");
        try {
            services.addStudentToAClass(clase, "teacherB@gmail.com");
            fail("Debió fallar al intentar añadir el profesor como estudiante");
        } catch (TeachToMeServiceException e) {
            assertEquals("El profesor no puede ser añadido a su propia clase", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAnAlreadyEnrolledStudentToAClass() {
        String email = "studentC@gmail.com";
        Clase clase = addClassAndTeacher("teacherC@gmail.com", "Clase C", "Clase C");
        User user = addUser(email);
        sendRequest(user.getId(), clase.getId());
        try {
            services.addStudentToAClass(clase, email);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al realizar al agregar al estudiante");
        }
        try {
            services.addStudentToAClass(clase, email);
            fail("Debió fallar al intentar añadir un estudiante ya inscrito");
        } catch (TeachToMeServiceException e) {
            assertEquals("El usuario con el email " + email + " ya se encuentra en la clase", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddStudentToAClassThatDoNotRequestIt() {
        String email = "studentD@gmail.com";
        Clase clase = addClassAndTeacher("teacherD@gmail.com", "Clase D", "Clase D");
        addUser(email);
        try {
            services.addStudentToAClass(clase, email);
            fail("Debió fallar al intentar añadir un estudiante que no lo solicitó");
        } catch (TeachToMeServiceException e) {
            assertEquals("El usuario con el email " + email + " no ha solicitado unirse a la clase con el nombre " + clase.getNombre(), e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAStudentToAClassWithoutSeats() {
        String email = "studentW@gmail.com";
        String email2 = "studentW2@gmail.com";
        Clase clase = addClassAndTeacher("teacherW@gmail.com", "Clase W", "Clase W", 30, 29);
        User user1 = addUser(email);
        User user2 = addUser(email2);
        sendRequest(user1.getId(), clase.getId());
        sendRequest(user2.getId(), clase.getId());
        try {
            services.addStudentToAClass(clase, email);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al realizar al agregar el primer estudiante");
        }
        try {
            services.addStudentToAClass(clase, "studentW2@gmail.com");
            fail("Debió fallar por intentar añadir un estudiante en una clase sin cupos");
        } catch (TeachToMeServiceException e) {
            assertEquals("Esa clase ya no tiene cupos", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheClassesOfANonExistingStudent() {
        String email = "noexiste@gmail.com";
        try {
            services.getClassesOfAStudent(email);
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email " + email, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheClassesWithANullEmail() {
        try {
            services.getClassesOfAStudent(null);
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldAddStudentToAClass() throws TeachToMeServiceException {
        String email = "studentE@gmail.com";
        Clase clase = addClassAndTeacher("teacherE@gmail.com", "Clase E", "Clase E");
        User user = addUser(email);
        sendRequest(user.getId(), clase.getId());
        services.addStudentToAClass(clase, email);
        boolean found = false;
        List<Clase> studentClasses = services.getClassesOfAStudent(email);
        for (Clase enrolledClass : studentClasses) {
            if (enrolledClass.lazyEquals(clase)) {
                found = true;
                break;
            }
        }
        assertTrue(found);
        boolean foundRequest = false;
        List<Request> requests = services.getRequestsOfAClass(clase.getId(), "teacherE@gmail.com");
        for (Request request : requests) {
            if (request.getClase().lazyEquals(clase)) {
                foundRequest = true;
                assertTrue(request.isAccepted());
                break;
            }
        }
        assertTrue(foundRequest);
    }

    @Test
    public void shouldNotUpdateTheRequestOfAClassIfIsNotTheTeacher() {
        String email = "studentM@gmail.com";
        Clase clase = addClassAndTeacher("teacherM@gmail.com", "Clase M", "Clase M");
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user.getId(), clase.getId());
        Request request = new Request(requestPK);
        try {
            services.updateRequest(clase.getId(), email, request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase sin ser su profesor");
        } catch (TeachToMeServiceException e) {
            assertEquals("No tiene permitido actualizar el request de esta clase", e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateTheRequestOfANonExistingClass() {
        long id = 200;
        String email = "studentN@gmail.com";
        User user = addUser(email);
        RequestPK requestPK = new RequestPK(user.getId(), 200);
        Request request = new Request(requestPK, true);
        try {
            services.updateRequest(id, email, request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase sin ser su profesor");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateTheRequestOfAClassWithANullTeacherEmail() {
        String email = "studentO@gmail.com";
        Clase clase = addClassAndTeacher("teacherO@gmail.com", "Clase O", "Clase O");
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user.getId(), clase.getId());
        Request request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), null, request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con el email del maestro nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El correo del maestro no debe ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateTheRequestOfAClassWithANonExistingTeacher() {
        String email = "studentP@gmail.com";
        Clase clase = addClassAndTeacher("teacherP@gmail.com", "Clase P", "Clase P");
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user.getId(), clase.getId());
        Request request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), "noexiste@gmail.com", request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con el email de un maestro que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No tiene permitido actualizar el request de esta clase", e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateWithABadConstructRequest() {
        Clase clase = addClassAndTeacher("teacherQ@gmail.com", "Clase Q", "Clase Q");
        String email = "studentQ@gmail.com";
        User user = addUser(email);
        sendRequest(user.getId(), clase.getId());
        RequestPK requestPK = new RequestPK();
        Request request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), "teacherQ@gmail.com", request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con la petición sin email");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el id 0", e.getMessage());
        }
        requestPK = new RequestPK(user.getId(), 200);
        request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), "teacherQ@gmail.com", request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con la petición sin una clase existente");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + 200, e.getMessage());
        }
        requestPK = new RequestPK(200, clase.getId());
        request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), "teacherQ@gmail.com", request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con la petición sin una clase existente");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el id 200", e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateARequestIfTheClassDoNotHaveCapacity() {
        String email = "studentV@gmail.com";
        String email2 = "studentV2@gmail.com";
        Clase clase = addClassAndTeacher("teacherV@gmail.com", "Clase V", "Clase V", 30, 29);
        User user = addUser(email);
        User user2 = addUser(email2);
        Request request;
        sendRequest(user.getId(), clase.getId());
        RequestPK requestPK = sendRequest(user2.getId(), clase.getId());
        try {
            request = new Request(requestPK, true);
            services.updateRequest(clase.getId(), "teacherV@gmail.com", request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al realizar las solicitudes");
        }
        requestPK = new RequestPK(user.getId(), clase.getId());
        request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), "teacherV@gmail.com", request);
            fail("Debió fallar al intentar aceptar un estudiante en una clase sin cupos");
        } catch (TeachToMeServiceException e) {
            assertEquals("Esa clase ya no tiene cupos", e.getMessage());
        }
    }

    @Test
    public void shouldUpdateARequestToTrue() throws TeachToMeServiceException {
        Clase clase = addClassAndTeacher("teacherR@gmail.com", "Clase R", "Clase R");
        String email = "studentR@gmail.com";
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user.getId(), clase.getId());
        Request request = new Request(requestPK, true);
        services.updateRequest(clase.getId(), "teacherR@gmail.com", request);
        boolean foundRequest = false;
        List<Request> requests = services.getRequestsOfAClass(clase.getId(), "teacherR@gmail.com");
        for (Request returnedRequest : requests) {
            if (returnedRequest.getClase().lazyEquals(clase)) {
                foundRequest = true;
                assertTrue(request.isAccepted());
                assertEquals(user.getId(), request.getRequestId().getStudent());
                break;
            }
        }
        assertTrue(foundRequest);
    }

    @Test
    public void shouldUpdateARequestToFalse() throws TeachToMeServiceException {
        Clase clase = addClassAndTeacher("teacherS@gmail.com", "Clase S", "Clase S");
        String email = "studentS@gmail.com";
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user.getId(), clase.getId());
        Request request = new Request(requestPK, false);
        services.updateRequest(clase.getId(), "teacherS@gmail.com", request);
        boolean foundRequest = false;
        List<Request> requests = services.getRequestsOfAClass(clase.getId(), "teacherS@gmail.com");
        for (Request returnedRequest : requests) {
            if (returnedRequest.getClase().lazyEquals(clase)) {
                foundRequest = true;
                assertFalse(request.isAccepted());
                assertEquals(user.getId(), request.getRequestId().getStudent());
                break;
            }
        }
        assertTrue(foundRequest);
    }

    @Test
    public void shouldNotConsultTheClassesByNameWithoutTheFilter() {
        try {
            services.getFilteredClassesByName(null);
            fail("Debió fallar por usar un filtro nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El nombre no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldConsultTheClassesByName() throws TeachToMeServiceException {
        addClassAndTeacher("teacherT@gmail.com", "Ejemplo T", "Ejemplo T", 30, 0);
        addClassAndTeacher("teacherU@gmail.com", "Ejemplo U", "Ejemplo U", 20, 0);
        String nameFilter = "Ejemplo";
        List<Clase> clases = services.getFilteredClassesByName(nameFilter);
        for (Clase returnedClass : clases) {
            assertTrue(returnedClass.getNombre().contains(nameFilter));
        }
    }

    @Test
    public void shouldNotGetARequestOfANonExistingUser() {
        Clase clase = addClassAndTeacher("teacherX@gmail.com", "Clase X", "Clase X");
        long id = 200;
        try {
            services.getRequest(clase.getId(), id);
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetARequestOfANonExistingClass() {
        User user = addUser("studentX@gmail.com");
        long id = 200;
        try {
            services.getRequest(id, user.getId());
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetANonExistingRequest() {
        Clase clase = addClassAndTeacher("teacherAL@gmail.com", "Clase AL", "Clase AL");
        User user = addUser("studentAL@gmail.com");
        try {
            services.getRequest(clase.getId(), user.getId());
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la solicitud de la clase " + clase.getId() + " por parte del usuario " + user.getId(), e.getMessage());
        }
    }

    @Test
    public void shouldNotGetARequestWithNullParameters() {
        User user = addUser("studentY@gmail.com");
        Clase clase = addClassAndTeacher("teacherY@gmail.com", "Clase Y", "Clase Y");
        try {
            services.getRequest(clase.getId(), null);
        } catch (TeachToMeServiceException e) {
            assertEquals("Los identificadores no pueden ser nulos", e.getMessage());
        }
        try {
            services.getRequest(null, user.getId());
        } catch (TeachToMeServiceException e) {
            assertEquals("Los identificadores no pueden ser nulos", e.getMessage());
        }
    }

    @Test
    public void shouldGetARequest() throws TeachToMeServiceException {
        User user = addUser("studentZ@gmail.com");
        Clase clase = addClassAndTeacher("teacherZ@gmail.com", "Clase Z", "Clase Z");
        RequestPK requestPK = sendRequest(user.getId(), clase.getId());
        Request expectedRequest = new Request(requestPK);
        Request request = services.getRequest(clase.getId(), user.getId());
        assertEquals(expectedRequest, request);
    }

    @Test
    public void shouldNotDeleteAClassIfTheUserIsNotTheTeacher() {
        String email = "teacherAC@gmail.com";
        User teacher = addUser(email);
        User user = addUser("studentAC@gmail.com");
        Clase clase = addClass(teacher, "Clase AC", "Clase AC");
        try {
            services.deleteClass(clase.getId(), "studentAC@gmail.com");
            fail("Debió fallar por intentar eliminar una clase sin ser su profesor");
        } catch (TeachToMeServiceException e) {
            assertEquals("El usuario no tiene permiso para eliminar esta clase", e.getMessage());
        }
    }

    @Test
    public void shouldDeleteAClass() throws TeachToMeServiceException {
        String email = "teacherAA@gmail.com";
        User teacher = addUser(email);
        Clase clase = addClass(teacher, "Clase AA", "Clase AA");
        Clase clase2 = addClass(teacher, "Clase AB", "Clase AB");
        List<Clase> originalClasses = services.getTeachingClassesOfUser(email);
        services.deleteClass(clase.getId(), email);
        List<Clase> classes = services.getTeachingClassesOfUser(email);
        assertEquals(originalClasses.size() - 1, classes.size());
        assertEquals(clase2, classes.get(0));
        services.deleteClass(clase2.getId(), email);
        List<Clase> emptyClasses = services.getTeachingClassesOfUser(email);
        assertTrue(emptyClasses.isEmpty());
    }

    @Test
    public void shouldNotSendAMessageToANonExistingClass() {
        long id = 200;
        Message message = new Message("hola");
        try {
            services.sendMessage(message, id, "aaa");
            fail("Debió fallar por enviar un mensaje a una clase que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotSendAMessageWithANonExistingUser() {
        String email = "noexiste@gmail.com";
        Clase clase = addClassAndTeacher("teacherAD@gmail.com", "Clase AD", "Clase AD");
        Message message = new Message("hola");
        try {
            services.sendMessage(message, clase.getId(), email);
            fail("Debió fallar por enviar un mensaje con un usuario que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email " + email, e.getMessage());
        }
    }

    @Test
    public void shouldNotSendAMessageWithAUserThatIsNotPartOfTheClass() throws TeachToMeServiceException {
        String email = "studentAE@gmail.com";
        Clase clase = addClassAndTeacher("teacherAE@gmail.com", "Clase AE", "Clase AE");
        addUser(email);
        Message message = new Message("hola");
        try {
            services.sendMessage(message, clase.getId(), email);
            fail("Debió fallar por enviar un mensaje con un usuario que no pertenece a la clase");
        } catch (TeachToMeServiceException e) {
            assertEquals("Este usuario no tiene acceso para publicar mensajes en este chat", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheChatWithANonExistingUser() {
        String email = "teacherAF@gmail.com";
        Clase clase = addClassAndTeacher(email, "Clase AF", "Clase AF");
        Message message = new Message("hola estudiantes");
        try {
            services.sendMessage(message, clase.getId(), email);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al enviar el mensaje");
        }
        try {
            services.getChat(clase.getId(), "noexiste@gmail.com");
            fail("Debió fallar por consultar los mensajes de un chat con un usuario que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email noexiste@gmail.com", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheChatOfANonExistingClass() {
        long id = 200;
        String email = "teacherAG@gmail.com";
        Clase clase = addClassAndTeacher(email, "Clase AG", "Clase AG");
        Message message = new Message("hola estudiantes");
        try {
            services.sendMessage(message, clase.getId(), email);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al enviar el mensaje");
        }
        try {
            services.getChat(id, email);
            fail("Debió fallar por consultar los mensajes de un chat que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheChatWithAUserThatIsNotPartOfTheClass() {
        String email = "teacherAF@gmail.com";
        addUser("studentAF@gmail.com");
        Clase clase = addClassAndTeacher(email, "Clase AF", "Clase AF");
        Message message = new Message("hola estudiantes");
        try {
            services.sendMessage(message, clase.getId(), email);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al enviar el mensaje");
        }
        try {
            services.getChat(clase.getId(), "studentAF@gmail.com");
            fail("Debió fallar por consultar los mensajes de un chat que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("Este usuario no tiene acceso para publicar mensajes en este chat", e.getMessage());
        }
    }

    @Test
    public void shouldSendAndGetMessages() throws TeachToMeServiceException {
        String email = "teacherAF@gmail.com";
        User user = addUser("studentAF@gmail.com");
        Clase clase = addClassAndTeacher(email, "Clase AF", "Clase AF");
        Message message = new Message("hola estudiantes");
        services.sendMessage(message, clase.getId(), email);
        List<Message> messages = services.getChat(clase.getId(), email);
        assertEquals(1, messages.size());
        assertEquals(message, messages.get(0));
        RequestPK requestPK = sendRequest(user.getId(), clase.getId());
        Request request = new Request(requestPK, true);
        services.updateRequest(clase.getId(), email, request);
        Message message2 = new Message("hola profe");
        services.sendMessage(message2, clase.getId(), "studentAF@gmail.com");
        messages = services.getChat(clase.getId(), email);
        assertEquals(2, messages.size());
        assertEquals(message2, messages.get(1));
        messages = services.getChat(clase.getId(), "studentAF@gmail.com");
        assertEquals(2, messages.size());
        assertEquals(message2, messages.get(1));
    }

    private Clase addClass(User user, String className, String classDescription) {
        Clase clase = getClase(className, classDescription);
        try {
            services.addClase(clase, user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar la clase");
        }
        return clase;
    }

    private Clase addClassAndTeacher(String teacherEmail, String className, String classDescription) {
        User user = new User(teacherEmail, "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClase(className, classDescription);
        try {
            services.addUser(user);
            services.addClase(clase, user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al profesor ni a su clase");
        }
        return clase;
    }

    private Clase addClassAndTeacher(String teacherEmail, String className, String classDescription, int capacity, int amount) {
        User user = new User(teacherEmail, "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClase(className, classDescription, capacity, amount);
        try {
            services.addUser(user);
            services.addClase(clase, user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al profesor ni a su clase");
        }
        return clase;
    }

    private Clase addShortClassAndTeacher(String teacherEmail, String className, String classDescription) {
        User user = new User(teacherEmail, "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClassOfShortDuration(className, classDescription, 30, 0);
        try {
            services.addUser(user);
            services.addClase(clase, user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al profesor ni a su clase");
        }
        return clase;
    }

    private RequestPK sendRequest(long userId, long classId) {
        RequestPK requestPK = null;
        Request request;
        try {
            requestPK = new RequestPK(userId, classId);
            request = new Request(requestPK);
            services.sendRequest(request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al realizar la solicitud");
        }
        return requestPK;
    }

    private User addUser(String email) {
        User user = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        try {
            services.addUser(user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al usuario");
        }
        return user;
    }

    private void waitAWhile() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

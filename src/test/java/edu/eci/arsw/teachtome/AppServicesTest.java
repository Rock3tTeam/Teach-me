package edu.eci.arsw.teachtome;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.RequestPK;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.services.TeachToMeServicesInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
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
        User user = new User("badteacher@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        try {
            services.addUser(user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar por agregar un nuevo usuario");
        }
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
        User user = new User("nuevo@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClaseAntigua("Clase con mal horario", "Mal horario");
        try {
            services.addUser(user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al agregar al usuario");
        }
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
        User user = new User("nuevo@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        services.addUser(user);
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
        User user = new User("julioprofe@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        services.addUser(user);
        for (int i = 1; i < 3; i++) {
            clase = getClase("Matemática " + i, "Matemática " + i);
            classes.add(clase);
            services.addClase(clase, user);
        }
        List<Clase> returnedClasses = services.getTeachingClassesOfUser("julioprofe@gmail.com");
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
    public void shouldNotSendARequestWithANullStudent() {
        Clase clase = addClassAndTeacher("teacherF@gmail.com","Clase F","Clase F");
        try {
            services.sendRequest(new Request(new RequestPK(null, clase.getId())));
            fail("Debió fallar al enviar una solicitud sin estudiante");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotSendARequestOfANonExistingStudent() {
        String email = "noexiste@gmail.com";
        Clase clase = addClassAndTeacher("teacherG@gmail.com","Clase G","Clase G");
        try {
            services.sendRequest(new Request(new RequestPK(email, clase.getId())));
            fail("Debió fallar al enviar una solicitud con un estudiante que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email " + email, e.getMessage());
        }
    }

    @Test
    public void shouldNotSendARequestTheTeacherInHisClass() {
        String email = "teacherH@gmail.com";
        Clase clase = addClassAndTeacher(email,"Clase H","Clase H");
        try {
            services.sendRequest(new Request(new RequestPK(email, clase.getId())));
            fail("Debió fallar por enviar una solicitud como estudiante por parte de su profesor");
        } catch (TeachToMeServiceException e) {
            assertEquals("El profesor no puede hacer un request a su misma clase", e.getMessage());
        }
    }

    @Test
    public void shouldNotSendARequestWithANonExistingClass() {
        String email = "studentF@gmail.com";
        long id = 200;
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        try {
            services.addUser(student);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al estudiante");
        }
        try {
            services.sendRequest(new Request(new RequestPK(email, id)));
            fail("Debió fallar al enviar una solicitud a una clase que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheRequestsOfANonExistingClass() {
        String email = "studentI@gmail.com";
        long id = 200;
        User user = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        try {
            services.addUser(user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al usuario");
        }
        try {
            services.getRequestsOfAClass(id, email);
            fail("Debió fallar por buscar las solicitudes de una clase que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheRequestsOfANullUser() {
        Clase clase = addClassAndTeacher("teacherI@gmail.com","Clase I","Clase I");
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
        Clase clase = addClassAndTeacher("teacherJ@gmail.com","Clase J","Clase J");
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
        Clase clase = addClassAndTeacher("teacherK@gmail.com","Clase K","Clase K");
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        try {
            services.addUser(student);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al estudiante");
        }
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
        Clase clase = addClassAndTeacher("teacherL@gmail.com","Clase L","Clase L");
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK;
        Request request = new Request();
        try {
            services.addUser(student);
            requestPK = new RequestPK(email, clase.getId());
            request = new Request(requestPK);
            services.sendRequest(request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar el estudiante ni al solicitar unirse a la clase");
        }
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
        User user = new User("badstudent@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        try {
            services.addUser(user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al insertar el usuario");
        }
        try {
            services.addStudentToAClass(null, "badstudent@gmail.com");
            fail("Debió fallar al intentar añadir un estudiante a una clase nula");
        } catch (TeachToMeServiceException e) {
            assertEquals("La clase no puede ser nula", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddANonExistingStudentToAClass() {
        String email = "noexiste@gmail.com";
        Clase clase = addClassAndTeacher("sadteacher@gmail.com","Clase vacía","Clase sin alumnos");
        try {
            services.addStudentToAClass(clase, email);
            fail("Debió fallar al intentar añadir un estudiante que no existe a una clase");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email " + email, e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAStudentWithNullEmailToAClass() {
        Clase clase = addClassAndTeacher("teacherA@gmail.com","Clase A","Clase A");
        try {
            services.addStudentToAClass(clase, null);
            fail("Debió fallar al intentar añadir un estudiante que no existe a una clase");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddTheTeacherAsAStudentToAClass() {
        Clase clase = addClassAndTeacher("teacherB@gmail.com","Clase B","Clase B");
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
        Clase clase = addClassAndTeacher("teacherC@gmail.com","Clase C","Clase C");
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK;
        try {
            services.addUser(student);
            requestPK = new RequestPK(email, clase.getId());
            services.sendRequest(new Request(requestPK));
            services.addStudentToAClass(clase, email);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar el estudiante ni al solicitar unirse a la clase");
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
        Clase clase = addClassAndTeacher("teacherD@gmail.com","Clase D","Clase D");
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        try {
            services.addUser(student);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al estudiante");
        }
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
        Clase clase = getClase("Clase W", "Clase W", 30, 29);
        User user = new User("teacherW@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        User student2 = new User("studentW2@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK;
        try {
            services.addUser(user);
            services.addUser(student);
            services.addUser(student2);
            services.addClase(clase, user);
            requestPK = new RequestPK(email, clase.getId());
            services.sendRequest(new Request(requestPK));
            requestPK = new RequestPK("studentW2@gmail.com", clase.getId());
            services.sendRequest(new Request(requestPK));
            services.addStudentToAClass(clase, email);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar los recursos");
        }
        try {
            services.addStudentToAClass(clase, "studentW2@gmail.com");
            fail("Debió fallar por intentar añadir un estudiante en una clase sin cupos");
        } catch (TeachToMeServiceException e) {
            assertEquals("Esa clase ya no tiene cupos", e.getMessage());
        }
    }

    @Test
    public void shouldAddStudentToAClass() throws TeachToMeServiceException {
        String email = "studentE@gmail.com";
        Clase clase = addClassAndTeacher("teacherE@gmail.com","Clase E","Clase E");
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK;
        try {
            services.addUser(student);
            requestPK = new RequestPK(email, clase.getId());
            services.sendRequest(new Request(requestPK));
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar el estudiante ni al solicitar unirse a la clase");
        }
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
        Clase clase = addClassAndTeacher("teacherM@gmail.com","Clase M","Clase M");
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK;
        Request request = null;
        try {
            services.addUser(student);
            requestPK = new RequestPK(email, clase.getId());
            request = new Request(requestPK);
            services.sendRequest(request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar el estudiante ni al solicitar unirse a la clase");
        }
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
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK = new RequestPK(email, 200);
        Request request = new Request(requestPK, true);
        try {
            services.addUser(student);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al usuario");
        }
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
        Clase clase = addClassAndTeacher("teacherO@gmail.com","Clase O","Clase O");
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK = null;
        Request request = null;
        try {
            services.addUser(student);
            requestPK = new RequestPK(email, clase.getId());
            request = new Request(requestPK);
            services.sendRequest(request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar el estudiante ni al solicitar unirse a la clase");
        }
        request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), null, request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con el amil del maestro nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El correo del maestro no debe ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateTheRequestOfAClassWithANonExistingTeacher() {
        String email = "studentP@gmail.com";
        Clase clase = addClassAndTeacher("teacherP@gmail.com","Clase P","Clase P");
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK = null;
        Request request = null;
        try {
            services.addUser(student);
            requestPK = new RequestPK(email, clase.getId());
            request = new Request(requestPK);
            services.sendRequest(request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar el estudiante ni al solicitar unirse a la clase");
        }
        request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), "noexiste@gmail.com", request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con el email de un maestro que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No tiene permitido actualizar el request de esta clase", e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateWithABadConstructRequest() {
        Clase clase = addClassAndTeacher("teacherQ@gmail.com","Clase Q","Clase Q");
        String email = "studentQ@gmail.com";
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK = null;
        Request request = null;
        try {
            services.addUser(student);
            requestPK = new RequestPK(email, clase.getId());
            request = new Request(requestPK);
            services.sendRequest(request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar el estudiante ni al solicitar unirse a la clase");
        }
        requestPK = new RequestPK();
        request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), "teacherQ@gmail.com", request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con la petición sin email");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
        requestPK = new RequestPK(email, 200);
        request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), "teacherQ@gmail.com", request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con la petición sin una clase existente");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + 200, e.getMessage());
        }
        requestPK = new RequestPK("noexiste@gmail.com", clase.getId());
        request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), "teacherQ@gmail.com", request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con la petición sin una clase existente");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email noexiste@gmail.com", e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateARequestIfTheClassDoNotHaveCapacity() {
        String email = "studentV@gmail.com";
        Clase clase = getClase("Clase V", "Clase V", 30, 29);
        User user = new User("teacherV@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        User student2 = new User("studentV2@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK = null;
        Request request = null;
        try {
            services.addUser(user);
            services.addUser(student);
            services.addUser(student2);
            services.addClase(clase, user);
            requestPK = new RequestPK(email, clase.getId());
            request = new Request(requestPK);
            services.sendRequest(request);
            requestPK = new RequestPK("studentV2@gmail.com", clase.getId());
            request = new Request(requestPK);
            services.sendRequest(request);
            request = new Request(requestPK, true);
            services.updateRequest(clase.getId(), "teacherV@gmail.com", request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar los recursos iniciales");
        }
        requestPK = new RequestPK(email, clase.getId());
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
        Clase clase = addClassAndTeacher("teacherR@gmail.com","Clase R","Clase R");
        String email = "studentR@gmail.com";;
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK = null;
        Request request = null;
        try {
            services.addUser(student);
            requestPK = new RequestPK(email, clase.getId());
            request = new Request(requestPK);
            services.sendRequest(request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar el estudiante ni al solicitar unirse a la clase");
        }
        request = new Request(requestPK, true);
        services.updateRequest(clase.getId(), "teacherR@gmail.com", request);
        boolean foundRequest = false;
        List<Request> requests = services.getRequestsOfAClass(clase.getId(), "teacherR@gmail.com");
        for (Request returnedRequest : requests) {
            if (returnedRequest.getClase().lazyEquals(clase)) {
                foundRequest = true;
                assertTrue(request.isAccepted());
                assertEquals(student.getEmail(), request.getRequestId().getStudent());
                break;
            }
        }
        assertTrue(foundRequest);
    }

    @Test
    public void shouldUpdateARequestToFalse() throws TeachToMeServiceException {
        Clase clase = addClassAndTeacher("teacherS@gmail.com","Clase S","Clase S");
        String email = "studentS@gmail.com";
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        RequestPK requestPK = null;
        Request request = null;
        try {
            services.addUser(student);
            requestPK = new RequestPK(email, clase.getId());
            request = new Request(requestPK);
            services.sendRequest(request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar el estudiante ni al solicitar unirse a la clase");
        }
        request = new Request(requestPK, false);
        services.updateRequest(clase.getId(), "teacherS@gmail.com", request);
        boolean foundRequest = false;
        List<Request> requests = services.getRequestsOfAClass(clase.getId(), "teacherS@gmail.com");
        for (Request returnedRequest : requests) {
            if (returnedRequest.getClase().lazyEquals(clase)) {
                foundRequest = true;
                assertFalse(request.isAccepted());
                assertEquals(student.getEmail(), request.getRequestId().getStudent());
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
        addClassAndTeacher("teacherT@gmail.com","Ejemplo T","Ejemplo T");
        addClassAndTeacher("teacherU@gmail.com","Ejemplo U","Ejemplo U");
        String nameFilter = "Ejemplo";
        List<Clase> clases = services.getFilteredClassesByName(nameFilter);
        for (Clase returnedClass : clases) {
            assertTrue(returnedClass.getNombre().contains(nameFilter));
        }
    }

    private Clase addClassAndTeacher(String teacherEmail, String className, String classDescription){
        User user = new User(teacherEmail, "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClase(className, classDescription);
        try {
            services.addUser(user);
            services.addClase(clase, user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar a los usuarios ni las clases");
        }
        return clase;
    }
}

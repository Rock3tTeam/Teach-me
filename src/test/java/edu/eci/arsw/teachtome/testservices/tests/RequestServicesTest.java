package edu.eci.arsw.teachtome.testservices.tests;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.RequestPK;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.testservices.BasicServicesTestsUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
public class RequestServicesTest extends BasicServicesTestsUtilities {

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
    public void shouldNotSendARequestOfANonExistingStudent() throws Exception {
        Clase clase = addClassAndTeacher("teacherG@gmail.com", "Clase G", "Clase G");
        try {
            services.sendRequest(new Request(new RequestPK(200, clase.getId())));
            fail("Debió fallar al enviar una solicitud con un estudiante que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el id " + 200, e.getMessage());
        }
    }

    @Test
    public void shouldNotSendARequestTheTeacherInHisClass() throws Exception {
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
    public void shouldNotSendARequestWithANonExistingClass() throws Exception {
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
    public void shouldNotSendARequestOfAFullClass() throws Exception {
        String email = "studentAI@gmail.com";
        Clase clase = addClassAndTeacher("teacherAI@gmail.com", "Clase AI", "Clase AI", 30, 30);
        User user = addUser(email);
        RequestPK requestPK = new RequestPK(user.getId(), clase.getId());
        Request request = new Request(requestPK);
        try {
            services.sendRequest(request);
            fail("Debió fallar por enviar a una solicitud a una clase sin cupos");
        } catch (TeachToMeServiceException e) {
            assertEquals("Esa clase ya no tiene cupos", e.getMessage());
        }
    }

    @Test
    public void shouldNotSendARequestOfAnAlreadyStartedClass() throws Exception {
        String email = "studentAJ@gmail.com";
        Clase clase = addShortClassAndTeacher("teacherAJ@gmail.com", "Clase AJ", "Clase AJ");
        User user = addUser(email);
        RequestPK requestPK = new RequestPK(user.getId(), clase.getId());
        Request request = new Request(requestPK);
        waitAWhile();
        try {
            services.sendRequest(request);
            fail("Debió fallar por enviar a una solicitud a una clase que ya inició");
        } catch (TeachToMeServiceException e) {
            assertEquals("El período de solicitudes de esta clase ya concluyó", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheRequestsOfANonExistingClass() throws Exception {
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
    public void shouldNotGetTheRequestsOfANullUser() throws Exception {
        Clase clase = addClassAndTeacher("teacherI@gmail.com", "Clase I", "Clase I");
        try {
            services.getRequestsOfAClass(clase.getId(), null);
            fail("Debió fallar por buscar las solicitudes con un email nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheRequestsOfANonExistingUser() throws Exception {
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
    public void shouldNotGetTheRequestsOfAClassWithoutBeingTheTeacher() throws Exception {
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
    public void shouldSendAndGetTheRequestsOfAClass() throws Exception {
        String email = "studentL@gmail.com";
        Clase clase = addClassAndTeacher("teacherL@gmail.com", "Clase L", "Clase L");
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user, clase.getId());
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
    public void shouldNotAddAStudentToANullClass() throws Exception {
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
    public void shouldNotAddANonExistingStudentToAClass() throws Exception {
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
    public void shouldNotAddAStudentWithNullEmailToAClass() throws Exception {
        Clase clase = addClassAndTeacher("teacherA@gmail.com", "Clase A", "Clase A");
        try {
            services.addStudentToAClass(clase, null);
            fail("Debió fallar al intentar añadir un estudiante que no existe a una clase");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddTheTeacherAsAStudentToAClass() throws Exception {
        Clase clase = addClassAndTeacher("teacherB@gmail.com", "Clase B", "Clase B");
        try {
            services.addStudentToAClass(clase, "teacherB@gmail.com");
            fail("Debió fallar al intentar añadir el profesor como estudiante");
        } catch (TeachToMeServiceException e) {
            assertEquals("El profesor no puede ser añadido a su propia clase", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAnAlreadyEnrolledStudentToAClass() throws Exception {
        String email = "studentC@gmail.com";
        Clase clase = addClassAndTeacher("teacherC@gmail.com", "Clase C", "Clase C");
        User user = addUser(email);
        sendRequest(user, clase.getId());
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
    public void shouldNotAddStudentToAClassThatDoNotRequestIt() throws Exception {
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
    public void shouldNotAddAStudentToAClassWithoutSeats() throws Exception {
        String email = "studentW@gmail.com";
        String email2 = "studentW2@gmail.com";
        Clase clase = addClassAndTeacher("teacherW@gmail.com", "Clase W", "Clase W", 30, 29);
        User user1 = addUser(email);
        User user2 = addUser(email2);
        sendRequest(user1, clase.getId());
        sendRequest(user2, clase.getId());
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
    public void shouldAddStudentToAClass() throws Exception {
        String email = "studentE@gmail.com";
        Clase clase = addClassAndTeacher("teacherE@gmail.com", "Clase E", "Clase E");
        User user = addUser(email);
        sendRequest(user, clase.getId());
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
    public void shouldNotUpdateTheRequestOfAClassIfIsNotTheTeacher() throws Exception {
        String email = "studentM@gmail.com";
        Clase clase = addClassAndTeacher("teacherM@gmail.com", "Clase M", "Clase M");
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user, clase.getId());
        Request request = new Request(requestPK);
        try {
            services.updateRequest(clase.getId(), email, request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase sin ser su profesor");
        } catch (TeachToMeServiceException e) {
            assertEquals("No tiene permitido actualizar el request de esta clase", e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateTheRequestOfANonExistingClass() throws Exception {
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
    public void shouldNotUpdateTheRequestOfAClassWithANullTeacherEmail() throws Exception {
        String email = "studentO@gmail.com";
        Clase clase = addClassAndTeacher("teacherO@gmail.com", "Clase O", "Clase O");
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user, clase.getId());
        Request request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), null, request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con el email del maestro nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El correo del maestro no debe ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateTheRequestOfAClassWithANonExistingTeacher() throws Exception {
        String email = "studentP@gmail.com";
        Clase clase = addClassAndTeacher("teacherP@gmail.com", "Clase P", "Clase P");
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user, clase.getId());
        Request request = new Request(requestPK, true);
        try {
            services.updateRequest(clase.getId(), "noexiste@gmail.com", request);
            fail("Debió fallar al intentar actualizar la solicitud de una clase con el email de un maestro que no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("No tiene permitido actualizar el request de esta clase", e.getMessage());
        }
    }

    @Test
    public void shouldNotUpdateWithABadConstructRequest() throws Exception {
        Clase clase = addClassAndTeacher("teacherQ@gmail.com", "Clase Q", "Clase Q");
        String email = "studentQ@gmail.com";
        User user = addUser(email);
        sendRequest(user, clase.getId());
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
    public void shouldNotUpdateARequestIfTheClassDoNotHaveCapacity() throws Exception {
        String email = "studentV@gmail.com";
        String email2 = "studentV2@gmail.com";
        Clase clase = addClassAndTeacher("teacherV@gmail.com", "Clase V", "Clase V", 30, 29);
        User user = addUser(email);
        User user2 = addUser(email2);
        Request request;
        sendRequest(user, clase.getId());
        RequestPK requestPK = sendRequest(user2, clase.getId());
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
    public void shouldUpdateARequestToTrue() throws Exception {
        Clase clase = addClassAndTeacher("teacherR@gmail.com", "Clase R", "Clase R");
        String email = "studentR@gmail.com";
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user, clase.getId());
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
    public void shouldUpdateARequestToFalse() throws Exception {
        Clase clase = addClassAndTeacher("teacherS@gmail.com", "Clase S", "Clase S");
        String email = "studentS@gmail.com";
        User user = addUser(email);
        RequestPK requestPK = sendRequest(user, clase.getId());
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
    public void shouldNotGetARequestOfANonExistingUser() throws Exception {
        Clase clase = addClassAndTeacher("teacherX@gmail.com", "Clase X", "Clase X");
        long id = 200;
        try {
            services.getRequest(clase.getId(), id);
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetARequestOfANonExistingClass() throws Exception {
        User user = addUser("studentX@gmail.com");
        long id = 200;
        try {
            services.getRequest(id, user.getId());
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetANonExistingRequest() throws Exception {
        Clase clase = addClassAndTeacher("teacherAL@gmail.com", "Clase AL", "Clase AL");
        User user = addUser("studentAL@gmail.com");
        try {
            services.getRequest(clase.getId(), user.getId());
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la solicitud de la clase " + clase.getId() + " por parte del usuario " + user.getId(), e.getMessage());
        }
    }

    @Test
    public void shouldNotGetARequestWithNullParameters() throws Exception {
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
    public void shouldGetARequest() throws Exception {
        User user = addUser("studentZ@gmail.com");
        Clase clase = addClassAndTeacher("teacherZ@gmail.com", "Clase Z", "Clase Z");
        RequestPK requestPK = sendRequest(user, clase.getId());
        Request expectedRequest = new Request(requestPK);
        Request request = services.getRequest(clase.getId(), user.getId());
        assertEquals(expectedRequest, request);
    }
}

package edu.eci.arsw.teachtome.testservices.tests;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Message;
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
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
public class MessagesServicesTest extends BasicServicesTestsUtilities {

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
    public void shouldNotSendAMessageWithANonExistingUser() throws Exception{
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
    public void shouldNotSendAMessageWithAUserThatIsNotPartOfTheClass() throws Exception{
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
    public void shouldNotGetTheChatWithANonExistingUser() throws Exception{
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
    public void shouldNotGetTheChatOfANonExistingClass() throws Exception{
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
    public void shouldNotGetTheChatWithAUserThatIsNotPartOfTheClass() throws Exception{
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
    public void shouldSendAndGetMessages() throws Exception {
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
}

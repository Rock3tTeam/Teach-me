package edu.eci.arsw.teachtome.testcontroller.tests;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Message;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.services.TeachToMeServices;
import edu.eci.arsw.teachtome.testcontroller.BasicControllerTestsUtilities;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class MessagesControllerTest extends BasicControllerTestsUtilities {

    @Autowired
    private TeachToMeServices services;

    @Before
    public void setUpTest() throws Exception {
        super.setUpUser();
    }

    @Test
    public void shouldNotGetTheChatWithANonExistingClass() throws Exception {
        String email = "sender1@gmail.com";
        addUser(email);
        long id = 200;
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/messages/" + id).header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + id, bodyResult);
    }

    @Test
    public void shouldGetTheChat() throws Exception {
        String email = "ProfeChat@outlook.com";
        addUser(email);
        User user = getUser(email);
        Clase clase = addClass(user, "Clase Chat", "Chat");
        Message message = new Message("hola estudiantes");
        services.sendMessage(message, clase.getId(), email);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/messages/" + clase.getId()).header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        JSONArray array = new JSONArray(bodyResult);
        assertEquals(1, array.length());
        String jsonMessage = array.getJSONObject(0).toString();
        Message returnedMessage = gson.fromJson(jsonMessage, Message.class);
        assertEquals(message.getContent(), returnedMessage.getContent());
        assertEquals(user.getFirstName() + " " + user.getLastName(), returnedMessage.getSender());
    }

}

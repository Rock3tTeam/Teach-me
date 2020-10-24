package edu.eci.arsw.teachtome.testcontroller;

import com.google.gson.Gson;
import edu.eci.arsw.teachtome.auth.UserDetailsImpl;
import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.User;
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
import org.springframework.test.web.servlet.MockMvc;
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
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    private final Gson gson = new Gson();
    private final String apiRoot = "/api/v1";
    private String token;

    @Before
    public void setUpUser() throws Exception {
        User user = addUser("authenticated@hotmail.com");
        UserDetailsImpl userDetails = new UserDetailsImpl(user.getEmail(), user.getPassword());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(userDetails)))
                .andExpect(status().isOk())
                .andReturn();
        token = result.getResponse().getHeader("Authorization");
    }

    @Test
    public void shouldNotGetANonExistentUserByEmail() throws Exception {
        String email = "noexiste@gmail.com";
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/users/" + email).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe el usuario con el email " + email, bodyResult);
    }

    @Test
    public void shouldNotAddAsUserSomethingThatIsNotAnUser() throws Exception {
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Clase())))
                .andExpect(status().isBadRequest())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("JSON Bad Format", bodyResult);
    }

    @Test
    public void shouldNotAddAnAlreadyExistingUser() throws Exception {
        String email = "UsuarioB@gmail.com";
        User user = addUser(email);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users").header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isConflict())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("Ya existe un usuario con el email " + email, bodyResult);
    }

    @Test
    public void shouldGetAnUserByEmail() throws Exception {
        String email = "UsuarioA@gmail.com";
        User expectedUser = addUser(email);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/users/" + email).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        User user = gson.fromJson(bodyResult, User.class);
        assertEquals(expectedUser, user);
    }

    private User addUser(String email) throws Exception {
        User user = new User(email, "Juan", "Rodriguez", "nuevo", email);
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        return user;
    }
}

package edu.eci.arsw.teachtome;

import com.google.gson.Gson;
import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.User;
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

import java.sql.Date;
import java.sql.Timestamp;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class APIControllerTest implements ClassGenerator {

    @Autowired
    private MockMvc mvc;

    private final Gson gson = new Gson();

    @Test
    public void shouldNotGetANonExistentUserByEmail() throws Exception {
        String email = "noexiste@gmail.com";
        mvc.perform(
                MockMvcRequestBuilders.get("/api/users/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotAddAsUserSomethingThatIsNotAnUser() throws Exception {
        mvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Clase())))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void shouldAddAUser() throws Exception {
        User user = new User("new@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        mvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get("/api/users/" + user.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String json = result.getResponse().getContentAsString();
        User selectedUser = gson.fromJson(json, User.class);
        assertEquals(user, selectedUser);
    }

    @Test
    public void shouldNotGetAClassById() throws Exception {
        long id = 200;
        mvc.perform(
                MockMvcRequestBuilders.get("/api/clases/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldNotAddAsAClassSomethingThatIsNotAnClass() throws Exception {
        User user = new User("badteacher@hotmail.com", "Juan", "Rodriguez", "nuevo", "description");
        mvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        mvc.perform(
                MockMvcRequestBuilders.post("/api/users/" + user.getEmail() + "/clases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldAddAClass() throws Exception {
        User user = new User("teacher@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClase("Controlador");
        mvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        System.out.println("Antes: " + clase);
        mvc.perform(
                MockMvcRequestBuilders.post("/api/users/" + user.getEmail() + "/clases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonClase(clase)))
                .andExpect(status().isCreated());
        System.out.println("Después: " + clase);
        //FALTA EL GET DE LA CLASE
        //MvcResult result = MockMvcRequestBuilders.get("/api/clases"+clase.getId())
    }

    private String getJsonClase(Clase clase) {
        return String.format("{\"nombre\":\"%s\",\"capacity\":%d,\"description\":\"%s\",\"amountOfStudents\":%d,\"dateOfInit\":\"%s\",\"dateOfEnd\":\"%s\"}", clase.getNombre(), clase.getCapacity(), clase.getNombre(), clase.getAmountOfStudents(), getJsonFormatTimeStamp(clase.getDateOfInit()), getJsonFormatTimeStamp(clase.getDateOfEnd()));
    }

    private String getJsonFormatTimeStamp(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        int hour = timestamp.getHours();
        int minute = timestamp.getMinutes();
        int second = timestamp.getSeconds();
        String hora = (hour < 10) ? "0" + hour : Integer.toString(hour);
        String minuto = (minute < 10) ? "0" + minute : Integer.toString(minute);
        String segundo = (second < 10) ? "0" + second : Integer.toString(second);
        return date + "T" + hora + ":" + minuto + ":" + segundo;
    }

}

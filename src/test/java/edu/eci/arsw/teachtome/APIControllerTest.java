package edu.eci.arsw.teachtome;

import com.google.gson.Gson;
import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.RequestPK;
import edu.eci.arsw.teachtome.model.User;
import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private final String apiRoot = "/api/v1";

    @Test
    public void shouldNotGetANonExistentUserByEmail() throws Exception {
        String email = "noexiste@gmail.com";
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/users/" + email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe el usuario con el email " + email, bodyResult);
    }

    @Test
    public void shouldNotAddAsUserSomethingThatIsNotAnUser() throws Exception {
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Clase())))
                .andExpect(status().isBadRequest())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("JSON Bad Format", bodyResult);
    }


    @Test
    public void shouldAddAUser() throws Exception {
        User user = new User("new@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/users/" + user.getEmail())
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
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/classes/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + id, bodyResult);
    }

    @Test
    public void shouldNotAddAsAClassSomethingThatIsNotAnClass() throws Exception {
        User user = new User("badteacher@hotmail.com", "Juan", "Rodriguez", "nuevo", "description");
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("JSON Bad Format", bodyResult);
    }

    @Test
    public void shouldAddAClass() throws Exception {
        User user = new User("teacher@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClase("Controlador", "Prueba de Inserción desde el controlador");
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonClase(clase)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotGetTheClassesOfANonExistingTeacher() throws Exception {
        String email = "noexiste@gmail.com";
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/users/" + email + "/teachingClasses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe el usuario con el email " + email, bodyResult);
    }

    @Test
    public void shouldGetTheClassesOfATeacher() throws Exception {
        List<Clase> classes = new ArrayList<>();
        Clase clase;
        User user = new User("felipemartinez@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        for (int i = 1; i < 3; i++) {
            clase = getClase("Español " + i, "Español " + i);
            classes.add(clase);
            mvc.perform(
                    MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(getJsonClase(clase)))
                    .andExpect(status().isCreated());
        }
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/users/" + user.getEmail() + "/teachingClasses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        JSONArray jsonElements = new JSONArray(bodyResult);
        List<Clase> returnedClasses = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            returnedClasses.add(gson.fromJson(jsonElements.get(i).toString(), Clase.class));
        }
        IntStream.range(0, 2).forEach(i -> {
            Clase originalClass = classes.get(i);
            Clase actualClass = returnedClasses.get(i);
            assertTrue(originalClass.lazyEquals(actualClass));
            long originalDateOfInit = originalClass.getDateOfInit().getTime();
            long originalDateOfEnd = originalClass.getDateOfEnd().getTime();
            long actualDateOfInit = actualClass.getDateOfInit().getTime();
            long actualDateOfEnd = actualClass.getDateOfEnd().getTime();
            assertTrue(Math.abs(originalDateOfInit - actualDateOfInit) < 1000);
            assertTrue(Math.abs(originalDateOfEnd - actualDateOfEnd) < 1000);
        });
    }

    @Test
    public void shouldNotSendAEmptyRequest() throws Exception {
        User user = new User("A@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClase("C1", "Clase C1");
        Request request = new Request();
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonClase(clase)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes/" + 1 + "/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("JSON Bad Format", bodyResult);
    }

    @Test
    public void shouldNotSendARequestOfANonExistingStudent() throws Exception {
        Request request = new Request(new RequestPK("noexiste@gmail.com", 1));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/noexiste@gmail.com/classes/" + 1 + "/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isConflict())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe el usuario con el email noexiste@gmail.com", bodyResult);
    }

    @Test
    public void shouldNotSendARequestWithANonExistingClass() throws Exception {
        User user = new User("B@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        Request request = new Request(new RequestPK(user.getEmail(), 200));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes/" + 200 + "/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isConflict())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + 200, bodyResult);
    }

    @Test
    public void shouldNotGetTheRequestsOfANonExistingClass() throws Exception {
        User user = new User("C@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/users/" + user.getEmail() + "/classes/" + 200 + "/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + 200, bodyResult);
    }

    @Test
    public void shouldNotAddAStudentToANonExistingClass() throws Exception {
        User student = new User("noexiste@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(student)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/classes/" + 200 + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(student)))
                .andExpect(status().isConflict())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + 200, bodyResult);
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

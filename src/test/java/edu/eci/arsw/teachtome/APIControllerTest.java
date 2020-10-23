package edu.eci.arsw.teachtome;

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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private final String apiRoot = "/api/v1";
    private static final String patternDate = "yyyy-MM-dd";
    private static final String patternHour = "HH:mm:ss";
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

    @Test
    public void shouldNotGetAClassById() throws Exception {
        addUser("UsuarioC@gmail.com");
        long id = 200;
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/classes/" + id).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + id, bodyResult);
    }

    @Test
    public void shouldNotAddAsAClassSomethingThatIsNotAnClass() throws Exception {
        String email = "UsuarioD@gmail.com";
        addUser(email);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/classes").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new User())))
                .andExpect(status().isBadRequest())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("JSON Bad Format", bodyResult);
    }

    @Test
    public void shouldNotAddAnOutOfDateClass() throws Exception {
        String email = "UsuarioE@gmail.com";
        addUser(email);
        Clase clase = getClaseDesfasada("Mal horario", "Clase con horario desfasado");
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/classes").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonClase(clase)))
                .andExpect(status().isConflict())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("Una clase no puede iniciar después de su fecha de finalización", bodyResult);
    }

    @Test
    public void shouldAddAClass() throws Exception {
        String email = "UsuarioF@gmail.com";
        addUser(email);
        Clase clase = getClase("Controlador", "Prueba de Inserción desde el controlador");
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/classes").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonClase(clase)))
                .andReturn();
        assertEquals(201, result.getResponse().getStatus());
    }

    /*@Test
    public void shouldNotGetTheClassesOfANonExistingTeacher() throws Exception {
        User user = new User("D@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        String token = addUserAndLoginToken(user);
        String email = "noexiste@gmail.com";
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/users/" + email + "/teachingClasses").header("Authorization",token)
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
        String token = addUserAndLoginToken(user);
        for (int i = 1; i < 3; i++) {
            clase = getClase("Español " + i, "Español " + i);
            classes.add(clase);
            mvc.perform(
                    MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes").header("Authorization",token)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(getJsonClase(clase)))
                    .andExpect(status().isCreated());
        }
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/users/" + user.getEmail() + "/teachingClasses").header("Authorization",token)
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
        String token = addUserAndLoginToken(user);
        Clase clase = getClase("C1", "Clase C1");
        Request request = new Request();
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes").header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonClase(clase)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes/" + 1 + "/requests").header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("JSON Bad Format", bodyResult);
    }

    @Test
    public void shouldNotSendARequestOfANonExistingStudent() throws Exception {
        User user = new User("E@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        String token = addUserAndLoginToken(user);
        Request request = new Request(new RequestPK("noexiste@gmail.com", 1));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/noexiste@gmail.com/classes/" + 1 + "/requests").header("Authorization",token)
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
        String token = addUserAndLoginToken(user);
        Request request = new Request(new RequestPK(user.getEmail(), 200));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes/" + 200 + "/requests").header("Authorization",token)
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
        String token = addUserAndLoginToken(user);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/users/" + user.getEmail() + "/classes/" + 200 + "/requests").header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + 200, bodyResult);
    }

    @Test
    public void shouldNotUpdateTheRequestOfANonExistingClass() throws Exception {
        String email = "studentM@gmail.com";
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        String token = addUserAndLoginToken(student);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.put(apiRoot + "/users/" + email + "/classes/" + 200 + "/requests").header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Request(new RequestPK(email, 200)))))
                .andExpect(status().isConflict())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + 200, bodyResult);
    }

    @Test
    public void shouldNotUpdateWithABadConstructRequest() throws Exception {
        String email = "studentM@gmail.com";
        User student = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        String token = addUserAndLoginToken(student);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.put(apiRoot + "/users/" + email + "/classes/" + 1 + "/requests").header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Request())))
                .andExpect(status().isBadRequest())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("JSON Bad Format", bodyResult);
    }

    @Test
    public void shouldConsultTheClassesByName() throws Exception {
        String nameFilter = "Controlador";
        Clase clase = getClase("Controlador T", "Controlador T");
        Clase clase2 = getClase("Controlador U", "Controlador U");
        User user = new User("teacherT2@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        User user2 = new User("teacherU2@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        String token = addUserAndLoginToken(user);
        String token2 = addUserAndLoginToken(user2);
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/" + user.getEmail() + "/classes").header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonClase(clase)))
                .andExpect(status().isCreated());
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users/" + user2.getEmail() + "/classes").header("Authorization",token2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonClase(clase2)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/classes?name=" + nameFilter).header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        JSONArray jsonElements = new JSONArray(bodyResult);
        IntStream.range(0, 2).forEach(i -> {
            try {
                assertTrue(gson.fromJson(jsonElements.get(i).toString(), Clase.class).getNombre().contains(nameFilter));
            } catch (JSONException e) {
                fail("No debió fallar al convertir las clases de JSON a Java");
            }
        });
    }

    @Test
    public void shouldNotConsultTheClassesByName() throws Exception {
        User student = new User("A@hotmail.com", "Juan", "Rodriguez", "nuevo", "description");
        String token = addUserAndLoginToken(student);
        String nameFilter = "Ejemplo";
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/classes?name=" + nameFilter).header("Authorization",token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        JSONArray jsonElements = new JSONArray(bodyResult);
        assertEquals(0, jsonElements.length());
    }
    */

    private User addUser(String email) throws Exception {
        User user = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(user)))
                .andExpect(status().isCreated());
        return user;
    }

    private String getJsonClase(Clase clase) {
        return String.format("{\"nombre\":\"%s\",\"capacity\":%d,\"description\":\"%s\",\"amountOfStudents\":%d,\"dateOfInit\":\"%s\",\"dateOfEnd\":\"%s\"}", clase.getNombre(), clase.getCapacity(), clase.getNombre(), clase.getAmountOfStudents(), getJsonFormatTimeStamp(clase.getDateOfInit()), getJsonFormatTimeStamp(clase.getDateOfEnd()));
    }

    private String getJsonFormatTimeStamp(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat formatDate = new SimpleDateFormat(patternDate);
        SimpleDateFormat hourDate = new SimpleDateFormat(patternHour);
        return formatDate.format(date) + "T" + hourDate.format(date);
    }
}
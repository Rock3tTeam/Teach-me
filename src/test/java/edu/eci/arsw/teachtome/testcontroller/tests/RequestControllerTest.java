package edu.eci.arsw.teachtome.testcontroller.tests;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.RequestPK;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.testcontroller.BasicControllerTestsUtilities;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class RequestControllerTest extends BasicControllerTestsUtilities {

    @Before
    public void setUpTest() throws Exception {
        super.setUpUser();
    }

    @Test
    public void shouldNotSendABadBuildRequest() throws Exception {
        String email = "EstudianteA@outlook.com";
        Clase clase = addClassAndTeacher("ProfeA@outlook.com", "Clase A", "Description A");
        addUser(email);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Request())))
                .andExpect(status().isBadRequest())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("JSON Bad Format", bodyResult);
    }

    @Test
    public void shouldNotSendARequestTheTeacherInHisClass() throws Exception {
        String email = "ProfeB@outlook.com";
        addUser(email);
        User user = getUser(email);
        Clase clase = addClass(user, "Clase B", "Description B");
        Request request = new Request(new RequestPK(user.getId(), clase.getId()));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("El profesor no puede hacer un request a su misma clase", bodyResult);
    }

    @Test
    public void shouldNotSendARequestOfANonExistingStudent() throws Exception {
        long id = 200;
        Clase clase = addClassAndTeacher("ProfeA2@outlook.com", "Clase A2", "Description A2");
        Request request = new Request(new RequestPK(id, clase.getId()));
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", "noexiste@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isConflict())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe el usuario con el id " + id, bodyResult);
    }

    @Test
    public void shouldNotGetTheRequestsOfANonExistingUser() throws Exception {
        Clase clase = addClassAndTeacher("ProfeC@outlook.com", "Clase C", "Clase C");
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", "noexiste@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe el usuario con el email noexiste@gmail.com", bodyResult);
    }

    @Test
    public void shouldNotGetTheRequestsOfAClassWithoutBeingTheTeacher() throws Exception {
        String email = "EstudianteD@outlook.com";
        addUser(email);
        Clase clase = addClassAndTeacher("ProfeD@outlook.com", "Clase D", "Clase D");
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No tiene permitido ver los requests a esta clase", bodyResult);
    }

    @Test
    public void shouldSendAndGetTheRequestsOfAClass() throws Exception {
        String email = "EstudianteE@outlook.com";
        addUser(email);
        User user = getUser(email);
        Clase clase = addClassAndTeacher("ProfeE@outlook.com", "Clase E", "Description E");
        Request request = new Request(new RequestPK(user.getId(), clase.getId()));
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", "ProfeE@outlook.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        JSONArray array = new JSONArray(bodyResult);
        assertEquals(1, array.length());
        String jsonRequest = array.getJSONObject(0).toString();
        Request returnedRequest = gson.fromJson(jsonRequest, Request.class);
        assertEquals(request, returnedRequest);
    }

    @Test
    public void shouldNotGetTheClassesOfANonExistingStudent() throws Exception {
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/studyingClasses").header("Authorization", token).header("x-userEmail", "noexiste@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe el usuario con el email noexiste@gmail.com", bodyResult);
    }

    @Test
    public void shouldNotUpdateTheRequestOfAClassIfIsNotTheTeacher() throws Exception {
        String email = "EstudianteF@outlook.com";
        addUser(email);
        User user = getUser(email);
        Clase clase = addClassAndTeacher("ProfeF@outlook.com", "Clase F", "Description F");
        RequestPK requestPK = sendRequest(user, clase.getId());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.put(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Request(requestPK, true))))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No tiene permitido actualizar el request de esta clase", bodyResult);
    }

    @Test
    public void shouldNotUpdateTheRequestOfANonExistingClass() throws Exception {
        long id = 200;
        String email = "EstudianteG@outlook.com";
        addUser(email);
        User user = getUser(email);
        RequestPK requestPK = new RequestPK(user.getId(), id);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.put(apiRoot + "/classes/" + id + "/requests").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Request(requestPK, true))))
                .andExpect(status().isConflict())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + id, bodyResult);
    }

    @Test
    public void shouldNotUpdateABadBuildRequest() throws Exception {
        String email = "EstudianteH@outlook.com";
        addUser(email);
        User user = getUser(email);
        Clase clase = addClassAndTeacher("ProfeH@outlook.com", "Clase H", "Description H");
        sendRequest(user, clase.getId());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.put(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Request(null))))
                .andExpect(status().isBadRequest())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("JSON Bad Format", bodyResult);
    }

    @Test
    public void shouldUpdateARequestToTrue() throws Exception {
        String email = "EstudianteI@outlook.com";
        addUser(email);
        User user = getUser(email);
        Clase clase = addClassAndTeacher("ProfeI@outlook.com", "Clase I", "Description I");
        RequestPK requestPK = sendRequest(user, clase.getId());
        mvc.perform(
                MockMvcRequestBuilders.put(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", "ProfeI@outlook.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Request(requestPK, true))))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", "ProfeI@outlook.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No hay solicitudes pendientes para esta clase", bodyResult);
    }

    @Test
    public void shouldGetTheStudyingClassesOfAStudent() throws Exception {
        String email = "EstudianteJ@outlook.com";
        addUser(email);
        User user = getUser(email);
        Clase clase = addClassAndTeacher("ProfeJ@outlook.com", "Clase J", "Description J");
        RequestPK requestPK = sendRequest(user, clase.getId());
        mvc.perform(
                MockMvcRequestBuilders.put(apiRoot + "/classes/" + clase.getId() + "/requests").header("Authorization", token).header("x-userEmail", "ProfeJ@outlook.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new Request(requestPK, true))))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/studyingClasses").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        JSONArray array = new JSONArray(bodyResult);
        assertEquals(1, array.length());
        String jsonClass = array.getJSONObject(0).toString();
        Clase returnedClass = gson.fromJson(jsonClass, Clase.class);
        assertTrue(clase.lazyEquals(returnedClass));
    }

    @Test
    public void shouldNotGetANonExistentRequest() throws Exception {
        String email = "EstudianteL@outlook.com";
        addUser(email);
        User user = getUser(email);
        Clase clase = addClassAndTeacher("ProfeL@outlook.com", "Clase L", "Description L");
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/requests/" + clase.getId() + "/" + user.getId()).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la solicitud de la clase "+clase.getId()+" por parte del usuario "+user.getId(),bodyResult);
    }

    @Test
    public void shouldGetARequest() throws Exception {
        String email = "EstudianteK@outlook.com";
        addUser(email);
        User user = getUser(email);
        Clase clase = addClassAndTeacher("ProfeK@outlook.com", "Clase K", "Description K");
        RequestPK requestPK = sendRequest(user, clase.getId());
        Request request = new Request(requestPK);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/requests/" + clase.getId() + "/" + user.getId()).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        Request returnedRequest = gson.fromJson(bodyResult, Request.class);
        assertEquals(request, returnedRequest);
    }
}

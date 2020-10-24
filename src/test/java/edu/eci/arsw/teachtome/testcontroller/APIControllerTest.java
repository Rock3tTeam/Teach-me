package edu.eci.arsw.teachtome.testcontroller;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
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
public class APIControllerTest extends BasicControllerUtilities {

    @Before
    public void setUpTest() throws Exception {
        super.setUpUser();
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
    public void shouldNotGetTheTeachingClassesOfANonExistentUser() throws Exception {
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/teachingClasses").header("Authorization", token).header("x-userEmail", "noexiste@gmail.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe el usuario con el email noexiste@gmail.com", bodyResult);
    }

    @Test
    public void shouldAddAClass() throws Exception {
        String email = "UsuarioF@gmail.com";
        addUser(email);
        Clase clase = getClase("Controlador", "Prueba de Inserción desde el controlador");
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/classes").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(getJsonClase(clase)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/teachingClasses").header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        JSONObject obj = new JSONArray(bodyResult).getJSONObject(0);
        Clase returnedClass = gson.fromJson(obj.toString(), Clase.class);
        assertTrue(clase.lazyEquals(returnedClass));
        result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/classes/" + returnedClass.getId()).header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        bodyResult = result.getResponse().getContentAsString();
        returnedClass = gson.fromJson(bodyResult, Clase.class);
        assertTrue(clase.lazyEquals(returnedClass));
    }

    //DELETE CLASS

    @Test
    public void shouldNotDeleteAClassIfTheUserIsNotTheTeacher() throws Exception {
        String email = "UsuarioG2@gmail.com";
        addUser(email);
        Clase clase = addClassAndTeacher("UsuarioG@gmail.com", "Clase que no se va a eliminar", "Description");
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.delete(apiRoot + "/classes/" + clase.getId()).header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isUnauthorized())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("El usuario no tiene permiso para eliminar esta clase", bodyResult);
    }

    @Test
    public void shouldNotDeleteANonExistentClass() throws Exception {
        String email = "UsuarioH@gmail.com";
        addUser(email);
        long id = 200;
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.delete(apiRoot + "/classes/" + id).header("Authorization", token).header("x-userEmail", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + id, bodyResult);
    }

    //REQUESTS

    //GET CLASSES OF A STUDENT

    //GET FILTERED CLASSES

    //CHAT
}
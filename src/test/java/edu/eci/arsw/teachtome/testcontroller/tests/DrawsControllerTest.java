package edu.eci.arsw.teachtome.testcontroller.tests;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Point;
import edu.eci.arsw.teachtome.services.TeachToMeServices;
import edu.eci.arsw.teachtome.testcontroller.BasicControllerTestsUtilities;
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

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class DrawsControllerTest extends BasicControllerTestsUtilities {

    @Before
    public void setUpTest() throws Exception {
        super.setUpUser();
    }

    @Test
    public void shouldNotAddABadBuildDraw() throws Exception {
        Clase clase = addClassAndTeacher("artistaB@outlook.com", "Arte B", "Arte B");
        ArrayList<Point> points = new ArrayList<>();
        Draw draw = new Draw(points);
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/draws/" + clase.getId()).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(draw)))
                .andExpect(status().isConflict())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("El dibujo debe poseer al menos un punto", bodyResult);
    }

    @Test
    public void shouldNotGetTheDrawsOfANonExistentClass() throws Exception {
        long id = 200;
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/draws/" + id).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isNotFound())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        assertEquals("No existe la clase con el id " + id, bodyResult);
    }

    @Test
    public void shouldGetTheDrawsOfAClass() throws Exception {
        Clase clase = addClassAndTeacher("artistaA@outlook.com", "Arte A", "Arte A");
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(20, 20));
        Draw draw = new Draw(points);
        mvc.perform(
                MockMvcRequestBuilders.post(apiRoot + "/draws/" + clase.getId()).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(draw)))
                .andExpect(status().isCreated());
        MvcResult result = mvc.perform(
                MockMvcRequestBuilders.get(apiRoot + "/draws/" + clase.getId()).header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isAccepted())
                .andReturn();
        String bodyResult = result.getResponse().getContentAsString();
        Draw returnedDraw = gson.fromJson(bodyResult, Draw.class);
        assertEquals(draw, returnedDraw);
    }
}
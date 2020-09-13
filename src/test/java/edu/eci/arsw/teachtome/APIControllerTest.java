package edu.eci.arsw.teachtome;

import com.google.gson.Gson;
import edu.eci.arsw.teachtome.model.Clase;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class APIControllerTest {

    @Autowired
    private MockMvc mvc;

    private Gson gson = new Gson();


    @Test
    public void shouldGetByIdController() throws Exception {
        /*mvc.perform(
                MockMvcRequestBuilders.post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(getClase("pruebaControlador"))))
                .andExpect(status().isCreated());

         */
    }


    private Clase getClase(String nombre){
        Date dateOfInit = null;
        Date dateOfEnd = null;
        try {
            dateOfInit = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("2020/04/13 15:23:12");
            dateOfEnd = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").parse("2019/04/13 12:23:12");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Clase(nombre, 23, "Clase para probar inserción", 0, new java.sql.Date(dateOfInit.getTime()),new java.sql.Date(dateOfEnd.getTime()));
    }

}

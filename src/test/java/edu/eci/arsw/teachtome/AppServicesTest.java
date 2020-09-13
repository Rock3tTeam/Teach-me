package edu.eci.arsw.teachtome;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.services.TeachToMeServicesInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class AppServicesTest {

    @Autowired
    private TeachToMeServicesInterface services;

    @Test
    public void shouldGetAClassById() throws TeachToMeServiceException {
        Clase clase = services.getClase(1L);
        assertEquals("description test", clase.getDescription());
    }

    @Test
    public void shouldNotGetAClassById() {
        Long id = 2L;
        try {
            services.getClase(id);
            fail("Debió fallar al buscar una clase con id inexistente");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetAClassWithANullId() {
        try {
            services.getClase(null);
            fail("Debió fallar la buscar una clase con id nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El id no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldGetAUserByEmail() throws TeachToMeServiceException {
        User user = services.getUser("prueba@gmail.com");
        assertEquals("pepito", user.getFirstName());
    }

    @Test
    public void shouldNotGetAUserByEmail() {
        String email = "noexiste@gmail.com";
        try {
            services.getUser(email);
            fail("Debió fallar al buscar un usuario que no existe en la base de datos");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email " + email, e.getMessage());
        }
    }

    @Test
    public void shouldNotGetAUserWithANullEmail() {
        try {
            services.getUser(null);
            fail("Debió fallar la buscar un usuario con email nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }



    @Test
    public void shouldAddANewUser() throws TeachToMeServiceException {
        User user = new User("nuevo@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        services.addUser(user);
        User databaseUser = services.getUser("nuevo@gmail.com");
        assertEquals(user,databaseUser);
    }

    @Test
    public void shouldAddANewClass() throws TeachToMeServiceException {
        Date dateOfInit = null;
        Date dateOfEnd = null;
        try {
            dateOfInit = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss").parse("2020/04/13 15:23:12");
            dateOfEnd = new SimpleDateFormat("dd/MM/YYYY hh:mm:ss").parse("2019/04/13 12:23:12");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Clase clase = new Clase("Nueva Clase", 23, "Clase para probar inserción", 0, new java.sql.Date(dateOfInit.getTime()),new java.sql.Date(dateOfEnd.getTime()));
        User user = services.getUser("prueba@gmail.com");
        services.addClase(clase, user);
    }
}

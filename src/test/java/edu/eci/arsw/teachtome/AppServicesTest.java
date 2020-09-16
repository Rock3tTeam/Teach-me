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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class AppServicesTest implements ClassGenerator {

    @Autowired
    private TeachToMeServicesInterface services;


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
    public void shouldNotAddANullUser() {
        try {
            services.addUser(null);
            fail("Debió fallar al intentar agregar un usuario nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El usuario no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldAddAndGetANewUser() throws TeachToMeServiceException {
        User user = new User("nuevo@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        services.addUser(user);
        User databaseUser = services.getUser("nuevo@gmail.com");
        assertEquals(user, databaseUser);
    }

    @Test
    public void shouldNotGetAClassById() {
        long id = 200;
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
    public void shouldNotAddANullClass() {
        User user = new User("badteacher@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        try {
            services.addUser(user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar por agregar un nuevo usuario");
        }
        try {
            services.addClase(null, user);
            fail("Debió fallar por insertar una clase nula");
        } catch (TeachToMeServiceException e) {
            assertEquals("La clase no puede ser nula", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAClassWithANullTeacher() {
        Clase clase = getClase("Mala clase");
        try {
            services.addClase(clase, null);
            fail("Debió fallar por insertar una clase con un profesor nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El usuario no puede ser nulo", e.getMessage());
        }
    }


    @Test
    public void shouldAddAndGetAClass() throws TeachToMeServiceException {
        Clase clase = getClase("Nueva Clase");
        User user = new User("nuevo@gmail.com", "Juan", "Rodriguez", "nuevo", "description");
        services.addUser(user);
        services.addClase(clase, user);
        //FALTA EL GET
        //Clase clasePrueba = services.getClase(1L);
        //assertEquals("Nueva Clase", clasePrueba.getNombre());
    }

}

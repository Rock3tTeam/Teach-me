package edu.eci.arsw.teachtome;

import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.services.TeachToMeServicesInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
public class UserServicesTest {

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
}

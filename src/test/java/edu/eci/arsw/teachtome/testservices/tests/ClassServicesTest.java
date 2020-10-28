package edu.eci.arsw.teachtome.testservices.tests;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.testservices.BasicServicesTestsUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
public class ClassServicesTest extends BasicServicesTestsUtilities {

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
    public void shouldNotAddANullClass() throws Exception {
        User user = addUser("badteacher@gmail.com");
        try {
            services.addClase(null, user);
            fail("Debió fallar por insertar una clase nula");
        } catch (TeachToMeServiceException e) {
            assertEquals("La clase no puede ser nula", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAClassWithANullTeacher() {
        Clase clase = getClase("Mala clase", "No debería insertar una clase con un profesor nulo");
        try {
            services.addClase(clase, null);
            fail("Debió fallar por insertar una clase con un profesor nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El usuario no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAClassWithNegativeCapacity() throws Exception {
        User user = addUser("negativeTeacher@gmail.com");
        Clase clase = getClase("Capacidad Negativa", "Capacidad Negativa", -1, 0);
        try {
            services.addClase(clase, user);
            fail("Debió fallar por insertar una clase con capacidad negativa");
        } catch (TeachToMeServiceException e) {
            assertEquals("No se puede insertar una clase con capacidad menor que 1", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAClassWithoutCapacity() throws Exception {
        User user = addUser("zeroTeacher@gmail.com");
        Clase clase = getClase("Capacidad Cero", "Capacidad Cero", 0, 0);
        try {
            services.addClase(clase, user);
            fail("Debió fallar por insertar una clase con capacidad cero");
        } catch (TeachToMeServiceException e) {
            assertEquals("No se puede insertar una clase con capacidad menor que 1", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddARepeatedDescriptionClass() throws Exception {
        Clase clase = getClase("Clase original", "Clase original");
        User user = addUser("copyTeacher@gmail.com");
        try {
            services.addClase(clase, user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al agregar esta clase");
        }
        try {
            services.addClase(clase, user);
            fail("Debió fallar por insertar una clase con descripción repetida");
        } catch (TeachToMeServiceException e) {
            assertEquals("No se puede insertar una clase con esa descripcion", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAClassWithAInvalidDate() throws Exception {
        User user = addUser("nuevoB@gmail.com");
        Clase clase = getClaseAntigua("Clase con mal horario", "Mal horario");
        try {
            services.addClase(clase, user);
            fail("Debió fallar por insertar una clase que inicia antes de la hora actual");
        } catch (TeachToMeServiceException e) {
            assertEquals("No se puede programar una clase antes de la hora actual", e.getMessage());
        }
        clase = getClaseDesfasada("Clase desfasada", "Horario desfasado");
        try {
            services.addClase(clase, user);
            fail("Debió fallar por insertar una clase cuya hora de fin es previa a la hora de inicio");
        } catch (TeachToMeServiceException e) {
            assertEquals("Una clase no puede iniciar después de su fecha de finalización", e.getMessage());
        }
    }


    @Test
    public void shouldAddAndGetAClass() throws Exception {
        Clase clase = getClase("Nueva Clase", "Prueba Exitosa de Inserción");
        String email = "nuevo@gmail.com";
        User user = addUser(email);
        services.addClase(clase, user);
        Clase clasePrueba = services.getClase(clase.getId());
        assertEquals(clase, clasePrueba);
    }

    @Test
    public void shouldNotGetTheClassesOfATeacherWithANullEmail() {
        try {
            services.getTeachingClassesOfUser(null);
            fail("Debió fallar por enviar un email nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El email no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheClassesOfANonExistingTeacher() {
        String email = "noexiste@gmail.com";
        try {
            services.getTeachingClassesOfUser(email);
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe el usuario con el email " + email, e.getMessage());
        }
    }

    @Test
    public void shouldGetTheClassesOfATeacher() throws Exception {
        List<Clase> classes = new ArrayList<>();
        Clase clase;
        String email = "julioprofe@gmail.com";
        User user = addUser(email);
        for (int i = 1; i < 3; i++) {
            clase = getClase("Matemática " + i, "Matemática " + i);
            classes.add(clase);
            services.addClase(clase, user);
        }
        List<Clase> returnedClasses = services.getTeachingClassesOfUser(email);
        assertEquals(2, returnedClasses.size());
        for (int i = 0; i < 2; i++) {
            assertEquals(classes.get(i), returnedClasses.get(i));
        }
    }

    @Test
    public void shouldNotDeleteAClassIfTheUserIsNotTheTeacher() throws Exception {
        String email = "teacherAC@gmail.com";
        User teacher = addUser(email);
        addUser("studentAC@gmail.com");
        Clase clase = addClass(teacher, "Clase AC", "Clase AC");
        try {
            services.deleteClass(clase.getId(), "studentAC@gmail.com");
            fail("Debió fallar por intentar eliminar una clase sin ser su profesor");
        } catch (TeachToMeServiceException e) {
            assertEquals("El usuario no tiene permiso para eliminar esta clase", e.getMessage());
        }
    }

    @Test
    public void shouldDeleteAClass() throws Exception {
        String email = "teacherAA@gmail.com";
        User teacher = addUser(email);
        Clase clase = addClass(teacher, "Clase AA", "Clase AA");
        Clase clase2 = addClass(teacher, "Clase AB", "Clase AB");
        List<Clase> originalClasses = services.getTeachingClassesOfUser(email);
        services.deleteClass(clase.getId(), email);
        List<Clase> classes = services.getTeachingClassesOfUser(email);
        assertEquals(originalClasses.size() - 1, classes.size());
        assertEquals(clase2, classes.get(0));
        services.deleteClass(clase2.getId(), email);
        List<Clase> emptyClasses = services.getTeachingClassesOfUser(email);
        assertTrue(emptyClasses.isEmpty());
    }

    @Test
    public void shouldNotConsultTheClassesByNameWithoutTheFilter() {
        try {
            services.getFilteredClassesByName(null);
            fail("Debió fallar por usar un filtro nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El nombre no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldConsultTheClassesByName() throws TeachToMeServiceException {
        addClassAndTeacher("teacherTT@gmail.com", "Ejemplo TT", "Ejemplo TT", 30, 0);
        addClassAndTeacher("teacherUU@gmail.com", "ejemplo UU", "Ejemplo UU", 20, 0);
        String nameFilter = "Ejemplo";
        List<Clase> clases = services.getFilteredClassesByName(nameFilter);
        assertEquals(2, clases.size());
        String name;
        String lowerCaseName = nameFilter.toLowerCase();
        String upperCaseName = nameFilter.toUpperCase();
        for (Clase returnedClass : clases) {
            name = returnedClass.getNombre();
            assertTrue(name.contains(nameFilter) || name.contains(lowerCaseName) || name.contains(upperCaseName));
        }
    }
}

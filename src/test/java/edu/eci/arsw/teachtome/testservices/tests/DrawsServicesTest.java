package edu.eci.arsw.teachtome.testservices.tests;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Draw;
import edu.eci.arsw.teachtome.model.Point;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:db-test.properties")
@Sql("/test-h2.sql")
@AutoConfigureTestDatabase
public class DrawsServicesTest extends BasicServicesTestsUtilities {

    @Test
    public void shouldNotAddDrawsOnANonExistentClass() {
        long id = 200;
        ArrayList<Draw> draws = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        try {
            points.add(new Point(20, 20));
            draws.add(new Draw(points));
            services.addDrawsToAClass(id, draws);
            fail("Debió fallar al buscar una clase con id inexistente");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotAddAnEmptyListOfDraws() {
        long id = 200;
        ArrayList<Draw> draws = new ArrayList<>();
        try {
            services.addDrawsToAClass(id, draws);
            fail("Debió fallar por enviar una lista de dibujos vacía");
        } catch (TeachToMeServiceException e) {
            assertEquals("La lista de dibujos debe poseer al menos un dibujo", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddADrawWithoutPoints() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteA@gmail.com", "Dibujo A", "Dibujo A");
        ArrayList<Draw> draws = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        try {
            draws.add(new Draw(points));
            services.addDrawsToAClass(clase.getId(), draws);
            fail("Debió fallar al intentar agregar una lista sin puntos");
        } catch (TeachToMeServiceException e) {
            assertEquals("Dibujo mal construido", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddANullPointsDraw() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteC@gmail.com", "Dibujo C", "Dibujo C");
        ArrayList<Draw> draws = new ArrayList<>();
        try {
            draws.add(new Draw(null));
            services.addDrawsToAClass(clase.getId(), draws);
            fail("Debió fallar al intentar agregar una lista con un dibujo con puntos nulos");
        } catch (TeachToMeServiceException e) {
            assertEquals("Dibujo mal construido", e.getMessage());
        }
    }

    @Test
    public void shouldGetTheDrawsOfANonExistentClass() {
        long id = 200;
        try {
            services.getDrawsOfAClass(id);
            fail("Debió fallar al buscar una clase con id inexistente");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldAddAndGetDraws() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteB@gmail.com", "Dibujo B", "Dibujo B");
        ArrayList<Draw> draws = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(20, 20));
        draws.add(new Draw(points));
        services.addDrawsToAClass(clase.getId(), draws);
        List<Draw> returnedDraws = services.getDrawsOfAClass(clase.getId());
        assertEquals(draws, returnedDraws);
    }

    @Test
    public void shouldAddAndGetOnlyTheLastDraws() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteD@gmail.com", "Dibujo D", "Dibujo D");
        ArrayList<Draw> draws = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(20, 20));
        draws.add(new Draw(points));
        services.addDrawsToAClass(clase.getId(), draws);
        List<Draw> returnedDraws = services.getDrawsOfAClass(clase.getId());
        assertEquals(draws, returnedDraws);
        ArrayList<Draw> draws2 = new ArrayList<>();
        ArrayList<Point> points2 = new ArrayList<>();
        points2.add(new Point(50, 50));
        draws2.add(new Draw(points2));
        services.addDrawsToAClass(clase.getId(), draws2);
        List<Draw> returnedLastDraws = services.getDrawsOfAClass(clase.getId());
        assertEquals(draws2, returnedLastDraws);
    }
}

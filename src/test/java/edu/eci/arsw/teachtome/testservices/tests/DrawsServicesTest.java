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
        ArrayList<Point> points = new ArrayList<>();
        try {
            points.add(new Point(20, 20));
            services.addDrawToAClass(id, new Draw(points));
            fail("Debió fallar al buscar una clase con id inexistente");
        } catch (TeachToMeServiceException e) {
            assertEquals("No existe la clase con el id " + id, e.getMessage());
        }
    }

    @Test
    public void shouldNotAddADrawWithoutPoints() {
        long id = 200;
        ArrayList<Point> points = new ArrayList<>();
        try {
            services.addDrawToAClass(id, new Draw(points));
            fail("Debió fallar por enviar un dibujo sin puntos");
        } catch (TeachToMeServiceException e) {
            assertEquals("El dibujo debe poseer al menos un punto", e.getMessage());
        }
    }

    @Test
    public void shouldNotAddANullPointsDraw() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteC@gmail.com", "Dibujo C", "Dibujo C");
        try {
            services.addDrawToAClass(clase.getId(), new Draw(null));
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
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(20, 20));
        Draw draw = new Draw(points);
        services.addDrawToAClass(clase.getId(), draw);
        Draw returnedDraw = services.getDrawsOfAClass(clase.getId());
        assertEquals(draw, returnedDraw);
    }

    @Test
    public void shouldAddAndGetOnlyTheLastDraws() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteD@gmail.com", "Dibujo D", "Dibujo D");
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(20, 20));
        Draw draw = new Draw(points);
        services.addDrawToAClass(clase.getId(), draw);
        Draw returnedDraw = services.getDrawsOfAClass(clase.getId());
        assertEquals(draw, returnedDraw);
        ArrayList<Point> points2 = new ArrayList<>();
        points2.add(new Point(50, 50));
        Draw draw2 = new Draw(points2);
        services.addDrawToAClass(clase.getId(), draw2);
        Draw returnedLastDraw = services.getDrawsOfAClass(clase.getId());
        assertEquals(draw2, returnedLastDraw);
    }
}

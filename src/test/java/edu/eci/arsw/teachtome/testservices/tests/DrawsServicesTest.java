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

    /*@Test
    public void shouldNotAddDrawsOnANonExistentClass() {
        long id = 200;
        ArrayList<Point> points = new ArrayList<>();
        try {
            points.add(new Point(20, 20, "color"));
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
        List<Point> points = null;
        try {
            services.addDrawToAClass(clase.getId(), new Draw(points));
            fail("Debió fallar al intentar agregar una lista con un dibujo con puntos nulos");
        } catch (TeachToMeServiceException e) {
            assertEquals("Dibujo mal construido", e.getMessage());
        }
    }*/

    @Test
    public void shouldNotAddANullDrawInCache() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteD@gmail.com", "Dibujo D", "Dibujo D");
        try {
            services.addDrawToCache(clase.getId(), null);
            fail("Debió fallar al intentar agregar en cache un dibujo nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("No se puede guardar en cache un dibujo nulo", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheDrawsOfAClassWithoutDraws() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteE@gmail.com", "Dibujo E", "Dibujo E");
        try {
            services.getDrawsOfAClass(clase.getId());
            fail("Debió fallar al buscar una clase sin dibujos");
        } catch (TeachToMeServiceException e) {
            assertEquals("Esa clase no tiene dibujo en cache", e.getMessage());
        }
    }

    @Test
    public void shouldNotGetTheDrawWithANullIdentifierClass() {
        try {
            services.getDrawsOfAClass(null);
            fail("Debió fallar al ingresar un identificador nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El identificador de la clase no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldAddAndGetDrawsFromCache() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteF@gmail.com", "Dibujo F", "Dibujo F");
        ArrayList<Point> points = new ArrayList<>();
        points.add(new Point(20, 20, "color"));
        Draw draw = new Draw(points);
        services.addDrawToCache(clase.getId(), draw);
        Draw returnedDraw = services.getDrawsOfAClass(clase.getId());
        assertEquals(draw, returnedDraw);
    }

    @Test
    public void shouldAddAndGetTheLastDrawFromCache() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteF@gmail.com", "Dibujo F", "Dibujo F");
        ArrayList<Point> points = new ArrayList<>();
        Point point1 = new Point(20, 20, "color");
        points.add(point1);
        Draw draw = new Draw(points);
        services.addDrawToCache(clase.getId(), draw);
        ArrayList<Point> points2 = new ArrayList<>();
        Point point2 = new Point(50, 50, "color");
        points2.add(point2);
        Draw draw2 = new Draw(points2);
        services.addDrawToCache(clase.getId(), draw2);
        List<Point> returnedPoints = services.getDrawsOfAClass(clase.getId()).getPoints();
        assertEquals(2, returnedPoints.size());
        assertEquals(point1, returnedPoints.get(0));
        assertEquals(point2, returnedPoints.get(1));
    }

    @Test
    public void shouldNotDeleteADrawWithANullIdentifierClass() {
        try {
            services.deleteDrawFromCache(null);
            fail("Debió fallar al ingresar un identificador nulo");
        } catch (TeachToMeServiceException e) {
            assertEquals("El identificador de la clase no puede ser nulo", e.getMessage());
        }
    }

    @Test
    public void shouldDeleteADraw() throws Exception {
        Clase clase = addClassAndTeacher("dibujanteF@gmail.com", "Dibujo F", "Dibujo F");
        ArrayList<Point> points = new ArrayList<>();
        Point point1 = new Point(20, 20, "color");
        points.add(point1);
        Draw draw = new Draw(points);
        services.addDrawToCache(clase.getId(), draw);
        services.deleteDrawFromCache(clase.getId());
        try {
            services.getDrawsOfAClass(clase.getId());
            fail("Debió fallar al consultar un dibujo que ya no existe");
        } catch (TeachToMeServiceException e) {
            assertEquals("Esa clase no tiene dibujo en cache", e.getMessage());
        }
    }
}

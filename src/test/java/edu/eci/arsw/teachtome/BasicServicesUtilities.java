package edu.eci.arsw.teachtome;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.RequestPK;
import edu.eci.arsw.teachtome.model.User;
import edu.eci.arsw.teachtome.services.TeachToMeServiceException;
import edu.eci.arsw.teachtome.services.TeachToMeServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.fail;

public class BasicServicesUtilities implements ClassGenerator {

    @Autowired
    protected TeachToMeServicesInterface services;

    protected User addUser(String email) {
        User user = new User(email, "Juan", "Rodriguez", "nuevo", "description");
        try {
            services.addUser(user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al usuario");
        }
        return user;
    }

    protected Clase addClass(User user, String className, String classDescription) {
        Clase clase = getClase(className, classDescription);
        try {
            services.addClase(clase, user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar la clase");
        }
        return clase;
    }

    protected Clase addClassAndTeacher(String teacherEmail, String className, String classDescription) {
        User user = new User(teacherEmail, "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClase(className, classDescription);
        try {
            services.addUser(user);
            services.addClase(clase, user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al profesor ni a su clase");
        }
        return clase;
    }

    protected Clase addClassAndTeacher(String teacherEmail, String className, String classDescription, int capacity, int amount) {
        User user = new User(teacherEmail, "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClase(className, classDescription, capacity, amount);
        try {
            services.addUser(user);
            services.addClase(clase, user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al profesor ni a su clase");
        }
        return clase;
    }

    protected Clase addShortClassAndTeacher(String teacherEmail, String className, String classDescription) {
        User user = new User(teacherEmail, "Juan", "Rodriguez", "nuevo", "description");
        Clase clase = getClassOfShortDuration(className, classDescription, 30, 0);
        try {
            services.addUser(user);
            services.addClase(clase, user);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al ingresar al profesor ni a su clase");
        }
        return clase;
    }

    protected RequestPK sendRequest(long userId, long classId) {
        RequestPK requestPK = null;
        Request request;
        try {
            requestPK = new RequestPK(userId, classId);
            request = new Request(requestPK);
            services.sendRequest(request);
        } catch (TeachToMeServiceException e) {
            fail("No debió fallar al realizar la solicitud");
        }
        return requestPK;
    }

    protected void waitAWhile() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
package edu.eci.arsw.teachtome;

import edu.eci.arsw.teachtome.model.Clase;

import java.sql.Timestamp;

public interface ClassGenerator {
    default Clase getClase(String nombre) {
        long classDurationInMillis = 1000000;
        long actualTime = System.currentTimeMillis();
        Timestamp dateOfInit = new Timestamp(actualTime);
        Timestamp dateOfEnd = new Timestamp(actualTime + classDurationInMillis);
        return new Clase(nombre, 23, "Clase para probar inserción", 0, dateOfInit, dateOfEnd);
    }
}

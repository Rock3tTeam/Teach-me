package edu.eci.arsw.teachtome.persistence;

import edu.eci.arsw.teachtome.model.Clase;

public interface TeachToMePersistence {
    public Clase getClase(Long id) throws TeachToMePersistenceException;
}

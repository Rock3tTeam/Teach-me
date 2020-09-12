package edu.eci.arsw.teachtome.persistence;

import edu.eci.arsw.teachtome.model.Clase;

public interface TeachToMePersistence {
    /**
     * Consulta una clase dentro de la base de datos
     *
     * @param id - El identificador de la clase
     * @return La clase con su respectivo identificador
     * @throws TeachToMePersistenceException - Cuando no existe la clase dentro de la base de datos
     */
    public Clase getClase(Long id) throws TeachToMePersistenceException;
}

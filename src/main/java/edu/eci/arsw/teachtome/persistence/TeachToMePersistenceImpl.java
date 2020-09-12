package edu.eci.arsw.teachtome.persistence;

import edu.eci.arsw.teachtome.model.Clase;
import edu.eci.arsw.teachtome.persistence.repositories.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de la capa de persistencia de la aplicación TeachToMe
 */
@Service
public class TeachToMePersistenceImpl implements TeachToMePersistence {

    @Autowired
    private ClaseRepository claseRepository;

    /**
     * Consulta una clase dentro de la base de datos
     *
     * @param id - El identificador de la clase
     * @return La clase con su respectivo identificador
     * @throws TeachToMePersistenceException - Cuando no existe la clase dentro de la base de datos
     */
    @Override
    public Clase getClase(Long id) throws TeachToMePersistenceException {
        Clase clase = null;
        if (claseRepository.existsById(id)) {
            clase = claseRepository.findById(id).get();
        }
        if (clase == null) {
            throw new TeachToMePersistenceException("No existe la clase con el id " + id);
        }
        return clase;
    }
}

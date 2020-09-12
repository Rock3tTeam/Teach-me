package edu.eci.arsw.teachtome.persistence;

import edu.eci.arsw.teachtome.model.Clase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeachToMePersistenceImpl implements TeachToMePersistence {

    @Autowired
    private ClaseRepository claseRepository;

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

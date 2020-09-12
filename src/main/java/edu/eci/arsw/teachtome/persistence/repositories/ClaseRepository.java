package edu.eci.arsw.teachtome.persistence.repositories;

import edu.eci.arsw.teachtome.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Conexión con base de Datos para la clase "Clase"
 */
@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    //@Query("FROM classes where nombre = ?1")
    //List<Clase> findByNombre(String nombre);
}

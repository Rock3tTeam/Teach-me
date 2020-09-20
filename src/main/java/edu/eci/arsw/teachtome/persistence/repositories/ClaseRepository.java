package edu.eci.arsw.teachtome.persistence.repositories;

import edu.eci.arsw.teachtome.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Conexión con base de Datos para la clase "Clase"
 */
@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {


    @Query(value = "select * from classes where name LIKE CONCAT('%',:filter,'%')", nativeQuery = true)
    List<Clase> filterByName(@Param("filter") String name);
}

package edu.eci.arsw.teachtome.persistence.repositories;

import edu.eci.arsw.teachtome.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Repositorio de Conexión con base de Datos para la clase "Clase"
 */
@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    /**
     * Obtiene las clases que poseen en el nombre el valor dado
     *
     * @param name - Valor sobre el cual se filtran las clases
     * @return Coleccion de las clases filtradas
     */
    @Query(value = "select * from classes where date_of_init > now() AND name ILIKE CONCAT('%',:filter,'%')", nativeQuery = true)
    List<Clase> filterByName(@Param("filter") String name);

    /**
     * Obtiene la clase por lo descrito en su descripcion
     *
     * @param descr - Contenido de la descripcion
     * @return La clase con la descripcion descrita
     */
    @Query(value = "select * from classes where description = :descr", nativeQuery = true)
    Clase getClaseByDescription(@Param("descr") String descr);

    /**
     * Elimina una clase segun su identificador
     *
     * @param id - Identificador de la clase
     */
    @Transactional
    @Modifying
    @Query(value = "delete from classes c where c.id = :id", nativeQuery = true)
    void deleteClass(@Param("id") Long id);

}

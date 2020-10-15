package edu.eci.arsw.teachtome.persistence.repositories;

import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.RequestPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Conexión con base de Datos para la clase "Request"
 */
@Repository
public interface RequestRepository extends JpaRepository<Request, RequestPK> {

    /**
     * Obtiene las solicitudes de una clase
     *
     * @param classId - El identificador de la clase
     * @return Coleccion de las solicitudes de la clase
     */
    @Query(value = "select * from requests where class = :classId", nativeQuery = true)
    List<Request> getRequestsByClassId(@Param("classId") long classId);
}

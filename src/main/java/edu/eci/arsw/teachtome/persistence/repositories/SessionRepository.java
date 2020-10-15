package edu.eci.arsw.teachtome.persistence.repositories;

import edu.eci.arsw.teachtome.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio de Conexión con base de Datos para la clase "Session"
 */
public interface SessionRepository extends JpaRepository<Session, Long> {
    /**
     * Obtiene la sesion de una clase
     *
     * @param classId - Identificador de la clase
     * @return Sesion de la clase con el identificador dado
     */
    @Query(value = "select * from sessions where class = :classId", nativeQuery = true)
    Session getSessionByClassId(@Param("classId") long classId);
}

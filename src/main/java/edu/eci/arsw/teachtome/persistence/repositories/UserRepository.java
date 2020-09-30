package edu.eci.arsw.teachtome.persistence.repositories;

import edu.eci.arsw.teachtome.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio de Conexión con base de Datos para la clase "User"
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "select * from users where email = :emailUser", nativeQuery = true)
    List<User> getUserByEmail(@Param("emailUser") String emailUser);
}

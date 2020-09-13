package edu.eci.arsw.teachtome.persistence.repositories;

import edu.eci.arsw.teachtome.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Conexión con base de Datos para la clase "User"
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
}

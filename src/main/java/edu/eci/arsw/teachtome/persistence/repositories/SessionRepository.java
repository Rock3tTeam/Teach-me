package edu.eci.arsw.teachtome.persistence.repositories;

import edu.eci.arsw.teachtome.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}

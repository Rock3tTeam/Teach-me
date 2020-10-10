package edu.eci.arsw.teachtome.persistence.repositories;

import edu.eci.arsw.teachtome.model.Request;
import edu.eci.arsw.teachtome.model.RequestPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, RequestPK> {
    @Query(value = "select * from requests where class = :classId", nativeQuery = true)
    List<Request> getRequestsByClassId(@Param("classId") long classId);
}

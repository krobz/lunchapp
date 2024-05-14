package com.example.lunchapp.repository;

import com.example.lunchapp.model.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


/**
 * The SessionRepository interface provides CRUD operations for managing Session entities.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
}

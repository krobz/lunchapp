package com.example.lunchapp.repository;

import com.example.lunchapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

/**
 * The SessionRepository interface provides CRUD operations for managing User entities.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
}


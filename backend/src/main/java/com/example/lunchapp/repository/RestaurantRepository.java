package com.example.lunchapp.repository;

import com.example.lunchapp.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

/**
 * The SessionRepository interface provides CRUD operations for managing Restaurant entities.
 */
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {
    List<Restaurant> findBySessionId(UUID sessionId);
}

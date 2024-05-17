package com.example.lunchapp.repository;

import com.example.lunchapp.model.Session;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;


/**
 * The SessionRepository interface provides CRUD operations for managing Session entities.
 */
@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    @EntityGraph(attributePaths = {"restaurants"})
    Session findWithRestaurantsById(UUID sessionId);

    /**
     * Finds a session by ID and retrieves associated participants and restaurants.
     *
     * @param sessionId the ID of the session to find
     * @return the found session with associated participants and restaurants
     */
    @EntityGraph(attributePaths = {"participants", "restaurants"})
    Session findWithParticipantsandRestaurantsById(UUID sessionId);


    @EntityGraph(attributePaths = {"pickedRestaurant"})
    Session findWithPickedRestaurantById(UUID sessionId);
}

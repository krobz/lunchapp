package com.example.lunchapp.service;

import com.example.lunchapp.model.Restaurant;
import com.example.lunchapp.model.Session;
import com.example.lunchapp.model.User;
import com.example.lunchapp.repository.RestaurantRepository;
import com.example.lunchapp.repository.SessionRepository;
import com.example.lunchapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import lombok.extern.log4j.Log4j2;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


/**
 * The SessionService class provides methods for managing sessions.
 */
@Service
@Log4j2
public class SessionService {

    private final SessionRepository sessionRepository;
    private final RestaurantRepository restaurantRepository;

    private final UserRepository userRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    /**
     * Creates a new session for the given creator ID.
     *
     * @param creatorId the UUID of the creator
     * @return the created Session
     * @throws RuntimeException if the user is not found or an error occurs during creation
     */
    public Session createSession(UUID creatorId) {
        log.debug("Starting creating session for creator id {}", creatorId);
        Session session;
        try {
            User creator = userRepository.findById(creatorId)
                    .orElseThrow(() -> {
                        log.error("User not found with id {}", creatorId);
                        return new RuntimeException("User not found");
                    });
            session = Session.builder()
                    .creator(creator)
                    .isActive(true)
                    .build();
            addParticipant(session, creator);
            sessionRepository.save(session);
            log.debug("Finished creating session for creator id {}", creatorId);
        } catch (Exception e) {
            log.error("Error occurred while creating session for creator id {}", creatorId, e);
            throw e;
        }

        return session;
    }

    /**
     * Invites a user to a session.
     *
     * @param sessionId the ID of the session
     * @param inviterId the ID of the user sending the invitation
     * @param inviteeId the ID of the user being invited
     * @throws RuntimeException      if the session or user is not found
     * @throws IllegalStateException if the inviter is not the creator of the session
     */
    public void inviteUserToSession(UUID sessionId, UUID inviterId, UUID inviteeId) {
        log.debug("Starting inviting user to session for session id {}, inviter id {}, and invitee id {}", sessionId, inviterId, inviteeId);

        try {
            Session session = sessionRepository.findWithParticipantsandRestaurantsById(sessionId);
            if (session == null) {
                throw new RuntimeException("Session not found");
            }

            if (!session.isActive()) {
                throw new IllegalStateException("Cannot invite a user in a session that has already ended.");
            }

            User inviter = userRepository.findById(inviterId)
                    .orElseThrow(() -> {
                        log.error("User not found with id {}", inviterId);
                        return new RuntimeException("User not found");
                    });

            // Check if the inviter is the creator of the session
            if (!session.getCreator().equals(inviter)) {
                log.error("Invitation attempt by non-creator for session id {} and inviter id {}", sessionId, inviterId);
                throw new IllegalStateException("Only the session creator can invite users.");
            }

            User invitee = userRepository.findById(inviteeId)
                    .orElseThrow(() -> {
                        log.error("User not found with id {}", inviteeId);
                        return new RuntimeException("User not found");
                    });
            addParticipant(session, invitee);
            sessionRepository.save(session);

            log.debug("Finished inviting user to session for session id {}, inviter id {}, and invitee id {}", sessionId, inviterId, inviteeId);
        } catch (Exception e) {
            log.error("Error occurred while inviting user to session for session id {}, inviter id {}, and invitee id {}", sessionId, inviterId, inviteeId, e);
            throw e;
        }
    }

    /**
     * Asynchronously adds a restaurant to a session.
     *
     * @param sessionId  the ID of the session to add the restaurant to
     * @param userId     the ID of the user adding the restaurant
     * @param restaurant the restaurant to add
     * @return a CompletableFuture that completes when the restaurant is added successfully or throws an exception if an error occurs
     */
    @Async
    public CompletableFuture<Void> addRestaurantAsync(UUID sessionId, UUID userId, Restaurant restaurant) {
        return CompletableFuture.runAsync(() -> {
            log.debug("Adding restaurant to session {}, by user id {}", sessionId, userId);
            try {
                Session session = sessionRepository.findWithParticipantsandRestaurantsById(sessionId);
                if (session == null) {
                    throw new RuntimeException("Session not found");
                }

                User user = userRepository.findById(userId).orElseThrow(() -> {
                    log.error("User not authorized with id {}", userId);
                    return new RuntimeException("User not authorized");
                });

                if (!session.getParticipants().contains(user)) {
                    log.error("Non-participant user attempted to add restaurant for session id {} and user id {}", sessionId, userId);
                    throw new IllegalStateException("Only participants of the session can add restaurants.");
                }

                Restaurant existingRestaurant = restaurantRepository.findByName(restaurant.getName());
                // When adding a new restaurant
                if (existingRestaurant == null) {
                    addRestaurant(session, restaurant);
                    restaurantRepository.save(restaurant);
                }
                // save session
                sessionRepository.save(session);
            } catch (Exception e) {
                throw new RuntimeException("Error occurred during adding restaurant", e);
            }
        }).exceptionally(e -> {
            log.error("Error occurred while adding restaurant to session id {} by user id {}", sessionId, userId, e);
            return null;
        });
    }

    /**
     * Ends a session by the given session ID and user ID.
     *
     * @param sessionId the ID of the session
     * @param userId    the ID of the user
     * @return the restaurant that was picked in the session
     * @throws RuntimeException      if the session is not found
     * @throws IllegalStateException if the user is not the creator of the session
     */
    public String endSession(UUID sessionId, UUID userId) {
        log.debug("Starting endSession for session Id {} and user Id {}", sessionId, userId);
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        if (!session.getCreator().getId().equals(userId)) {
            throw new IllegalStateException("Only the creator of the session can end the session.");
        }

        String pickedRestaurant = endSession(session);
        log.debug("Finished endSession for session Id {} and user Id {}. Picked Restaurant: {}.", sessionId, userId, pickedRestaurant);
        return pickedRestaurant;
    }

    /**
     * Adds a restaurant to the session.
     *
     * @param session    the session to add the restaurant to
     * @param restaurant the restaurant to be added
     * @throws IllegalStateException if the session is not active (ended session)
     */
    public void addRestaurant(Session session, Restaurant restaurant) {
        if (!session.isActive()) {
            throw new IllegalStateException("Session already ended.");
        }
        session.getRestaurants().add(restaurant);
    }

    /**
     * Adds a participant to the given session.
     *
     * @param session the session to add the participant to
     * @param user    the user to add as a participant
     * @throws IllegalStateException if the session is not active
     */
    public void addParticipant(Session session, User user) {
        if (!session.isActive()) {
            throw new IllegalStateException("Session already ended.");
        }
        session.getParticipants().add(user);
    }

    /**
     * Ends the given session by setting it to inactive, picking a random restaurant from the session's restaurant list,
     * clearing the restaurant list, saving the session, and deleting all restaurants.
     *
     * @param session the session to be ended
     * @throws IllegalStateException if the session is already ended
     */
    public String endSession(Session session) {
        if (!session.isActive()) {
            throw new IllegalStateException("Session already ended.");
        }

        session.setActive(false);
        String pickedRestaurant = "";
        int size = session.getRestaurants().size();
        if (size > 0) {
            int itemIndex = new Random().nextInt(size);
            int i = 0;
            for (Restaurant restaurant : session.getRestaurants()) {
                if (i == itemIndex) {
                    pickedRestaurant = restaurant.getName();
                    break;
                }
                i++;
            }

//            session.setRestaurants(new HashSet<>());
//            sessionRepository.saveAndFlush(session);
        }
        sessionRepository.delete(session);

        restaurantRepository.deleteAll();
        return pickedRestaurant;
    }
}

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
            session.addParticipant(creator);
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
     * @param sessionId  the ID of the session
     * @param inviterId  the ID of the user sending the invitation
     * @param inviteeId  the ID of the user being invited
     * @throws RuntimeException if the session or user is not found
     * @throws IllegalStateException if the inviter is not the creator of the session
     */
    public void inviteUserToSession(UUID sessionId, UUID inviterId, UUID inviteeId) {
        log.debug("Starting inviting user to session for session id {}, inviter id {}, and invitee id {}", sessionId, inviterId, inviteeId);

        try {
            Session session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> {
                        log.error("Session not found with id {}", sessionId);
                        return new RuntimeException("Session not found");
                    });
            if (!session.isActive()) {
                throw new IllegalStateException("Cannot join a session that has already ended.");
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
            session.addParticipant(invitee);
            sessionRepository.save(session);

            log.debug("Finished inviting user to session for session id {}, inviter id {}, and invitee id {}", sessionId, inviterId, inviteeId);
        } catch (Exception e) {
            log.error("Error occurred while inviting user to session for session id {}, inviter id {}, and invitee id {}", sessionId, inviterId, inviteeId, e);
            throw e;
        }
    }

    @Async
    public CompletableFuture<Void> addRestaurantAsync(UUID sessionId, UUID userId, Restaurant restaurant) {
        log.debug("Adding restaurant to session {}, by user id {}", sessionId, userId);
        try {
            Session session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> {
                        log.error("Session not found with id {}", sessionId);
                        return new RuntimeException("Session not found");
                    });

            User user = userRepository.findById(userId).orElseThrow(() -> {
                log.error("User not authorized with id {}", userId);
                return new RuntimeException("User not authorized");
            });

            if (!session.getParticipants().contains(user)) {
                log.error("Non-participant user attempted to add restaurant for session id {} and user id {}", sessionId, userId);
                throw new IllegalStateException("Only participants of the session can add restaurants.");
            }
            restaurant.setSession(session);
            session.addRestaurant(restaurant);
            // save session and restaurant
            restaurantRepository.save(restaurant);
            sessionRepository.save(session);

            log.debug("Finished adding restaurant to session {}, by user id {}", sessionId, userId);
        } catch (Exception e) {
            log.error("Error occurred while adding restaurant to session id {} by user id {}", sessionId, userId, e);
            throw e;
        }
        return CompletableFuture.completedFuture(null);
    }

    public Restaurant endSession(UUID sessionId, UUID userId) {
        log.debug("Starting endSession for session Id {} and user Id {}", sessionId, userId);
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        if (!session.getCreator().getId().equals(userId)) {
            throw new IllegalStateException("Only the creator of the session can end the session.");
        }
        session.endSession();
        Restaurant pickedRestaurant = session.getPickedRestaurant();
        log.debug("Finished endSession for session Id {} and user Id {}. Picked Restaurant: {}.", sessionId, userId, pickedRestaurant.getName());
        return pickedRestaurant;
    }


}

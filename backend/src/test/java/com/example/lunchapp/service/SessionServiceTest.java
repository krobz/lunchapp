package com.example.lunchapp.service;

import com.example.lunchapp.model.Restaurant;
import com.example.lunchapp.model.Session;
import com.example.lunchapp.model.User;
import com.example.lunchapp.repository.RestaurantRepository;
import com.example.lunchapp.repository.SessionRepository;
import com.example.lunchapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * The SessionServiceTest class is a test class for the SessionService class.
 */
public class SessionServiceTest {

    @Mock
    SessionRepository sessionRepository;

    @Mock
    RestaurantRepository restaurantRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    SessionService sessionService;

    @BeforeEach
    public void initialize() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllSessionsTest() {
        Session session1 = new Session();
        Session session2 = new Session();
        when(sessionRepository.findAll()).thenReturn(Arrays.asList(session1, session2));

        List<Session> sessions = sessionService.getAllSessions();
        assertEquals(2, sessions.size());
    }

    @Test
    public void testCreateSession() {
        UUID creatorId = UUID.randomUUID();
        User creator = new User();
        when(userRepository.findById(creatorId)).thenReturn(Optional.of(creator));

        Session createdSession = sessionService.createSession(creatorId);
        assertEquals(creator, createdSession.getCreator());
    }

    @Test
    public void testCreateSessionUserNotFound() {
        UUID creatorId = UUID.randomUUID();
        when(userRepository.findById(creatorId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sessionService.createSession(creatorId));
    }


    @Test
    public void testGetSessionById() {
        UUID sessionId = UUID.randomUUID();
        Session session = new Session();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        Session fetchedSession = sessionService.getSessionById(sessionId);
        assertEquals(session, fetchedSession);
    }

    /**
     * Test case for the getSessionById method when the session is not found.
     * It verifies that the getSessionById method throws a RuntimeException
     * when the session with the given ID is not found in the SessionRepository.
     */
    @Test
    public void testGetSessionByIdNotFound() {
        UUID sessionId = UUID.randomUUID();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> sessionService.getSessionById(sessionId));
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
    @Test
    public void testInviteUserToSession() {
        UUID sessionId = UUID.randomUUID();
        UUID inviterId = UUID.randomUUID();
        UUID inviteeId = UUID.randomUUID();
        Session session = new Session();
        session.setActive(true);
        session.setCreator(new User());
        session.getCreator().setId(inviterId);
        User invitee = new User();
        when(sessionRepository.findWithParticipantsandRestaurantsById(sessionId)).thenReturn(session);
        when(userRepository.findById(inviterId)).thenReturn(Optional.of(session.getCreator()));
        when(userRepository.findById(inviteeId)).thenReturn(Optional.of(invitee));

        sessionService.inviteUserToSession(sessionId, inviterId, inviteeId);
        assertEquals(1, session.getParticipants().size());
        assertTrue(session.getParticipants().contains(invitee));
    }

    /**
     * Tests the {@code addRestaurantAsync} method of the {@code SessionService} class.
     * This method performs the following steps:
     * 1. Generates a random UUID for the session and user IDs.
     * 2. Creates a session object and sets the participants.
     * 3. Sets up mock configurations for the sessionRepository and userRepository.
     * 4. Calls the {@code addRestaurantAsync} method with the session ID, user ID, and a restaurant object.
     * 5. Verifies that the restaurant is added to the session and that the session is saved.
     *
     * @throws Exception if an error occurs during the test
     */
    @Test
    public void testAddRestaurantAsync() throws Exception {
        UUID sessionId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Session session = new Session();
        session.setActive(true);
        session.setParticipants(Collections.singleton(new User()));
        session.getParticipants().iterator().next().setId(userId);
        Restaurant restaurant = new Restaurant();

        when(sessionRepository.findWithParticipantsandRestaurantsById(sessionId)).thenReturn(session);
        when(userRepository.findById(userId)).thenReturn(Optional.of(session.getParticipants().iterator().next()));

        sessionService.addRestaurantAsync(sessionId, userId, restaurant).get();

        assertEquals(1, session.getRestaurants().size());
        assertTrue(session.getRestaurants().contains(restaurant));
    }

}

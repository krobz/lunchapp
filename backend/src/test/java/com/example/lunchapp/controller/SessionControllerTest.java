package com.example.lunchapp.controller;

import com.example.lunchapp.dto.AddRestaurantRequest;
import com.example.lunchapp.dto.InviteUsersRequest;
import com.example.lunchapp.model.Restaurant;
import com.example.lunchapp.model.Session;
import com.example.lunchapp.model.User;
import com.example.lunchapp.service.SessionService;
import com.example.lunchapp.service.UserService;
import com.example.lunchapp.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * This class contains unit tests for the SessionController class.
 */
class SessionControllerTest {

    @Mock
    private SessionService sessionService;

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private SessionController sessionController;

    private UUID mockUserId;
    private Session mockSession;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup security context mock
        mockUserId = UUID.randomUUID();
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(mockUserId.toString());

        // Create a mock session
        mockSession = new Session();
        mockSession.setId(UUID.randomUUID());
        mockSession.setActive(false);
        mockSession.setRestaurants(Collections.emptySet());
        mockSession.setParticipants(Collections.emptySet());
    }

    /**
     * Retrieves all existing sessions.
     *
     * @return A list of Session objects representing all sessions.
     */
    @Test
    void testGetAllSessions() {
        List<Session> sessions = Collections.singletonList(mockSession);
        when(sessionService.getAllSessions()).thenReturn(sessions);

        List<Session> result = sessionController.getAllSessions();


        assertEquals(sessions, result);
        verify(sessionService, times(1)).getAllSessions();
    }


    @Test
    void testGetSessionById() {
        UUID sessionId = UUID.randomUUID();
        Session session = new Session();
        when(sessionService.getSessionById(sessionId)).thenReturn(session);

        ResponseEntity<?> response = sessionController.getSessionById(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(session, response.getBody());
        verify(sessionService, times(1)).getSessionById(sessionId);
    }

    @Test
    void testGetSessionById_NotFound() {
        UUID sessionId = UUID.randomUUID();
        when(sessionService.getSessionById(sessionId)).thenThrow(new RuntimeException("Session not found"));

        ResponseEntity<?> response = sessionController.getSessionById(sessionId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Session not found", response.getBody());
        verify(sessionService, times(1)).getSessionById(sessionId);
    }


    /**
     * Method to test the creation of a new session.
     */
    @Test
    void testCreateSession() {
        Session session = new Session();
        when(sessionService.createSession(any(UUID.class))).thenReturn(session);

        ResponseEntity<?> response = sessionController.createSession();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(session, response.getBody());
        verify(sessionService, times(1)).createSession(any(UUID.class));
    }

    /**
     * Invites a user to a session.
     *
     * @param sessionId       the UUID of the session to invite the user to
     * @param request         the request object containing the inviter and invitee IDs
     * @return a ResponseEntity with the result of the invitation
     */
    @Test
    void testInviteUser() {
        UUID sessionId = UUID.randomUUID();
        InviteUsersRequest request = new InviteUsersRequest();
        request.setInviteeId(UUID.randomUUID());

        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<?> response = sessionController.inviteUser(sessionId, request, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).inviteUserToSession(eq(sessionId), any(UUID.class), eq(request.getInviteeId()));
    }

    @Test
    void testInviteUser_BindingErrors() {
        UUID sessionId = UUID.randomUUID();
        InviteUsersRequest request = new InviteUsersRequest();
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<?> response = sessionController.inviteUser(sessionId, request, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, times(0)).inviteUserToSession(any(UUID.class), any(UUID.class), any(UUID.class));
    }

    @Test
    void testJoinSession() {
        UUID sessionId = UUID.randomUUID();
        Session session = new Session();
        session.setActive(true);
        Set<User> Users = new HashSet<>();
        Users.add(new User());
        session.setParticipants(Users);

        when(sessionService.getSessionById(sessionId)).thenReturn(session);
        when(userService.getUserById(any(UUID.class))).thenReturn(new User());

        ResponseEntity<?> response = sessionController.joinSession(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully joined the session", response.getBody());
    }

    /**
     * This method is used to test adding a restaurant to a session.
     */
    @Test
    void testAddRestaurant() {
        UUID sessionId = UUID.randomUUID();
        AddRestaurantRequest request = new AddRestaurantRequest();
        request.setRestaurantName("Test Restaurant");

        when(bindingResult.hasErrors()).thenReturn(false);

        ResponseEntity<?> response = sessionController.addRestaurant(sessionId, request, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).addRestaurantAsync(eq(sessionId), any(UUID.class), any(Restaurant.class));
    }

    /**
     * This method is used to test the endSession() functionality of the SessionController class.
     * It verifies that the session is ended successfully and the response contains the picked restaurant.
     */
    @Test
    void testEndSession() {
        UUID sessionId = UUID.randomUUID();
        when(sessionService.endSession(any(UUID.class), any(UUID.class))).thenReturn("Picked Restaurant");

        ResponseEntity<?> response = sessionController.endSession(sessionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Picked Restaurant", response.getBody());
    }

    /**
     *
     * This method is used to test the behavior of the `endSession` method in the `SessionController` class when an `IllegalStateException` is thrown.
     * It verifies that an `IllegalStateException` is thrown when attempting to end a session that the user is not the creator of,
     * and that the response contains the correct status code and error message.
     *
     */
    @Test
    void testEndSession_IllegalStateException() {
        UUID sessionId = UUID.randomUUID();
        when(sessionService.endSession(any(UUID.class), any(UUID.class))).thenThrow(new IllegalStateException("Not allowed"));

        ResponseEntity<?> response = sessionController.endSession(sessionId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Not allowed", response.getBody());
    }
}

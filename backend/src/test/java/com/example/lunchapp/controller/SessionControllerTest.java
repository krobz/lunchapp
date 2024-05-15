package com.example.lunchapp.controller;

import com.example.lunchapp.dto.AddRestaurantRequest;
import com.example.lunchapp.dto.EndSessionRequest;
import com.example.lunchapp.dto.InviteUsersRequest;
import com.example.lunchapp.model.Restaurant;
import com.example.lunchapp.model.Session;
import com.example.lunchapp.service.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


/**
 * The SessionControllerTest class is responsible for testing the functionality of the SessionController class.
 *
 * Before each test, the initialize method is called to initialize the Mockito annotations.
 *
 */
public class SessionControllerTest {
    @Mock
    SessionService sessionService;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    SessionController sessionController;

    @BeforeEach
    public void initialize() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Tests the {@link SessionController#createSession(UUID)} method.
     *
     * Scenario:
     * - Generate a random UUID representing the creatorId.
     * - Create a new Session object.
     * - Configure the mock sessionService to return the testSession when createSession() is called with the creatorId.
     * - Call the createSession() method of the sessionController with the creatorId.
     * - Assert that the returned ResponseEntity has HTTP status code 200 (OK).
     * - Assert that the body of the response is equal to the testSession.
     */
    @Test
    public void createSessionTest() {
        UUID creatorId = UUID.randomUUID();
        Session testSession = new Session();
        given(sessionService.createSession(creatorId)).willReturn(testSession);
        ResponseEntity<?> result = sessionController.createSession(creatorId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testSession, result.getBody());
    }


    /**
     * Tests the inviteUser method of the SessionController class.
     *
     * Scenario:
     * - Generate a random UUID representing the sessionId.
     * - Create a new InviteUsersRequest object.
     * - Set the hasErrors method of the bindingResult to return false.
     * - Call the inviteUser method of the sessionController with the sessionId, request, and bindingResult.
     * - Assert that the returned ResponseEntity has HTTP status code 200 (OK).
     */
    @Test
    public void inviteUserTest() {
        UUID sessionId = UUID.randomUUID();
        InviteUsersRequest request = new InviteUsersRequest();
        given(bindingResult.hasErrors()).willReturn(false);
        ResponseEntity<?> result = sessionController.inviteUser(sessionId, request, bindingResult);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void addRestaurantTest() {
        UUID sessionId = UUID.randomUUID();
        AddRestaurantRequest request = new AddRestaurantRequest();
        given(bindingResult.hasErrors()).willReturn(false);
        ResponseEntity<?> result = sessionController.addRestaurant(sessionId, request, bindingResult);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    /**
     * Tests the {@link SessionController#EndSession(UUID, EndSessionRequest, BindingResult)} method.
     *
     * Scenario:
     * - Generate a random UUID to represent the sessionId.
     * - Create a new instance of EndSessionRequest.
     * - Create a new Restaurant instance.
     * - Configure the bindingResult object to return false for the hasErrors() method call.
     * - Configure the sessionService mock to return the restaurant object when endSession() is called with the sessionId and request.getUserId().
     * - Call the endSession() method of sessionController with the sessionId, request, and bindingResult.
     * - Assert that the returned ResponseEntity has HTTP status code 200 (OK).
     * - Assert that the body of the response is equal to the restaurant object.
     */
    @Test
    public void endSessionTest() {
        UUID sessionId = UUID.randomUUID();
        EndSessionRequest request = new EndSessionRequest();
        Restaurant restaurant = new Restaurant();
        given(bindingResult.hasErrors()).willReturn(false);
        given(sessionService.endSession(sessionId, request.getUserId())).willReturn(restaurant);
        ResponseEntity<?> result = sessionController.endSession(sessionId, request, bindingResult);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(restaurant, result.getBody());
    }

    /**
     * This method tests the scenario where an exception is thrown while creating a session.
     * It verifies that the appropriate HTTP response is returned with the error message.
     */
    @Test
    public void testCreateSessionIllegalStateException() {
        UUID creatorId = UUID.randomUUID();
        when(sessionService.createSession(creatorId)).thenThrow(new IllegalStateException("Session cannot be created"));
        ResponseEntity<?> result = sessionController.createSession(creatorId);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Session cannot be created", result.getBody());
    }


    /**
     * This method tests the scenario where an exception is thrown while trying to end a session.
     *
     */
    @Test
    public void testEndSessionIllegalStateException() {
        UUID sessionId = UUID.randomUUID();
        EndSessionRequest request = new EndSessionRequest();
        when(bindingResult.hasErrors()).thenReturn(false);
        when(sessionService.endSession(sessionId, request.getUserId())).thenThrow(new IllegalStateException("Session cannot be ended"));
        ResponseEntity<?> result = sessionController.endSession(sessionId, request, bindingResult);
        assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
        assertEquals("Session cannot be ended", result.getBody());
    }
}
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

    @Test
    public void createSessionTest() {
        UUID creatorId = UUID.randomUUID();
        Session testSession = new Session();
        given(sessionService.createSession(creatorId)).willReturn(testSession);
        ResponseEntity<?> result = sessionController.createSession(creatorId);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(testSession, result.getBody());
    }

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

    @Test
    public void testCreateSessionIllegalStateException() {
        UUID creatorId = UUID.randomUUID();
        when(sessionService.createSession(creatorId)).thenThrow(new IllegalStateException("Session cannot be created"));
        ResponseEntity<?> result = sessionController.createSession(creatorId);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertEquals("Session cannot be created", result.getBody());
    }


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
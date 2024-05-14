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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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

}

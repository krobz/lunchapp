package com.example.lunchapp;

import com.example.lunchapp.controller.SessionController;
import com.example.lunchapp.model.User;
import com.example.lunchapp.repository.UserRepository;
import com.example.lunchapp.service.SessionService;
import com.example.lunchapp.model.Session;
import com.example.lunchapp.repository.SessionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * LunchappApplicationTests is a class that contains test methods for testing the Lunch App application.
 */
@SpringBootTest
class LunchappApplicationTests {

    @Autowired
    private SessionService sessionService;

    @MockBean
    private SessionRepository sessionRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void testCreateSession() {
        Session sessionToSave = new Session();
        User creator = new User();
        UUID creatorId = UUID.randomUUID();

        sessionToSave.setCreator(creator);
        when(userRepository.findById(creatorId)).thenReturn(Optional.of(creator));
        when(sessionRepository.save(any(Session.class))).thenReturn(sessionToSave);

        // Test service layer
        Session savedSession = sessionService.createSession(creatorId);

        Assertions.assertEquals(creator, savedSession.getCreator());
        verify(userRepository).findById(creatorId);
        verify(sessionRepository).save(any(Session.class));
    }

    @Test
    void testCreateSessionController() throws Exception {
        // Setup MockMvc
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new SessionController(sessionService)).build();

        // Test controller layer
        mockMvc.perform(post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"creator\": \"d290f1ee-6c54-4b01-90e6-d701748f0851\"}"))
                .andExpect(status().isOk());

        verify(sessionService).createSession(UUID.fromString("d290f1ee-6c54-4b01-90e6-d701748f0851")); // verify if createSession invoked on service
    }
}

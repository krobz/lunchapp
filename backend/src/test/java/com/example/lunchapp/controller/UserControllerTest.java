package com.example.lunchapp.controller;

import com.example.lunchapp.model.User;
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
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * This class is used to test the UserController class.
 */
public class UserControllerTest {

    @Mock
    UserService userService;

    @Mock
    JwtUtil jwtUtil;

    @InjectMocks
    UserController userController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    UUID mockUserId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUserId = UUID.randomUUID();

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(mockUserId.toString());
    }

    @Test
    public void getAllUsersTest() {
        User user1 = new User();
        User user2 = new User();
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userController.getAllUsers();
        assertEquals(2, users.size());
    }

    /**
     * Retrieves a user by their ID and verifies that the correct user is returned with a success response.
     */
    @Test
    public void getUserByIdTest_success() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        when(userService.getUserById(userId)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserById(userId);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void getUserByIdTest_fail() {
        UUID userId = UUID.randomUUID();
        when(userService.getUserById(userId)).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<User> response = userController.getUserById(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void getUserByNameTest_success() {
        User user = new User();
        String userName = "John Doe";
        when(userService.getUserByName(userName)).thenReturn(user);

        ResponseEntity<User> response = userController.getUserByName(userName);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    public void getUserByNameTest_fail() {
        String userName = "John Doe";
        when(userService.getUserByName(userName)).thenThrow(new RuntimeException("User not found"));

        ResponseEntity<User> response = userController.getUserByName(userName);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void createUserTest_validationFail() {
        User user = new User();
        BindingResult bindingResult = new BindException(user, "user");
        bindingResult.addError(new FieldError("user", "email", "Email cannot be empty"));

        ResponseEntity<?> response = userController.createUser(user, bindingResult);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

//    @Test
//    public void createUserTest_success() {
//        User user = new User();
//        UUID userId = UUID.randomUUID(); // Set user ID
//        user.setId(userId); // Assign the ID to the user
//        BindingResult bindingResult = mock(BindingResult.class);
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(userService.createUser(user)).thenReturn(user);
//        when(jwtUtil.generateToken(user.getId().toString())).thenReturn("mockToken");
//
//        ResponseEntity<?> response = userController.createUser(user, bindingResult);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(user, response.getBody());
//        assertEquals("Bearer mockToken", response.getHeaders().getFirst("Authorization"));
//    }

    @Test
    public void createUserTest_fail() {
        User user = new User();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.createUser(user)).thenThrow(new RuntimeException("Error creating user"));

        ResponseEntity<?> response = userController.createUser(user, bindingResult);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void deleteUserTest_success() {
        UUID userId = UUID.randomUUID();

        ResponseEntity<Void> response = userController.deleteUser(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void deleteUserTest_fail() {
        UUID userId = UUID.randomUUID();
        doThrow(new RuntimeException("User not found")).when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}

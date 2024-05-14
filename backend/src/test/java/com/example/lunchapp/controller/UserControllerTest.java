package com.example.lunchapp.controller;

import com.example.lunchapp.model.User;
import com.example.lunchapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.UUID;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.BDDMockito.given;

/**
 * The UserControllerTest class is responsible for testing the functionality of the UserController class.
 */
public class UserControllerTest {

    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @BeforeEach
    public void initialize() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * unit test for the getAllUsers() method
     */
    @Test
    public void getAllUsersTest() {
        User user1 = new User();
        User user2 = new User();
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userController.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void getUserByIdTest() {
        User user = new User();
        UUID userId = UUID.randomUUID();
        when(userService.getUserById(userId)).thenReturn(user);

        assertEquals(user, userController.getUserById(userId).getBody());
        assertEquals(HttpStatus.OK, userController.getUserById(userId).getStatusCode());
    }

    @Test
    public void createUserTest() {
        User user = new User();
        given(userService.createUser(user)).willReturn(user);

        assertEquals(user, userController.createUser(user, null).getBody());
        assertEquals(HttpStatus.CREATED, userController.createUser(user, null).getStatusCode());
    }

    @Test
    public void deleteUserTest() {
        UUID userId = UUID.randomUUID();
        assertEquals(HttpStatus.NO_CONTENT, userController.deleteUser(userId).getStatusCode());
    }
}
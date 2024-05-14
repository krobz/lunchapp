package com.example.lunchapp.service;

import com.example.lunchapp.model.User;
import com.example.lunchapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


/**
 * This class contains unit tests for the UserService class.
 */
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllUsersTest() {
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void getUserByIdTest() {
        UUID id = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User foundUser = userService.getUserById(id);
        assertEquals(user, foundUser);

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void getUserByIdNotFoundTest() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, ()-> userService.getUserById(id));

        verify(userRepository, times(1)).findById(id);
    }

    @Test
    public void createUserTest() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userService.createUser(user);
        assertEquals(user, savedUser);

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void deleteUserTest() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(true);

        userService.deleteUser(id);

        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteUserNotFoundTest() {
        UUID id = UUID.randomUUID();
        when(userRepository.existsById(id)).thenReturn(false);

        assertThrows(RuntimeException.class, ()-> userService.deleteUser(id));

        verify(userRepository, times(1)).existsById(id);
    }
}

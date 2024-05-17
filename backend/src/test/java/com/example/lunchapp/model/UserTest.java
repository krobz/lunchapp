package com.example.lunchapp.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * The UserTest class is a unit test class that tests the functionality of the User class.
 */
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testGetterAndSetter() {

        UUID id = UUID.randomUUID();
        user.setId(id);
        assertEquals(id, user.getId());

        String name = "Test User";
        user.setName(name);
        assertEquals(name, user.getName());

        String email = "test@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }
}

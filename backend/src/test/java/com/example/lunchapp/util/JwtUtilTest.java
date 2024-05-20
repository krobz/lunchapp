package com.example.lunchapp.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class contains unit tests for the JwtUtil class.
 * It uses SpringBootTest and ExtendWith annotations for integration testing.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { TestPropertyValues.class, JwtUtil.class })
public class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void whenGenerateToken_thenSuccess(){
        String username = "testUser";
        String token = jwtUtil.generateToken(username);
        assertNotNull(token);
    }

    @Test
    public void whenValidateToken_thenSuccess(){
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        boolean isValid = jwtUtil.validateToken(token);
        assertTrue(isValid);
    }

    @Test
    public void whenGetUsernameFromToken_thenSuccess(){
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        String parsedUsername = jwtUtil.getUsernameFromToken(token);
        assertEquals(username, parsedUsername);
    }

    @Test
    public void whenDecodeToken_thenSuccess(){
        String username = "testUser";
        String token = jwtUtil.generateToken(username);

        Claims claims = jwtUtil.decodeToken(token);
        assertNotNull(claims);
        assertEquals(username, claims.getSubject());
    }
}

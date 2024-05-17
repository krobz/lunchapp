package com.example.lunchapp.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class RestaurantTest {

    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        restaurant = new Restaurant();
    }

    @Test
    void testGetterAndSetter() {

        UUID id = UUID.randomUUID();
        restaurant.setId(id);
        assertEquals(id, restaurant.getId());

        String name = "Test Restaurant";
        restaurant.setName(name);
        assertEquals(name, restaurant.getName());
    }
}

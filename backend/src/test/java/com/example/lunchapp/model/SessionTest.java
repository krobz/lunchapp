package com.example.lunchapp.model;

import com.example.lunchapp.model.Restaurant;
import com.example.lunchapp.model.Session;
import com.example.lunchapp.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

class SessionTest {

    private Session session;
    private User creator, participant;
    private Restaurant restaurant, pickedRestaurant;

    @BeforeEach
    void setUp() {
        session = new Session();
        participant = new User();
        creator = new User();
        restaurant = new Restaurant();
        pickedRestaurant = new Restaurant();

        Set<User> participants = new HashSet<>();
        participants.add(participant);

        Set<Restaurant> restaurants = new HashSet<>();
        restaurants.add(restaurant);

        session.setParticipants(participants);
        session.setRestaurants(restaurants);
        session.setCreator(creator);
        session.setPickedRestaurant(pickedRestaurant);
    }

    @Test
    void testGetters() {
        assertEquals(Collections.singleton(participant), session.getParticipants());
        assertEquals(Collections.singleton(restaurant), session.getRestaurants());
        assertEquals(creator, session.getCreator());
        assertEquals(pickedRestaurant, session.getPickedRestaurant());
    }

    @Test
    void testSetters() {
        User newCreator = new User();
        session.setCreator(newCreator);
        assertEquals(newCreator, session.getCreator());

        Restaurant newPickedRestaurant = new Restaurant();
        session.setPickedRestaurant(newPickedRestaurant);
        assertEquals(newPickedRestaurant, session.getPickedRestaurant());

        Set<User> newParticipants = new HashSet<>();
        session.setParticipants(newParticipants);
        assertEquals(newParticipants, session.getParticipants());

        Set<Restaurant> newRestaurants = new HashSet<>();
        session.setRestaurants(newRestaurants);
        assertEquals(newRestaurants, session.getRestaurants());
    }

    @Test
    void testIsActive() {
        assertFalse(session.isActive());
        session.setActive(true);
        assertTrue(session.isActive());
    }
}

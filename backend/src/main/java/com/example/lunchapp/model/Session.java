package com.example.lunchapp.model;

import lombok.*;
import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Session {
    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @OneToMany(mappedBy = "session")
    private List<Restaurant> restaurants;

    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToMany
    @JoinTable(
            name = "session_users",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> participants;

    @OneToOne
    private Restaurant pickedRestaurant;

    private boolean isActive = false;

    public void addRestaurant(Restaurant restaurant) {
        if (!isActive) {
            throw new IllegalStateException("Session already ended.");
        }
        this.restaurants.add(restaurant);
        restaurant.setSession(this);
    }

    public void addParticipant(User user) {
        if (!isActive) {
            throw new IllegalStateException("Session already ended.");
        }
        this.participants.add(user);
    }

    public void endSession() {
        if (!isActive) {
            throw new IllegalStateException("Session already ended.");
        }

        this.isActive = false;
        int randomIndex = new Random().nextInt(restaurants.size());
        pickedRestaurant = restaurants.get(randomIndex);
    }

    public Restaurant getPickedRestaurant() {
        if (!isActive) {
            return this.pickedRestaurant;
        }
        else {
            throw new RuntimeException("Session is still running");
        }
    }
}


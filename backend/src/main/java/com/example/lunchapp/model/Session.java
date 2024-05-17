package com.example.lunchapp.model;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

/**
 * The Session class represents a session in which users can participate and choose a restaurant to visit.
 */
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

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "session_restaurants",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "restaurant_id")
    )
    @Builder.Default
    private Set<Restaurant> restaurants = new HashSet<>();



    @ManyToOne
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "session_users",
            joinColumns = @JoinColumn(name = "session_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Builder.Default
    private Set<User> participants = new HashSet<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "picked_restaurant_id")
    private Restaurant pickedRestaurant;

    private boolean isActive = false;

}

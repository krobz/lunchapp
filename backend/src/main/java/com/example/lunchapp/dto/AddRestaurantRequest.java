package com.example.lunchapp.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;
import com.example.lunchapp.model.Restaurant;

import javax.validation.constraints.NotNull;


/**
 * The AddRestaurantRequest class represents a request to add a restaurant to a session. It contains the user ID and the restaurant object.
 * It is used in the process of adding a restaurant to a session.
 */
@Getter
@Setter
public class AddRestaurantRequest{
    @NotNull(message = "User Id cannot be null")
    private UUID userId;

    @NotNull(message = "Restaurant cannot be null")
    private Restaurant restaurant;
}

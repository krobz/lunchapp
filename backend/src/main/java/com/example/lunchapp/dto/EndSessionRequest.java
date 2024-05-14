package com.example.lunchapp.dto;

import javax.validation.constraints.NotNull;
import java.util.UUID;


/**
 * The EndSessionRequest class represents a request to end a user session.
 * It contains the user ID of the session to be ended.
 */
public class EndSessionRequest {
    @NotNull(message = "User Id cannot be null")
    private UUID userId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
package com.example.lunchapp.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;


/**
 * The {@code InviteUsersRequest} class is a  object that
 * encapsulates the information required to send an invitation request
 * from the inviter to the invitee.
 */
@Getter
@Setter
public class InviteUsersRequest {
    @NotNull(message = "Inviter Id cannot be null")
    private UUID inviterId;

    @NotNull(message = "Invitee Id cannot be null")
    private UUID inviteeId;
}

package com.example.lunchapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Builder
@Getter
@Setter
public class SessionResponse {
    private String sessionId;
    private UUID creatorId;
    private Set<UUID> participants;
}

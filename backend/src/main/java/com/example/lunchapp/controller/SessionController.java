package com.example.lunchapp.controller;

import com.example.lunchapp.dto.AddRestaurantRequest;
import com.example.lunchapp.dto.InviteUsersRequest;
import com.example.lunchapp.model.Restaurant;
import com.example.lunchapp.model.Session;
import com.example.lunchapp.service.SessionService;
import com.example.lunchapp.service.UserService;
import com.example.lunchapp.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import lombok.extern.log4j.Log4j2;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


/**
 * The SessionController class handles API requests related to sessions.
 */
@RestController
@RequestMapping("/sessions")
@Log4j2
public class SessionController {

    private final SessionService sessionService;

    private final UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public SessionController(SessionService sessionService, UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @Operation(summary = "Get all existing sessions")
    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    /**
     * Retrieves a session by its ID.
     *
     * @param sessionId The ID of the session to retrieve.
     * @return The ResponseEntity containing the session if found, or an error message if something wrong.
     */
    @Operation(summary = "Get a session by ID")
    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSessionById(@PathVariable UUID sessionId) {
        log.debug("getSessionById API called with sessionId: {}", sessionId);
        try {
            Session session = sessionService.getSessionById(sessionId);
            return ResponseEntity.ok(session);
        } catch (RuntimeException e) {
            log.error("Error fetching session with sessionId: {}", sessionId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @Operation(summary = "Creates a new session")
    @PostMapping("/create")
    public ResponseEntity<?> createSession() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId;
            if (authentication != null) {
                log.info("Authentication details: {}", authentication);
                userId = authentication.getName();  // Ensure this is not null or incorrect
                log.info("User ID from Authentication: {}", userId);
            } else {
                log.warn("No Authentication object could be retrieved from SecurityContext");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            UUID creatorId = UUID.fromString(userId);
            Session newSession = sessionService.createSession(creatorId);

            log.info("Session created for creatorId: {}", creatorId);
            return ResponseEntity.ok(newSession);
        } catch (IllegalArgumentException e) {
            log.error("Invalid user ID format", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user ID format");
        } catch (Exception e) {
            log.error("Error occurred while creating session", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the session");
        }
    }

    /**
     * Invite a user to a session.
     *
     * @param sessionId       the UUID of the session to invite the user to
     * @param request         the request object containing the inviter and invitee IDs
     * @param bindingResult   the binding result of the request validation
     * @return a ResponseEntity with the result of the invitation
     */
    @Operation(summary = "Invite a user to a session")
    @PostMapping("/{sessionId}/invite")
    public ResponseEntity<?> inviteUser(@PathVariable UUID sessionId, @Valid @RequestBody InviteUsersRequest request,
                                        BindingResult bindingResult) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(authentication.getName());

        // validate the param
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        log.debug("invite API called for session Id {} and request {}", sessionId, request);

        try {
            sessionService.inviteUserToSession(sessionId, userId, request.getInviteeId());
            return ResponseEntity.ok().body(sessionId.toString());
        } catch (IllegalStateException e) {
            // if inviter is not the creator of this session
            log.error("Invitation error for session Id {}, inviter Id {}, invitee Id {}", sessionId, userId, request.getInviteeId(), e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error occurred while inviting user for session Id {}, inviter Id {}, invitee Id {}", sessionId, userId, request.getInviteeId(), e);
            if (e.getMessage().equals("Session not found") || e.getMessage().equals("User not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            else {
                // TODO: Other erros should return a 500 status
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
        }
    }

    @PostMapping("/api/joinSession")
    public ResponseEntity<?> joinSession(@RequestParam UUID sessionId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(authentication.getName());

        try {
            Session session = sessionService.getSessionById(sessionId);

            if (!session.isActive()) {
                return ResponseEntity.badRequest().body("Session is not active");
            }

            if (!session.getParticipants().contains(userService.getUserById(userId))) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User is not invited to this session");
            }


            return ResponseEntity.ok("Successfully joined the session");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while joining the session: " + e.getMessage());
        }
    }

    /**
     * submit a restaurant to a session.
     *
     * @param sessionId the unique identifier for the session
     * @param request the request containing the details of the restaurant to be added
     * @return a ResponseEntity object indicating the success or failure of the operation
     */
    @Operation(summary = "Submit a restaurant to a session")
    @PostMapping("/{sessionId}/restaurants")
    public ResponseEntity<?> addRestaurant(@PathVariable UUID sessionId, @Valid @RequestBody AddRestaurantRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(authentication.getName());

        Restaurant restaurant = Restaurant.builder()
                .name(request.getRestaurantName())
                .build();

        log.debug("addRestaurant is called for session Id {} and request {}", sessionId, request);
        sessionService.addRestaurantAsync(sessionId, userId, restaurant);
        return ResponseEntity.ok().build();
    }

    /**
     * Ends the session identified by the given session ID and user request.
     *
     * @param sessionId The ID of the session to be ended.
     * @param request The request object containing the user ID.
     * @return ResponseEntity<?> The response entity representing the result of ending the session. If the session is ended successfully, it returns an OK response with the picked
     * restaurant. If an illegal operation occurs, it returns a FORBIDDEN response with the error message. If the session or user is not found, it returns a NOT_FOUND response with
     * the error message. For other errors, it returns an INTERNAL_SERVER_ERROR response with a generic message.
     */
    @Operation(summary = "Ends the session identified by the given session ID and user request")
    @PostMapping("/{sessionId}/end")
    public ResponseEntity<?> endSession(@PathVariable UUID sessionId) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userId = UUID.fromString(authentication.getName());

        log.debug("End session is called for session Id {} by user {}", sessionId, userId);
        try {
            String pickedRestaurant = sessionService.endSession(sessionId, userId);
            return ResponseEntity.ok(pickedRestaurant);
        } catch (IllegalStateException e) {
            log.error("Illegal operation while ending a session for session Id {} by user {}", sessionId, userId, e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error occurred while ending a session for session Id {} by user {}", sessionId, userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }
}

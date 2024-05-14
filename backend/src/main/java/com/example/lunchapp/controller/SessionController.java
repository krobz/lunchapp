package com.example.lunchapp.controller;

import com.example.lunchapp.dto.AddRestaurantRequest;
import com.example.lunchapp.dto.EndSessionRequest;
import com.example.lunchapp.dto.InviteUsersRequest;
import com.example.lunchapp.model.Restaurant;
import com.example.lunchapp.model.Session;
import com.example.lunchapp.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    /**
     * Creates a new session with the specified creator ID.
     *
     * @param creatorId the ID of the user who is creating the session
     * @return a ResponseEntity object representing the HTTP response, with the created session as the response body
     */
    @PostMapping("/create")
    public ResponseEntity<?> createSession(@RequestParam UUID creatorId) {
        log.debug("createSession API called with creatorId: {}", creatorId);
        try {
            Session newSession = sessionService.createSession(creatorId);
            return ResponseEntity.ok(newSession);
        } catch (RuntimeException e) {
            log.error("Error creating session with creatorId: {}", creatorId, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{sessionId}/invite")
    public ResponseEntity<?> inviteUser(@PathVariable UUID sessionId, @Valid @RequestBody InviteUsersRequest request,
                                        BindingResult bindingResult) {
        // validate the param
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        log.debug("invite API called for session Id {} and request {}", sessionId, request);

        try {
            sessionService.inviteUserToSession(sessionId, request.getInviterId(), request.getInviteeId());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            // if inviter is not the creator of this session
            log.error("Invitation error for session Id {}, inviter Id {}, invitee Id {}", sessionId, request.getInviterId(), request.getInviteeId(), e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error occurred while inviting user for session Id {}, inviter Id {}, invitee Id {}", sessionId, request.getInviterId(), request.getInviteeId(), e);
            if (e.getMessage().equals("Session not found") || e.getMessage().equals("User not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            else {
                // TODO: Other erros should return a 500 status
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
        }
    }

    /**
     * submit a restaurant to the session.
     *
     * @param sessionId the unique identifier for the session
     * @param request the request containing the details of the restaurant to be added
     * @return a ResponseEntity object indicating the success or failure of the operation
     */
    @PostMapping("/{sessionId}/restaurants")
    public synchronized ResponseEntity<?> addRestaurant(@PathVariable UUID sessionId, @Valid @RequestBody AddRestaurantRequest request,
                                                           BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        log.debug("addRestaurant is called for session Id {} and request {}", sessionId, request);
        sessionService.addRestaurant(sessionId, request.getUserId(), request.getRestaurant());
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
    @PostMapping("/{sessionId}/end")
    public ResponseEntity<?> endSession(@PathVariable UUID sessionId, @Valid @RequestBody EndSessionRequest request,
                                        BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        log.debug("End session is called for session Id {} by user {}", sessionId, request.getUserId());
        try {
            Restaurant pickedRestaurant = sessionService.endSession(sessionId, request.getUserId());
            return ResponseEntity.ok(pickedRestaurant);
        } catch (IllegalStateException e) {
            log.error("Illegal operation while ending a session for session Id {} by user {}", sessionId, request.getUserId(), e);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error occurred while ending a session for session Id {} by user {}", sessionId, request.getUserId(), e);
            if (e.getMessage().equals("Session not found") || e.getMessage().equals("User not authorized")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            } else {
                // TODO: Other erros should return a 500 status
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
            }
        }
    }
}

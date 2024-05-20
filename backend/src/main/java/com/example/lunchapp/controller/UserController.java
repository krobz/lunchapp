package com.example.lunchapp.controller;

import com.example.lunchapp.model.User;
import com.example.lunchapp.service.UserService;
import com.example.lunchapp.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
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
 * The UserController class is responsible for handling HTTP requests related to user operations.
 */
@RestController
@RequestMapping("/users")
@Log4j2
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Get all users")
    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Get all users called");
        return userService.getAllUsers();
    }

    @Operation(summary = "Get users by id")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        log.debug("Getting user api called by ID: {}", id);
        User user = null;
        try {
            user = userService.getUserById(id);
        } catch (Exception e) {
            log.error("An error occurred while getting user with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Retrieved user by ID: {}", id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Retrieves a user by their name.
     * Need to get id by other users' names without login function.
     * @param name The name of the user to retrieve.
     * @return ResponseEntity<User> The HTTP response containing the user if found, or an error response if not found or an exception occurred.
     */
    @Operation(summary = "Retrieves a user by their name")
    @GetMapping("/name/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        log.debug("Getting user by name: {}", name);
        User user;
        try {
            user = userService.getUserByName(name);
        } catch (Exception e) {
            log.error("An error occurred while getting user with name: {}", name, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        log.info("Retrieved user by name: {}", name);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Create new user")
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        User createdUser;
        try {
            createdUser = userService.createUser(user);
        } catch (Exception e) {
            log.error("An error occurred while creating a user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        String token = jwtUtil.generateToken(createdUser.getId().toString());
        log.info("Created user. ID of the created user: {}", createdUser.getId());

        return ResponseEntity.ok().header("Authorization", "Bearer " + token).body(createdUser);
    }

    @Operation(summary = "Delete existing user")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        log.debug("Deleting user api called by ID: {}", id);
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            log.error("An error occurred while deleting user with ID: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

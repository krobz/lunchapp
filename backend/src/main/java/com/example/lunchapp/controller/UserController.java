package com.example.lunchapp.controller;

import com.example.lunchapp.model.User;
import com.example.lunchapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import lombok.extern.log4j.Log4j2;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


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

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Get all users called");
        List<User> users = userService.getAllUsers();
        return users;
    }

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

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {

        // validate the param
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        User createdUser = null;
        try {
            createdUser = userService.createUser(user);
        } catch (Exception e) {
            log.error("An error occurred while creating a user", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("Created user. ID of the created user: {}", createdUser.getId());
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

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

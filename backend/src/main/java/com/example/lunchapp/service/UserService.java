package com.example.lunchapp.service;

import com.example.lunchapp.model.User;
import com.example.lunchapp.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


/**
 * Service class for managing user operations.
 */
@Service
@Log4j2
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        List<User> users = userRepository.findAll();
        log.debug("Found {} users", users.size());
        return users;
    }

    public User getUserById(UUID id) {
        log.debug("Executing getUserById for id {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> {
            log.error("User with id {} not found", id);
            return new RuntimeException("User not found");
        });
        log.debug("Found user with id {}", id);
        return user;
    }

    public User createUser(User user) {
        log.debug("Executing createUser for user {}", user);
        User savedUser = userRepository.save(user);
        log.debug("Created user with id {}", savedUser.getId());
        return savedUser;
    }

    public void deleteUser(UUID id) {
        log.debug("Executing deleteUser for id {}", id);
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            log.debug("Deleted user with id {}", id);
        } else {
            log.error("User with id {} not found", id);
            throw new RuntimeException("User not found");
        }
    }
}
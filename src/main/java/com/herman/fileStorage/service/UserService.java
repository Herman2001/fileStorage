package com.herman.fileStorage.service;

import com.herman.fileStorage.entity.User;
import com.herman.fileStorage.exception.ForbiddenException;
import com.herman.fileStorage.exception.ResourceAlreadyExistsException;
import com.herman.fileStorage.exception.ResourceNotFoundException;
import com.herman.fileStorage.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Service class for User operations
 */

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Register a new user with hashed password
     *
     * @param username the username of the new user
     * @param passwordString the password in plain text
     * @return the saved User
     * @throws ResourceNotFoundException if username is already taken
     */
    public User registerUser(String username, String passwordString) {
        if (userRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException("Username already exists.");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(passwordString);
        User newUser = new User(UUID.randomUUID(), username, encodedPassword);
        return userRepository.save(newUser);
    }

    /**
     * Finds a user by their username
     *
     * @param username the username to find
     * @return Optional containing the User if found, else empty
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Authenticates a user by their username and password
     *
     * @param username the username
     * @param password the password in plain text
     * @return the authenticated user
     * @throws ResourceNotFoundException if username not found
     * @throws ForbiddenException if password is invalid
     */
    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid username or password."));
        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
            throw new ForbiddenException("Invalid username or password.");
        }
        return user;
    }

    /**
     * Finds a user by ID.
     *
     * @param id the UUID of the user
     * @return Optional containing the User if found, else empty
     */
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    /**
     * To find or Create a GitHub User
     *
     * @param githubId the githubId of the user
     * @param username the username of the user
     * @return the user
     */
    public User findOrCreateGithubUser(String githubId, String username) {
        return userRepository.findByGithubId(githubId)
                .orElseGet(() -> {
                    String finalUsername = username;
                    if (userRepository.existsByUsername(finalUsername)) {
                        finalUsername = username + "-" + githubId;
                    }
                    User newUser = new User();
                    newUser.setId(UUID.randomUUID());
                    newUser.setUsername(finalUsername);
                    newUser.setGithubId(githubId);
                    return userRepository.save(newUser);
                });
    }
}

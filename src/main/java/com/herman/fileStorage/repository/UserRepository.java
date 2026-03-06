package com.herman.fileStorage.repository;

import com.herman.fileStorage.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repo interface for User Entity
 * Provides CRUD operations and query methods
 */

public interface UserRepository extends JpaRepository<User,UUID> {

    /**
     * Finds a users by their username
     *
     * @param username the username to search for
     * @return an optional containing the User if found, else empty
     */
    Optional<User> findByUsername(String username);

    /**
     * Checks if a user with the username does exist
     *
     * @param username the username to check for
     * @return true if a user with the username exists, else false
     */
    boolean existsByUsername(String username);

    /**
     * Finds a user by their GitHub id
     *
     * @param gitHubId the GitHub id to check for
     * @return an optional containing the user if found, else empty
     */
    Optional<User> findByGithubId(String gitHubId);
}

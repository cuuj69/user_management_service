package com.tech11.usermanagement.repository;

import com.tech11.usermanagement.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity operations.
 * Provides data access methods using JPA.
 */
@ApplicationScoped
@Transactional
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;
    
    private boolean tableInitialized = false;
    
    /**
     * Initialize the database table if it doesn't exist.
     */
    private void initializeTableIfNeeded() {
        if (!tableInitialized) {
            try {
                // Drop existing table if it exists (for schema migration)
                try {
                    entityManager.createNativeQuery("DROP TABLE IF EXISTS users").executeUpdate();
                } catch (Exception e) {
                    // Ignore errors
                }
                
                // Create the table with UUID
                entityManager.createNativeQuery(
                    "CREATE TABLE users (" +
                    "id UUID PRIMARY KEY, " +
                    "first_name VARCHAR(255) NOT NULL, " +
                    "last_name VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(255) NOT NULL UNIQUE, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "birthday DATE NOT NULL, " +
                    "created_at TIMESTAMP NOT NULL, " +
                    "updated_at TIMESTAMP, " +
                    "version BIGINT DEFAULT 0" +
                    ")"
                ).executeUpdate();
                
                tableInitialized = true;
            } catch (Exception e) {
                // Table might already exist, mark as initialized
                tableInitialized = true;
            }
        }
    }
    

    

    


    /**
     * Find all users with pagination.
     *
     * @param page the page number (0-based)
     * @param size the page size
     * @return list of users for the specified page
     */
    public List<User> findAll(int page, int size) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u ORDER BY u.id", User.class);
        query.setFirstResult(page * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    /**
     * Count total number of users.
     *
     * @return total count of users
     */
    public long count() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u", Long.class);
        return query.getSingleResult();
    }

    /**
     * Find user by ID.
     *
     * @param id the user ID
     * @return Optional containing the user if found
     */
    public Optional<User> findById(UUID id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    /**
     * Find user by email.
     *
     * @param email the user email
     * @return Optional containing the user if found
     */
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        List<User> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /**
     * Save a new user.
     *
     * @param user the user to save
     * @return the saved user with generated ID
     */
    public User save(User user) {
        entityManager.persist(user);
        entityManager.flush(); // Ensure ID is generated
        return user;
    }

    /**
     * Update an existing user.
     *
     * @param user the user to update
     * @return the updated user
     */
    public User update(User user) {
        return entityManager.merge(user);
    }

    /**
     * Delete a user by ID.
     *
     * @param id the user ID to delete
     * @return true if user was deleted, false if not found
     */
    public boolean deleteById(UUID id) {
        Optional<User> userOpt = findById(id);
        if (userOpt.isPresent()) {
            entityManager.remove(userOpt.get());
            return true;
        }
        return false;
    }

    /**
     * Check if a user with the given email exists.
     *
     * @param email the email to check
     * @return true if user exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        initializeTableIfNeeded();
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.getSingleResult() > 0;
    }

    /**
     * Check if a user with the given email exists, excluding a specific user ID.
     *
     * @param email the email to check
     * @param excludeId the user ID to exclude from the check
     * @return true if user exists, false otherwise
     */
    public boolean existsByEmailExceptId(String email, UUID excludeId) {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT COUNT(u) FROM User u WHERE u.email = :email AND u.id != :excludeId", Long.class);
        query.setParameter("email", email);
        query.setParameter("excludeId", excludeId);
        return query.getSingleResult() > 0;
    }

    /**
     * Find users by first name.
     *
     * @param firstName the first name to search for
     * @return list of users with matching first name
     */
    public List<User> findByFirstName(String firstName) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.firstName = :firstName", User.class);
        query.setParameter("firstName", firstName);
        return query.getResultList();
    }

    /**
     * Find users by last name.
     *
     * @param lastName the last name to search for
     * @return list of users with matching last name
     */
    public List<User> findByLastName(String lastName) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.lastName = :lastName", User.class);
        query.setParameter("lastName", lastName);
        return query.getResultList();
    }

    /**
     * Find users by first name and last name.
     *
     * @param firstName the first name to search for
     * @param lastName the last name to search for
     * @return list of users with matching first and last name
     */
    public List<User> findByFirstNameAndLastName(String firstName, String lastName) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.firstName = :firstName AND u.lastName = :lastName", User.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        return query.getResultList();
    }

    /**
     * Find users by partial first name match.
     * This method allows searching users where the first name starts with the provided prefix.
     * For example, searching with "Joh" would find users with names like "John", "Johanna", etc.
     *
     * @param firstNamePrefix the first name prefix to search for (minimum 3 characters recommended)
     * @return list of users with matching first name prefix
     */
    public List<User> findByFirstNameStartingWith(String firstNamePrefix) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.firstName LIKE :firstNamePrefix ORDER BY u.firstName", User.class);
        query.setParameter("firstNamePrefix", firstNamePrefix + "%");
        return query.getResultList();
    }

    /**
     * Find users by partial last name match.
     * This method allows searching users where the last name starts with the provided prefix.
     * For example, searching with "Smi" would find users with names like "Smith", "Smithson", etc.
     *
     * @param lastNamePrefix the last name prefix to search for (minimum 3 characters recommended)
     * @return list of users with matching last name prefix
     */
    public List<User> findByLastNameStartingWith(String lastNamePrefix) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.lastName LIKE :lastNamePrefix ORDER BY u.lastName", User.class);
        query.setParameter("lastNamePrefix", lastNamePrefix + "%");
        return query.getResultList();
    }

    /**
     * Find users by partial email match.
     * This method allows searching users where the email starts with the provided prefix.
     * For example, searching with "john" would find users with emails like "john@example.com", "johnny@test.com", etc.
     *
     * @param emailPrefix the email prefix to search for (minimum 3 characters recommended)
     * @return list of users with matching email prefix
     */
    public List<User> findByEmailStartingWith(String emailPrefix) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email LIKE :emailPrefix ORDER BY u.email", User.class);
        query.setParameter("emailPrefix", emailPrefix + "%");
        return query.getResultList();
    }
} 
package hr.fer.progi.satcom.services;

import hr.fer.progi.satcom.models.User;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import hr.fer.progi.satcom.services.exceptions.RequestDeniedException;

import java.util.List;
import java.util.Optional;

/**
 * Manages user database.
 *
 * @see User
 * @author satcomBackend */
public interface UserService {

    /**
     * List all users in the system.
     * @return a list with all users*/
    List<User> listAll();

    /**
     * Fetches user with given ID.
     * @param userId given user ID
     * @return user associated with given ID in the system
     * @throws EntityMissingException if user with that ID is not found
     * @see UserService#findById(Long)
     */
    User fetch(Long userId);

    /**
     * Finds user with given ID, if exists.
     * @param userId given user ID
     * @return Optional with value of user associated with given ID in the system,
     * or no value if one does not exist
     * @see UserService#fetch
     */
    Optional<User> findById(Long userId);



    /**
     * Creates new user in the system.
     * @param user object to create, with ID set to null
     * @return created user object in the system with ID set
     * @throws IllegalArgumentException if given object is null, its ID is not null, has null username or email or roleName or password
     * @throws RequestDeniedException if user with that email or username already exists in the system or rolName is not valid
     * @see User
     */
    User createNewUser(User user);

    /**
     * Updates the user with that same ID.
     * @param user object to update, with ID set
     * @return updated user object in the system
     * @throws IllegalArgumentException if given object is null, has null ID or username or email or roleName or password
     * @throws EntityMissingException if user with given ID is not found
     * @throws RequestDeniedException if user with username already exists in the system
     */
    User updateUser(User user);


    /**
     * Deletes one user.
     * @param userId ID of user to delete from the system
     * @return deleted data
     * @throws EntityMissingException if user with that ID is not found
     */
    User deleteUser(Long userId);






}

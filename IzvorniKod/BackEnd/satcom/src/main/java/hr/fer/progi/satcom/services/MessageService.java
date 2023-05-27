package hr.fer.progi.satcom.services;

import hr.fer.progi.satcom.models.Message;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Manages message database.
 *
 * @see Message
 * @author satcomBackend*
 * */
public interface MessageService {

    /**
     * List all messages for a user whose id is userId.
     * @param userId id of a user whose messages we want to list.
     * @return a list with all messages
     * */
     Set<Message> listAll(Long userId);

    /**
     * Fetches message with given ID.
     * @param messageId given message ID
     * @return message associated with given ID in the system
     * @throws EntityMissingException if message with that ID is not found
     * @see MessageService#findById(Long)
     */
    Message fetch(Long messageId);

    /**
     * Finds message with given ID, if exists.
     * @param messageId given message ID
     * @return Optional with value of message associated with given ID in the system,
     * or no value if one does not exist
     * @see MessageService#fetch
     */
    Optional<Message> findById(Long messageId);

    /**
     * Deletes one message.
     * @param messageId ID of message to delete from the system
     * @throws EntityMissingException if message with that ID is not found
     */
    void deleteMessage(Long messageId);

    void deleteAllMessages(Long userId);
}

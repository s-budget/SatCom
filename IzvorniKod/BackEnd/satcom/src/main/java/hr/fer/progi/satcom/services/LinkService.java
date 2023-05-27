package hr.fer.progi.satcom.services;

import hr.fer.progi.satcom.models.Link;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;

import java.util.List;
import java.util.Optional;

/**
 * Manages link database.
 *
 * @see Link
 * @author satcomBackend
 * */
public interface LinkService {

    /**
     * List all links in the system.
     * @return a list with all */
    List<Link> listAll();

    /**
     * Fetches link with given ID.
     * @param linkId given link ID
     * @return link associated with given ID in the system
     * @throws EntityMissingException if link with that ID is not found
     * @see LinkService#findById(Long)
     */
    Link fetch(Long linkId);

    /**
     * Finds link with given ID, if exists.
     * @param linkId given link ID
     * @return Optional with value of link associated with given ID in the system,
     * or no value if one does not exist
     * @see LinkService#fetch
     */
    Optional<Link> findById(Long linkId);

    /**
     * Creates new link in the system.
     * @param link object to create, with ID set to null
     * @return created link object in the system with ID set
     * @throws IllegalArgumentException if given object is null, his ID is not null, has null linkMode or linkFreq or linkBaud
     * @see Link
     */
    Link createNewLink(Link link);

    /**
     * Updates the link with that same ID.
     * @param link object to update, with ID set
     * @return updated link object in the system
     * @throws IllegalArgumentException if given object is null, has null ID or  linkMode or linkFreq or linkBaud
     * @throws EntityMissingException if link with given ID is not found
     */
    Link updateLink(Link link);


    /**
     * Deletes one link.
     * @param linkId ID of link to delete from the system
     * @return deleted data
     * @throws EntityMissingException if link with that ID is not found
     */
    Link deleteLink(Long linkId);

}

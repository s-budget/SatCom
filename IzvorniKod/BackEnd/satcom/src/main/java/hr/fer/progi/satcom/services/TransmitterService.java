package hr.fer.progi.satcom.services;

import hr.fer.progi.satcom.models.Transmitter;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;

import java.util.List;
import java.util.Optional;

/**
 *
 * @see Transmitter
 */

public interface TransmitterService {

    /**
     * List all transmitters in the system.
     * @return a list with all transmitters*/
    List<Transmitter> listAll();

    /**
     * Fetches transmitter with given ID.
     * @param transmitterId given transmitter ID
     * @return transmitter associated with given ID in the system
     * @throws EntityMissingException if transmitter with that ID is not found
     * @see TransmitterService#findById(Long)
     */
    Transmitter fetch(Long transmitterId);

    /**
     * Finds transmitter with given ID, if exists.
     * @param transmitterId given transmitter ID
     * @return Optional with value of transmitter associated with given ID in the system,
     * or no value if one does not exist
     * @see TransmitterService#fetch
     */
    Optional<Transmitter> findById(Long transmitterId);

    /**
     * Creates new transmitter in the system.
     * @param transmitter object to create, with ID set to null
     * @return created transmitter object in the system with ID set
     * @throws IllegalArgumentException if given object is null, its ID is not null, has null transmName, transmMode, transmFreq or transmBaud
     * @see Transmitter
     */
    Transmitter createNewTransmitter(Transmitter transmitter);

    /**
     * Updates the transmitter with that ID.
     * @param transmitter object to update, with ID set
     * @return updated transmitter object in the system
     * @throws IllegalArgumentException if given object is null, has null ID or has null transmName, transmMode, transmFreq or transmBaud
     * @throws EntityMissingException if transmitter with given ID is not found
     */
    Transmitter updateTransmitter(Transmitter transmitter);

    /**
     * Deletes one transmitter.
     * @param transmitterId ID of transmitter to delete from the system
     * @return deleted data
     * @throws EntityMissingException if transmitter with that ID is not found
     */
    Transmitter deleteTransmitter(Long transmitterId);
}

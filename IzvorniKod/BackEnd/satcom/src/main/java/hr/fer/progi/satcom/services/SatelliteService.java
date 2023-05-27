package hr.fer.progi.satcom.services;

import hr.fer.progi.satcom.models.Satellite;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import hr.fer.progi.satcom.services.exceptions.RequestDeniedException;

import java.util.List;
import java.util.Optional;

public interface SatelliteService {

    /**
     * List all transmitters in the system.
     * @return a list with all transmitters*/
    List<Satellite> listAll();

    /**
     * Fetches satellite with given ID.
     * @param satelliteId given satellite ID
     * @return satellite associated with given ID in the system
     * @throws EntityMissingException if satellite with that ID is not found
     * @see SatelliteService#findById
     */
    Satellite fetch(Long satelliteId);

    /**
     * Finds satellite with given ID, if exists.
     * @param satelliteId given satellite ID
     * @return Optional with value of satellite associated with given ID in the system,
     * or no value if one does not exist
     * @see SatelliteService#fetch
     */
    Optional<Satellite> findById(Long satelliteId);

    /**
     * Creates new transmitter in the system.
     * @param satellite object to create, with ID set to null
     * @return created satellite object in the system with ID set
     * @throws IllegalArgumentException if given object is null, its ID is not null, has null satName, creationDate or createdBy.
     * @see Satellite
     */
    Satellite createNewSatellite(Satellite satellite);

    /**
     * Updates the satellite with that ID.
     * @param satellite object to update, with ID set
     * @return updated satellite object in the system
     * @throws IllegalArgumentException if given object is null, its ID is not null, has null satName, creationDate or createdBy.
     * @throws EntityMissingException if satellite with given ID is not found
     * @throws RequestDeniedException if satellite with that satName already exists
     */
    Satellite updateSatellite(Satellite satellite);

    /**
     * Deletes one satellite.
     * @param satelliteId ID of satellite to delete from the system
     * @return deleted data
     * @throws EntityMissingException if satellite with that ID is not found
     */
    Satellite deleteSatellite(Long satelliteId);

    List<String> listAllByName();

}

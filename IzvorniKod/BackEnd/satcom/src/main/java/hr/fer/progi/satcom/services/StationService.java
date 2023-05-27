package hr.fer.progi.satcom.services;

import hr.fer.progi.satcom.models.Station;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface StationService {

    /**
     * List all stations in the system.
     * @return a list with all stations*/
    List<Station> listAll();

    /**
     * Fetches station with given ID.
     * @param stationId given station ID
     * @return station associated with given ID in the system
     * @throws EntityMissingException if station with that ID is not found
     * @see StationService#findById
     */
    Station fetch(Long stationId);

    /**
     * Finds station with given ID, if exists.
     * @param stationId given station ID
     * @return Optional with value of station associated with given ID in the system,
     * or no value if one does not exist
     * @see StationService#fetch
     */
    Optional<Station> findById(Long stationId);

    /**
     * Creates new station in the system.
     * @param station object to create, with ID set to null
     * @return created station object in the system with ID set
     * @see Station
     */
    Station createNewStation(Station station);



}

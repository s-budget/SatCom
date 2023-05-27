package hr.fer.progi.satcom.services;


import hr.fer.progi.satcom.models.Antenna;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Manages antenna database.
 *
 * @see Antenna
 * @author satcomBackend*/
@Service
public interface AntennaService {

    /**
     * List all antennas in the system.
     * @return a list with all antennas*/
    List<Antenna> listAll();

    /**
     * Fetches antenna with given ID.
     * @param antennaId given antenna ID
     * @return antenna associated with given ID in the system
     * @throws EntityMissingException if antenna with that ID is not found
     * @see AntennaService#findById(Long)
     */
    Antenna fetch(Long antennaId);

    /**
     * Finds antenna with given ID, if exists.
     * @param antennaId given antenna ID
     * @return Optional with value of antenna associated with given ID in the system,
     * or no value if one does not exist
     * @see AntennaService#fetch
     */
    Optional<Antenna> findById(Long antennaId);

    /**
     * create new antenna
     *
     * @param antenna antenna
     * @return {@link Antenna}
     * @see Antenna
     */
    Antenna createNewAntenna(Antenna antenna);
}

package hr.fer.progi.satcom.dao;

import hr.fer.progi.satcom.models.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByLinkFreq(Long linkFreq);

    Optional<Link> findByLinkMode(String linkMode);
    @Query(value = "SELECT * from links where links.link_id = ?1 limit 1", nativeQuery = true)
    Optional<Link> findByLinkId(Long id);

    boolean existsByLinkFreq(Long linkFreq);

    boolean existsByLinkBaud(Integer linkBaud);

    boolean existsByLinkMode(String linkMode);

    @Query(value = "SELECT * from links where links.link_baud = ?1 and links.link_mode = ?2 and links.link_freq = ?3 limit 1", nativeQuery = true)
    Optional<Link> findPreExistingLink(Integer Baud, String Mode, Long Freq);


    @Query(value = "SELECT * from links where links.link_baud = ?1 and links.link_mode = ?2 and links.link_freq = ?3 limit 1", nativeQuery = true)
    Optional<Link> findCompatibleLinkForTransmitter(Integer transmBaud, String transmMode, Long transmFreq);

    @Query(value = "SELECT * from links where links.link_freq <= ?1 and links.link_freq >= ?2", nativeQuery = true)
    Optional<Set<Link>> findCompatibleLinksForAntenna(Long antennaFreqHigh, Long antennaFreqLow);

    @Modifying
    @Transactional
    @Query(value = "update transmitters set link_id = ?1 where transmitters.transm_mode = ?2 AND transmitters.transm_baud = ?3 AND transmitters.transm_freq = ?4", nativeQuery = true)
    void connectCompatibleTransmitters(Long linkId, String linkMode, Integer linkBaud, Long linkFreq);

    @Modifying
    @Transactional
    @Query(value = "update link_antenna set link_id = ?1, antenna_id = antennas.antenna_id from" +
            " antennas CROSS JOIN links where antennas.freq_low <= ?2 AND antennas.freq_high >= ?2", nativeQuery = true)
    void connectCompatibleAntennas(Long linkId, Long linkFreq);
}

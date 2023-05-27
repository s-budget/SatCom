package hr.fer.progi.satcom.dao;

import hr.fer.progi.satcom.models.Antenna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AntennaRepository extends JpaRepository<Antenna, Long> {
    
    Boolean existsByantennaId(long antennaId);
    @Query(value = "SELECT * from antennas where ?1 <= antennas.freq_high and ?1 >= antennas.freq_low", nativeQuery = true)
    Optional<Set<Antenna>> findCompatibleAntennasForLink(Long linkFreq);

    @Query(value = "SELECT stat_id from stat_antennas where ?1 = stat_antennas.antenna_id limit 1", nativeQuery = true)
    Optional<Long> findCompatibleStationIdForAntenna(Long antennaId);
    @Modifying
    @Query(value = "INSERT INTO antennas (antenna_id,freq_low,freq_high,type) VALUES (?1, '1','1','placeholder') ",nativeQuery = true)
    void forceSaveStation(Long antennaId);
}

package hr.fer.progi.satcom.dao;

import hr.fer.progi.satcom.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {


    Optional<Station> findByStatName(String Name);
    Boolean existsBystatId(long statId);

    @Modifying
    @Query(value = "INSERT INTO stations (stat_id,altitude,latitude,longitude,stat_name,success_rate) VALUES (?1,'1','1','1','placeholder','1') ",nativeQuery = true)
    void forceSaveStation(long id);



    @Query(value = "SELECT * from stations where stations.stat_id = ?1 limit 1", nativeQuery = true)
    Optional<Station> findByStationId(Long id);

}

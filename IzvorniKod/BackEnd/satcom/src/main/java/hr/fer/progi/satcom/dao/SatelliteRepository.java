package hr.fer.progi.satcom.dao;

import hr.fer.progi.satcom.models.Satellite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
---getAllSatellites X -- mozemo koristiti findAll
---getSatellite(po Id-u) X (povuci i listu linkova i stanica koji su vezani za taj satelit)
---createNewSatellite(pri kreiranju napraviti vezu s Linkom)
---deleteSatellite X
---editSatellite X
 */

@Repository
public interface SatelliteRepository extends JpaRepository<Satellite, Long> {

    boolean existsBySatName(String satName);
}

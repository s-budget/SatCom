package hr.fer.progi.satcom.services.impl;

import hr.fer.progi.satcom.dao.AntennaRepository;
import hr.fer.progi.satcom.dao.LinkRepository;
import hr.fer.progi.satcom.dao.StationRepository;
import hr.fer.progi.satcom.models.Antenna;
import hr.fer.progi.satcom.models.Link;
import hr.fer.progi.satcom.models.Satellite;
import hr.fer.progi.satcom.models.Station;
import hr.fer.progi.satcom.services.AntennaService;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AntennaServiceImpl implements AntennaService {

    @Autowired
    private AntennaRepository antennaRepo;

    @Autowired
    private LinkRepository linkRepo;

    @Autowired
    private StationRepository statRepo;

    @Override
    public List<Antenna> listAll() {
        return antennaRepo.findAll();
    }

    @Override
    public Antenna fetch(Long antennaId) {
        return findById(antennaId).orElseThrow(
                () -> new EntityMissingException(Antenna.class, antennaId)
        );
    }

    @Override
    public Optional<Antenna> findById(Long antennaId) {
        Assert.notNull(antennaId, "AntennaId must be given");
        return antennaRepo.findById(antennaId);
    }

    @Override
    public Antenna createNewAntenna(Antenna antenna) {
        return antennaRepo.save(antenna);

    }

    public Antenna deleteAntenna(Long antennaId) {
        Antenna antenna = fetch(antennaId);
        antennaRepo.delete(antenna);
        return antenna;
    }
    private void validate(Antenna antenna) {
        Assert.notNull(antenna, "Antenna object must be given");
    }

    public Set<Link> findCompatibleLinks(Antenna antenna) {
        return linkRepo.findCompatibleLinksForAntenna(antenna.getAntennaFreqHigh(), antenna.getAntennaFreqLow()).orElse(new HashSet<Link>());
    }

    public Station findCompatibleStation(Antenna antenna)
    {
        Long stat_id=antennaRepo.findCompatibleStationIdForAntenna(antenna.getAntennaId()).orElse(null);
        if(stat_id!=null)
        {
            return (statRepo.findByStationId(stat_id).orElse(null));
        }
        return null;
    }
    public Set<Station> findAllCompatibleStations(Set<Antenna> antennas) {
        Set<Station> allCompatibleStations = new HashSet<>();
        for(Antenna a : antennas)
        {
            if(findCompatibleStation(a)!=null)
            {
                allCompatibleStations.add(findCompatibleStation(a));
            }
        }
        return allCompatibleStations;
    }

    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public void forceSaveStation(Long id,long h,long l, String a) {
        entityManager.createNativeQuery("INSERT INTO antennas (antenna_id,freq_low,freq_high,type) VALUES (?,?,?,?)")
                .setParameter(1, id)
                .setParameter(2, l)
                .setParameter(3, h)
                .setParameter(4, a)
                .executeUpdate();
    }

}

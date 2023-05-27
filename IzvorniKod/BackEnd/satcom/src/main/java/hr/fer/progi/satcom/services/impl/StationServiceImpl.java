package hr.fer.progi.satcom.services.impl;

import hr.fer.progi.satcom.dao.StationRepository;
import hr.fer.progi.satcom.models.Station;
import hr.fer.progi.satcom.services.StationService;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Service
public class StationServiceImpl implements StationService {

    @Autowired
    private StationRepository statRepo;

    @Override
    public List<Station> listAll() {
        return statRepo.findAll();
    }

    @Override
    public Station fetch(Long stationId) {
        return findById(stationId).orElseThrow(
                () -> new EntityMissingException(Station.class, stationId)
        );
    }

    @Override
    public Optional<Station> findById(Long stationId) {
        Assert.notNull(stationId, "StationId must be given");
        return statRepo.findById(stationId);
    }

    public void delete(Station st) {
        statRepo.delete(st);
    }

    public Station deleteStation(Long stId) {
        Station s = fetch(stId);
        statRepo.delete(s);
        return s;
    }

    @Override
    public Station createNewStation(Station s) {
        return statRepo.save(s);
    }
    @PersistenceContext
    private EntityManager entityManager;
    @Transactional
    public void forceSaveStation(Long id,long a,double b,double c,String d,long e) {
        entityManager.createNativeQuery("INSERT INTO stations (stat_id,altitude,latitude,longitude,stat_name,success_rate) VALUES (?,?,?,?,?,?)")
                .setParameter(1, id)
                .setParameter(2, a)
                .setParameter(3, b)
                .setParameter(4, c)
                .setParameter(5, d)
                .setParameter(6, e)
                .executeUpdate();
    }
}

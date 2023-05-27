package hr.fer.progi.satcom.services.impl;

import hr.fer.progi.satcom.dao.SatelliteRepository;
import hr.fer.progi.satcom.models.Satellite;
import hr.fer.progi.satcom.services.SatelliteService;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import hr.fer.progi.satcom.services.exceptions.RequestDeniedException;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SatelliteServiceImpl implements SatelliteService {

    @Autowired
    private SatelliteRepository satRepo;

    @Override
    public List<Satellite> listAll() {
        return satRepo.findAll();
    }

    @Override
    public Satellite fetch(Long satelliteId) {
        return findById(satelliteId).orElseThrow(
                () -> new EntityMissingException(Satellite.class, satelliteId)
        );
    }

    @Override
    public Optional<Satellite> findById(Long satelliteId) {
        Assert.notNull(satelliteId, "SatelliteId must be given");
        return satRepo.findById(satelliteId);
    }

    @Override
    public Satellite createNewSatellite(Satellite satellite) {
        validate(satellite);
        Assert.isNull(satellite.getSatId(),
                "Satellite ID must be null, not: " + satellite.getSatId()
        );

        if(satRepo.existsBySatName(satellite.getSatName()))
            throw new RequestDeniedException("Name is already taken");

        return satRepo.save(satellite);
    }

    private void validate(Satellite satellite) {
        Assert.notNull(satellite, "Satellite object must be given");
        Assert.notNull(satellite.getSatName(), "Name must be given");
        Assert.notNull(satellite.getCreationDate(), "Date of creation must be given");
    }

    @Override
    public Satellite updateSatellite(Satellite satellite) {
        return satRepo.save(satellite);
    }

    public boolean existsBySatName(String satName) {
        return  satRepo.existsBySatName(satName);
    }

    @Override
    public Satellite deleteSatellite(Long satelliteId) {
        Satellite satellite = fetch(satelliteId);
        satRepo.delete(satellite);
        return satellite;
    }

    @Override
    public List<String> listAllByName() {
        return satRepo.findAll().stream().map(satellite -> satellite.getSatName()).collect(Collectors.toList());
    }
}

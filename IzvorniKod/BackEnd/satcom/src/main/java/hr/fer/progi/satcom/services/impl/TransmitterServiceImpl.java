package hr.fer.progi.satcom.services.impl;

import hr.fer.progi.satcom.dao.LinkRepository;
import hr.fer.progi.satcom.dao.TransmitterRepository;
import hr.fer.progi.satcom.models.Link;
import hr.fer.progi.satcom.models.Transmitter;
import hr.fer.progi.satcom.services.TransmitterService;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransmitterServiceImpl implements TransmitterService {

    @Autowired
    private TransmitterRepository transmRepo;

    @Autowired
    private LinkRepository linkRepo;

    @Override
    public List<Transmitter> listAll() {
        return transmRepo.findAll();
    }

    @Override
    public Transmitter fetch(Long transmitterId) {
        return findById(transmitterId).orElseThrow(
                () -> new EntityMissingException(Transmitter.class, transmitterId)
        );
    }

    @Override
    public Optional<Transmitter> findById(Long transmitterId) {
        Assert.notNull(transmitterId, "TransmitterId must be given");
        return transmRepo.findById(transmitterId);
    }

    @Override
    public Transmitter createNewTransmitter(Transmitter transmitter) {
        validate(transmitter);
        Assert.isNull(transmitter.getTransmId(),
                "Transmitter ID must be null, not: " + transmitter.getTransmId()
        );

        return transmRepo.save(transmitter);
    }

    private void validate(Transmitter transmitter) {
        Assert.notNull(transmitter, "Transmitter object must be given");
        Assert.notNull(transmitter.getTransmName(), "Name must be given");
        Assert.notNull(transmitter.getTransmFreq(), "Frequency must be given");
        Assert.notNull(transmitter.getTransmMode(), "Mode must be given");
        Assert.notNull(transmitter.getTransmBaud(), "Baud must be given");
        Assert.notNull(transmitter.getOwningSatelliteObj(), "Satellite must be given");
    }

    @Override
    public Transmitter updateTransmitter(Transmitter transmitter) {
        validate(transmitter);
        Long transmitterId = transmitter.getTransmId();
        if (!transmRepo.existsById(transmitterId))
            throw new EntityMissingException(Transmitter.class, transmitterId);

        return transmRepo.save(transmitter);
    }

    @Override
    public Transmitter deleteTransmitter(Long transmitterId) {
        Transmitter transmitter = fetch(transmitterId);
        transmRepo.delete(transmitter);
        return transmitter;
    }


    public Link findCompatibleLink(Transmitter transmitter) {
        return linkRepo.findCompatibleLinkForTransmitter(transmitter.getTransmBaud(), transmitter.getTransmMode(), transmitter.getTransmFreq()).orElse(null);
    }

    public Set<Link> findAllCompatibleLinks(Set<Transmitter> transmitters) {
        Set<Link> allCompatibleLinks = new HashSet<>();
        for(Transmitter a : transmitters)
        {
            if(findCompatibleLink(a)!=null)
            {
                allCompatibleLinks.add(findCompatibleLink(a));
            }
        }
        return  allCompatibleLinks;
    }
}

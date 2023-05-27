package hr.fer.progi.satcom.services.impl;

import hr.fer.progi.satcom.dao.AntennaRepository;
import hr.fer.progi.satcom.dao.LinkRepository;
import hr.fer.progi.satcom.models.Antenna;
import hr.fer.progi.satcom.models.Link;
import hr.fer.progi.satcom.services.LinkService;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private LinkRepository linkRepo;

    @Autowired
    private AntennaRepository antennaRepo;

    @Override
    public List<Link> listAll() {
        return linkRepo.findAll();
    }

    @Override
    public Link fetch(Long linkId) {
        return findById(linkId).orElseThrow(
                () -> new EntityMissingException(Link.class, linkId)
        );
    }

    @Override
    public Optional<Link> findById(Long linkId) {
        Assert.notNull(linkId, "LinkId must be given");
        return linkRepo.findById(linkId);
    }

    @Override
    public Link createNewLink(Link link) {
        validate(link);
        Assert.isNull(link.getLinkId(),
                "Link ID must be null, not: " + link.getLinkId()
        );

        Link saved = linkRepo.save(link);

        linkRepo.connectCompatibleTransmitters(saved.getLinkId(), saved.getLinkMode(), saved.getLinkBaud(), saved.getLinkFreq());
        //linkRepo.connectCompatibleAntennas(saved.getLinkId(), saved.getLinkFreq());
        if(antennaRepo.findCompatibleAntennasForLink(link.getLinkFreq()).isPresent())
        {
            Set<Antenna> compatible=antennaRepo.findCompatibleAntennasForLink(link.getLinkFreq()).get();
            for(Antenna a : compatible)
            {
                Set<Link> current =a.getLinkAntennaObj();
                current.add(link);
                a.setLinkAntenna(current);
                antennaRepo.save(a);

            }
            link.setAntennas(compatible);
            //linkRepo.save(link);
        }
            return saved;
    }

    private void validate(Link link) {
        Assert.notNull(link, "Link object must be given");
        Assert.notNull(link.getLinkFreq(), "Frequency must be given");
        Assert.notNull(link.getLinkMode(), "Mode must be given");
        Assert.notNull(link.getLinkBaud(), "Baud must be given");
    }

    @Override
    public Link updateLink(Link link) {
        if(linkRepo.findByLinkId(link.getLinkId()).isPresent() && antennaRepo.findCompatibleAntennasForLink(linkRepo.findByLinkId(link.getLinkId()).get().getLinkFreq()).isPresent())
        {
            Set<Antenna> compatible=antennaRepo.findCompatibleAntennasForLink(linkRepo.findByLinkId(link.getLinkId()).get().getLinkFreq()).get();
            for(Antenna a : compatible)
            {
                Set<Link> current =a.getLinkAntennaObj();
                current.remove(linkRepo.findByLinkId(link.getLinkId()).get());
                a.setLinkAntenna(current);
                antennaRepo.save(a);
            }
        }

        Link saved = linkRepo.save(link);

        linkRepo.connectCompatibleTransmitters(saved.getLinkId(), saved.getLinkMode(), saved.getLinkBaud(), saved.getLinkFreq());
        //linkRepo.connectCompatibleAntennas(saved.getLinkId(), saved.getLinkFreq());
        if(antennaRepo.findCompatibleAntennasForLink(link.getLinkFreq()).isPresent())
        {
            Set<Antenna> compatible=antennaRepo.findCompatibleAntennasForLink(link.getLinkFreq()).get();
            for(Antenna a : compatible)
            {
                Set<Link> current =a.getLinkAntennaObj();
                current.add(link);
                a.setLinkAntenna(current);
                antennaRepo.save(a);
            }
            link.setAntennas(compatible);
            linkRepo.save(link);
        }
        return saved;
    }

    @Override
    public Link deleteLink(Long linkId) {
        Link link = fetch(linkId);
        if(antennaRepo.findCompatibleAntennasForLink(link.getLinkFreq()).isPresent())
        {
            Set<Antenna> compatible=antennaRepo.findCompatibleAntennasForLink(link.getLinkFreq()).get();
            for(Antenna a : compatible)
            {
                Set<Link> current =a.getLinkAntennaObj();
                current.remove(link);
                a.setLinkAntenna(current);
                antennaRepo.save(a);
            }
        }

        linkRepo.delete(link);
        return link;
    }

    public boolean existsByAllParams(String mode, Long freq, Integer baud) {

        return linkRepo.findPreExistingLink(baud,mode,freq).isPresent();
    }

}

package hr.fer.progi.satcom.controllers;

import hr.fer.progi.satcom.dao.AntennaRepository;
import hr.fer.progi.satcom.models.Antenna;
import hr.fer.progi.satcom.models.Link;
import hr.fer.progi.satcom.models.Station;
import hr.fer.progi.satcom.services.exceptions.RequestDeniedException;
import hr.fer.progi.satcom.services.impl.AntennaServiceImpl;
import hr.fer.progi.satcom.services.impl.LinkServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/links")
public class LinkController {

    @Autowired
    private LinkServiceImpl linkService;

    @Autowired
    private AntennaServiceImpl antennaService;

    @Autowired
    private AntennaRepository antennaRepo;

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public List<Link> listLinks() {
        return linkService.listAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Link getLink(@PathVariable("id") Long id) {
        return linkService.fetch(id);
    }

    @GetMapping("/{id}/stations")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Set<Station> getLinkStations(@PathVariable("id") Long id) {
        return antennaService.findAllCompatibleStations(linkService.fetch(id).getAntennasObj());
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SATELLITE_ADMIN')")
    public ResponseEntity<Link> createLink(@RequestBody Link link) {
        if(linkService.existsByAllParams(link.getLinkMode(), link.getLinkFreq(), link.getLinkBaud()))
            throw new RequestDeniedException("Link with given parameters already exists");
        Link saved = linkService.createNewLink(link);
        saved.setTransmitter(new HashSet<>());
        //saved.setAntennas(new HashSet<>());

        return ResponseEntity.created(URI.create("/links/" + saved.getLinkId())).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SATELLITE_ADMIN')")
    public Link updateLink(@PathVariable("id") Long id, @RequestBody Link link) {
        Link newLink = linkService.fetch(id);

        if(link.getLinkMode() != null) {
            newLink.setLinkMode(link.getLinkMode());
        }

        if(link.getLinkFreq() != null) {
            newLink.setLinkFreq(link.getLinkFreq());
        }

        if(link.getLinkBaud() != null) {
            newLink.setLinkBaud(link.getLinkBaud());
        }
        //&& (!newLink.getLinkMode().equals(link.getLinkMode()) && !newLink.getLinkFreq().equals(link.getLinkFreq()) && !newLink.getLinkBaud().equals(link.getLinkBaud()))
        if(linkService.existsByAllParams(newLink.getLinkMode(), newLink.getLinkFreq(), newLink.getLinkBaud()))
            throw new RequestDeniedException("Link with given parameters already exists");

        return linkService.updateLink(newLink);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SATELLITE_ADMIN')")
    public Link deleteLink(@PathVariable("id") Long linkId) {
        return linkService.deleteLink(linkId);
    }


}

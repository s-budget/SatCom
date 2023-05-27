package hr.fer.progi.satcom.controllers;

import hr.fer.progi.satcom.models.Antenna;
import hr.fer.progi.satcom.services.impl.AntennaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/antennas")
public class AntennaController {

    @Autowired
    private AntennaServiceImpl antennaService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public List<Antenna> listAntennas() {
        return antennaService.listAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Antenna getAntenna(@PathVariable("id") Long id) {
        return antennaService.fetch(id);
    }

    public ResponseEntity<Antenna> createAntenna(@RequestBody Antenna antenna) {
        antenna.setLinkAntenna(antennaService.findCompatibleLinks(antenna));
        Antenna saved = antennaService.createNewAntenna(antenna);
        return ResponseEntity.created(URI.create("/antennas/" + saved.getAntennaId())).body(saved);
    }
}

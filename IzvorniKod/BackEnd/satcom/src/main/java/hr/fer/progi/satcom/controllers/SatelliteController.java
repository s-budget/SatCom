package hr.fer.progi.satcom.controllers;

import hr.fer.progi.satcom.models.Link;
import hr.fer.progi.satcom.models.Satellite;
import hr.fer.progi.satcom.models.Transmitter;
import hr.fer.progi.satcom.security.user_security_context.SecurityUserDetails;
import hr.fer.progi.satcom.services.exceptions.RequestDeniedException;
import hr.fer.progi.satcom.services.impl.SatelliteServiceImpl;
import hr.fer.progi.satcom.services.impl.TransmitterServiceImpl;
import hr.fer.progi.satcom.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/satellites")
public class SatelliteController {

    @Autowired
    private SatelliteServiceImpl satelliteService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TransmitterServiceImpl transmitterService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public List<Satellite> listSatellites() {
        return satelliteService.listAll();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public List<String> listSatellitesByName() {
        return satelliteService.listAllByName();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Satellite getSatellite(@PathVariable("id") Long id) {
        return satelliteService.fetch(id);
    }

    @GetMapping("/{id}/transmitters")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Set<Transmitter> getSatelliteTransmitters(@PathVariable("id") Long id) {
        return satelliteService.fetch(id).getTransmittersObj();
    }
    @GetMapping("/{id}/links")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Set<Link> getSatelliteLinks(@PathVariable("id") Long id) {
        return transmitterService.findAllCompatibleLinks(satelliteService.fetch(id).getTransmittersObj());
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SATELLITE_ADMIN')")
    public ResponseEntity<Satellite> createSatellite(@RequestBody Satellite satellite) {
        satellite.setCreationDate(new Date());
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        satellite.setCreatedBy(userService.fetch(userDetails.getId()));
        Satellite saved = satelliteService.createNewSatellite(satellite);
        saved.setTransmitters(new HashSet<>());
        return ResponseEntity.created(URI.create("/satellites/" + saved.getSatId())).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SATELLITE_ADMIN')")
    public Satellite updateSatellite(@PathVariable("id") Long id, @RequestBody Satellite satellite) {
        Satellite updateSat = satelliteService.fetch(id);
        if(satellite.getSatName() != null) {
            if(satelliteService.existsBySatName(satellite.getSatName()) && !updateSat.getSatName().equals(satellite.getSatName()))
                throw new RequestDeniedException("Username is already taken");
            updateSat.setSatName(satellite.getSatName());
        }
        return satelliteService.updateSatellite(updateSat);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SATELLITE_ADMIN')")
    public Satellite deleteSatellite(@PathVariable("id") Long satId) {
        return satelliteService.deleteSatellite(satId);
    }
}

package hr.fer.progi.satcom.controllers;

import hr.fer.progi.satcom.models.Transmitter;
import hr.fer.progi.satcom.services.exceptions.RequestDeniedException;
import hr.fer.progi.satcom.services.impl.SatelliteServiceImpl;
import hr.fer.progi.satcom.services.impl.TransmitterServiceImpl;
import hr.fer.progi.satcom.utils.Mode;
import hr.fer.progi.satcom.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/transmitters")
public class TransmitterController {

    @Autowired
    private TransmitterServiceImpl transmitterService;

    @Autowired
    private SatelliteServiceImpl satelliteService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public List<Transmitter> listTransmitters() {
        return transmitterService.listAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Transmitter getTransmitter(@PathVariable("id") Long id) {
        return transmitterService.fetch(id);
    }

    @GetMapping("/forSat/{satId}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Set<Transmitter> getTransmittersForSatellite(@PathVariable Long satId) {
        return satelliteService.fetch(satId).getTransmittersObj();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SATELLITE_ADMIN')")
    public ResponseEntity<Transmitter> createTransmitter(@RequestBody Transmitter transmitter) {

        if(Arrays.stream(Mode.values()).noneMatch(mode -> mode.toString().equals(transmitter.getTransmMode())))
            throw new RequestDeniedException("No such mode defined");

        transmitter.setOwningSatellite(satelliteService.fetch(transmitter.getOwningSatelliteObj().getSatId()));
        transmitter.setLink(transmitterService.findCompatibleLink(transmitter));
        Transmitter saved = transmitterService.createNewTransmitter(transmitter);
        return ResponseEntity.created(URI.create("/transmitters/" + saved.getTransmId())).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SATELLITE_ADMIN')")
    public Transmitter updateTransmitter(@PathVariable("id") Long id, @RequestBody Transmitter transmitter) {

        if(Arrays.stream(Mode.values()).noneMatch(mode -> mode.toString().equals(transmitter.getTransmMode())))
            throw new IllegalArgumentException("No such mode defined");

        Transmitter newTransm = transmitterService.fetch(id);

        if(transmitter.getTransmName() != null) {
            newTransm.setTransmName(transmitter.getTransmName());
        }

        if(transmitter.getTransmMode() != null) {
            newTransm.setTransmMode(transmitter.getTransmMode());
        }

        if(transmitter.getTransmBaud() != null) {
            newTransm.setTransmBaud(transmitter.getTransmBaud());
        }

        if(transmitter.getTransmFreq() != null) {
            newTransm.setTransmFreq(transmitter.getTransmFreq());
        }

        newTransm.setLink(transmitterService.findCompatibleLink(newTransm));

        return transmitterService.updateTransmitter(newTransm);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SATELLITE_ADMIN')")
    public Transmitter deleteTransmitter(@PathVariable("id") Long transmId) {
        return transmitterService.deleteTransmitter(transmId);
    }

}

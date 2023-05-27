package hr.fer.progi.satcom.controllers;

import hr.fer.progi.satcom.models.Station;
import hr.fer.progi.satcom.pullingFromApi.ScheduledTask;
import hr.fer.progi.satcom.pullingFromApi.StationChecker;
import hr.fer.progi.satcom.services.impl.StationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/stations")
public class StationController {


    @Autowired
    private StationServiceImpl stationService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public List<Station> listStations() {
        return stationService.listAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Station getStation(@PathVariable("id") Long id) {
        return stationService.fetch(id);
    }

    @GetMapping("/{id}/isPresent")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public boolean checkStation(@PathVariable("id") Long id) {
        boolean result = StationChecker.checkStations(id);

        if(!result) {
            try {
                new ScheduledTask().refreshStations();
            } catch (IOException e) {
                return true;
            }
        }

        return result;
    }

}

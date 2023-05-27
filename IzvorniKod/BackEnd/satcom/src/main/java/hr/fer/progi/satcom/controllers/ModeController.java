package hr.fer.progi.satcom.controllers;

import hr.fer.progi.satcom.utils.Mode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/modes")
public class ModeController {

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public List<String> exportAllModes() {
        return Stream.of(Mode.values()).map(Mode::name).collect(Collectors.toList());
    }
}

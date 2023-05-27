package hr.fer.progi.satcom.controllers;

import hr.fer.progi.satcom.models.User;
import hr.fer.progi.satcom.security.jwt.JwtUtilization;
import hr.fer.progi.satcom.security.user_security_context.SecurityUserDetails;
import hr.fer.progi.satcom.services.exceptions.RequestDeniedException;
import hr.fer.progi.satcom.services.impl.UserServiceImpl;
import hr.fer.progi.satcom.utils.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtilization jwtTokenUtilization;

    @GetMapping("")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public List<User> listUsers() {
        return userService.listAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public User getUser(@PathVariable("id") Long id) {
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(Role.valueOf(userDetails.getRole()) == Role.SUPER_ADMIN || Objects.equals(userDetails.getId(), id)) {
            return userService.fetch(id);
        }
        else {
            throw new RequestDeniedException("SUPER_ADMIN role is required to look at other user profiles.");
        }

    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = new User(user.getUsername().trim(), user.getEmail().trim(), passwordEncoder.encode(user.getPassword().trim()), user.getRole().trim().toUpperCase());
        User saved = userService.createNewUser(newUser);
        return ResponseEntity.created(URI.create("/users/" + saved.getUserId())).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN') or hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN')")
    public User updateUser(@PathVariable("id") Long id, @RequestBody @NotNull User user) {
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User updateUser = userService.fetch(userDetails.getId());

        if(!updateUser.getUserId().equals(id) || (!Role.valueOf(userDetails.getRole()).equals(Role.SUPER_ADMIN) && !userDetails.getId().equals(id))) {
            throw new IllegalArgumentException("User ID must be preserved");
        }

        if(user.getUsername() != null) {
            if(userService.existsByUsername(user.getUsername()) && !updateUser.getUsername().equals(user.getUsername()))
                throw new RequestDeniedException("Username is already taken");
            updateUser.setUsername(user.getUsername().trim());
        }

        if(user.getPassword() != null) {
            updateUser.setPassword(passwordEncoder.encode(user.getPassword().trim()));
        }
        
        return userService.updateUser(updateUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public User deleteUser(@PathVariable("id") Long userId) {
        return userService.deleteUser(userId);
    }
}

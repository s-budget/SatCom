package hr.fer.progi.satcom.controllers;

import hr.fer.progi.satcom.dao.UserRepository;
import hr.fer.progi.satcom.models.User;
import hr.fer.progi.satcom.security.jwt.JwtUtilization;
import hr.fer.progi.satcom.security.payload.request.LoginRequest;
import hr.fer.progi.satcom.security.payload.request.SignupRequest;
import hr.fer.progi.satcom.security.payload.response.JwtResponse;
import hr.fer.progi.satcom.security.user_security_context.SecurityUserDetails;
import hr.fer.progi.satcom.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Authentication controller.
 * Takes incoming login and signup requests.
 * Returns JWT upon valid login request and user info upon valid sign up request
 * @see User
 * @author satcomBackend
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
    UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtUtilization jwtUtilization;

	@Autowired
	UserServiceImpl userService;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtilization.generateJwtToken(authentication);

		SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
		Optional<? extends GrantedAuthority> role = userDetails.getAuthorities().stream().findFirst();

		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getUsername(),
				userDetails.getEmail(),
				role.get().toString()));
	}

	@PostMapping("/signup")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		User user = new User(signUpRequest.getUsername().trim(),
				signUpRequest.getEmail().trim(),
				passwordEncoder.encode(signUpRequest.getPassword().trim()),
				signUpRequest.getRole().trim());
		userService.createNewUser(user);
		return ResponseEntity.ok(user);
	}
}

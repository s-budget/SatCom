package hr.fer.progi.satcom.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Test controller.
 *
 * @author satcomBackend
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/test")
public class
TestController {
	/**
	 * All access string.
	 *
	 * @return the string
	 */
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content route.";
	}

	/**
	 * User access string.
	 *
	 * @return the string
	 */
	@GetMapping("/user")
	@PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
	public String userAccess() {
		return "User Content.";
	}

	/**
	 * Moderator access string.
	 *
	 * @return the string
	 */
	@GetMapping("/satAdmin")
	@PreAuthorize("hasAuthority('SATELLITE_ADMIN')")
	public String moderatorAccess() {
		return "Satellite admin Board.";
	}

	/**
	 * Admin access string.
	 *
	 * @return the string
	 */
	@GetMapping("/supAdmin")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public String adminAccess() {
		return "Super admin Board.";
	}
}

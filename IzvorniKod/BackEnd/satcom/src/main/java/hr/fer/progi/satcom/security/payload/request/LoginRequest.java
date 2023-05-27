package hr.fer.progi.satcom.security.payload.request;

import javax.validation.constraints.NotBlank;

/**
 * Incoming login request.
 * Takes username and password from client login request.
 * @see hr.fer.progi.satcom.controllers.AuthController
 * @author satcomBackend
 */
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}

package hr.fer.progi.satcom.security.payload.response;

/**
 * The type Jwt response.
 *
 * @author satcomBackend
 */
public class JwtResponse {

	private Long id;
	private String username;
	private String email;
	private String role;
	private String token;
	private String type = "Bearer";

	/**
	 * Instantiates a new Jwt response.
	 *
	 * @param accessToken the access token
	 * @param id          the id
	 * @param username    the username
	 * @param email       the email
	 * @param role        the role
	 */
	public JwtResponse(String accessToken, Long id, String username, String email, String role) {
		this.token = accessToken;
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
	}

	/**
	 * Gets token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets token.
	 *
	 * @param token the token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Gets type.
	 *
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets type.
	 *
	 * @param type the type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Gets id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets id.
	 *
	 * @param id the id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets username.
	 *
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets username.
	 *
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets email.
	 *
	 * @param email the email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets role.
	 *
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets role.
	 *
	 * @param role the role
	 */
	public void setRole(String role) {
		this.role = role;
	}
}

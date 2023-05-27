package hr.fer.progi.satcom.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.Set;

/**
 * The type User.
 * @author satcomBackend
 */
@Entity
@Table(	name = "users", 
		uniqueConstraints = { 
			@UniqueConstraint(columnNames = "username"),
			@UniqueConstraint(columnNames = "email") 
		})
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@NotBlank
	@Size(max = 20)
	private String username;

	@OneToMany(mappedBy="userMessage", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Set<Message> logData;

	@OneToMany(mappedBy = "createdBy", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
	private Set<Satellite> satellites;


	@NotBlank
	@Size(max = 50)
	@Email
	private String email;

	@NotBlank
	@Size(max = 120)
	@JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	@NotBlank
	private String role;

	/**
	 * Instantiates a new User.
	 */
	public User() {
	}

	/**
	 * Instantiates a new User.
	 * @param username the username
	 * @param email    the email
	 * @param password the password
	 * @param role     the role
	 */
	public User(String username, String email, String password, String role) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	/**
	 * Gets id.
	 * @return the id
	 */
	public Long getUserId() {
		return userId;
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
	 * @param username the username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets email.
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets email.
	 * @param email the email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Gets password.
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets password.
	 * @param password the password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Gets role.
	 * @return the role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Sets role.
	 * @param role the role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Returns user message history.
	 * @see Message
	 */
	public Set<Message> getLogData() {
		return logData;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(userId, user.userId) && Objects.equals(username, user.username) && Objects.equals(email, user.email);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, username, email);
	}
}

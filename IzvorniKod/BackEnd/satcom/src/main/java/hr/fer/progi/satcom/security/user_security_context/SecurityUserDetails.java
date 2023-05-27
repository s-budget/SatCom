package hr.fer.progi.satcom.security.user_security_context;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hr.fer.progi.satcom.models.User;
import hr.fer.progi.satcom.utils.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class SecurityUserDetails implements UserDetails {

	private Long id;

	private String username;

	private String email;

	private Role role;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public SecurityUserDetails(Long id, String username, String email, String password, String role,
							   Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = Role.valueOf(role);
		this.authorities = authorities;
	}

	public static SecurityUserDetails buildUser(User user) {

		List<GrantedAuthority> authorities = new ArrayList<>();

		authorities.add(new SimpleGrantedAuthority(user.getRole()));

		return new SecurityUserDetails(
				user.getUserId(),
				user.getUsername(),
				user.getEmail(),
				user.getPassword(),
				user.getRole(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public String getRole() {
		return role.name();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		SecurityUserDetails user = (SecurityUserDetails) o;
		return Objects.equals(id, user.id);
	}

}

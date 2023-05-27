package hr.fer.progi.satcom.security.jwt;

import hr.fer.progi.satcom.security.user_security_context.SecurityUserDetails;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Class for JWT utilization.
 * Used to create and validate JWT tokens.
 * Throws exception upon failed validation.
 * @see JwtAuthenticationFilter
 */
@Component
public class JwtUtilization {

  @Value("${app.jwt.secret}")
  private String JWT_SECRET;

  @Value("${app.jwt.expiration}")
  private int JWT_EXPIRATION;

  public String generateJwtToken(Authentication authentication) {

    SecurityUserDetails userPrincipal = (SecurityUserDetails) authentication.getPrincipal();

    return Jwts.builder().setSubject((userPrincipal.getUsername()))
            .setIssuedAt(new Date())
            .setIssuer("SATcom")
            .setExpiration(new Date((new Date()).getTime() + JWT_EXPIRATION))
            .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
            .claim("role", userPrincipal.getAuthorities().stream().findFirst().get().getAuthority())
            .compact();
  }

  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
  }

  public boolean validateJwtToken(String token) {
    try {
      Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
      return true;
    } catch (SignatureException e) {
      throw new SignatureException("Invalid JWT signature");
    } catch (MalformedJwtException e) {
      throw new MalformedJwtException("Invalid JWT token");
    } catch (ExpiredJwtException e) {
      throw new ExpiredJwtException(e.getHeader(), e.getClaims(), "JWT token is expired");
    } catch (UnsupportedJwtException e) {
      throw new UnsupportedJwtException("JWT token is unsupported");
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("JWT claims string is empty");
    }
  }
}

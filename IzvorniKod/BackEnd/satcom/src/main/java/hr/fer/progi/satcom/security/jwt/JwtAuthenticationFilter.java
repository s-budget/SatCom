package hr.fer.progi.satcom.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import hr.fer.progi.satcom.security.payload.response.ErrorResponse;
import hr.fer.progi.satcom.security.user_security_context.SecurityUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The type Jwt authentication filter.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtilization jwtTokenUtilization;

  @Autowired
  private SecurityUserDetailsService userDetailsService;

  /**
   * Filters incoming requests.
   * Allows all requests with validated JWT tokens.
   * @see JwtUtilization
   * Throws exception and sends error message to client.
   * @see JwtAuthenticationEntryPoint
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtTokenUtilization.validateJwtToken(jwt)) {
        String username = jwtTokenUtilization.getUserNameFromJwtToken(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
            userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

      final Map<String, Object> body = new HashMap<>();
      body.put("error", new ErrorResponse(e.getMessage()).getError());
      final ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(response.getOutputStream(), body);
    }

    filterChain.doFilter(request, response);
  }

  /**
   * Returns parsed token from request header.
   */
  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }
}

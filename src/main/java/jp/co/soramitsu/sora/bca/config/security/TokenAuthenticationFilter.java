package jp.co.soramitsu.sora.bca.config.security;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jp.co.soramitsu.sora.bca.utils.TokenExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * The TokenAuthenticationFilter is responsible of extracting the authentication token from the
 * request headers. It takes the Authorization header value and attempts to extract the token from
 * it.
 *
 * After that, {@link Authentication} is passed to registered {@link
 * org.springframework.security.authentication.AuthenticationManager}
 */
@Slf4j
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

  public TokenAuthenticationFilter(RequestMatcher requiresAuth) {
    super(requiresAuth);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult
  ) throws IOException, ServletException {
    super.successfulAuthentication(request, response, chain, authResult);
    chain.doFilter(request, response);
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws AuthenticationException {
    String token = TokenExtractor.extractToken(request);
    Authentication auth = new UsernamePasswordAuthenticationToken(token, token);
    return getAuthenticationManager().authenticate(auth);
  }
}

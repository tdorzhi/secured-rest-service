package jp.co.soramitsu.sora.bca.config.security;

import static java.util.Optional.ofNullable;

import jp.co.soramitsu.sora.bca.domain.entities.User;
import jp.co.soramitsu.sora.bca.exceptions.NoSuchTokenException;
import jp.co.soramitsu.sora.bca.services.UserAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *  Only responsibility is to authenticate user by given token
 * */
@Service
public class TokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

  private UserAuthService<User> userDetailsService;

  @Autowired
  public TokenAuthenticationProvider(UserAuthService<User> userAuthService) {
    this.userDetailsService = userAuthService;
  }

  @Override
  protected void additionalAuthenticationChecks(UserDetails userDetails,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
  }

  @Override
  protected UserDetails retrieveUser(String username,
      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    return ofNullable(authentication.getCredentials())
        .map(Object::toString)
        .flatMap(userDetailsService::findByToken)
        .orElseThrow(
            () -> new NoSuchTokenException(authentication.getCredentials().toString())
        );
  }
}

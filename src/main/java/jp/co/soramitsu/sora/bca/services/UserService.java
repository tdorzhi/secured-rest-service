package jp.co.soramitsu.sora.bca.services;

import java.util.Optional;
import jp.co.soramitsu.sora.bca.exceptions.NoSuchUserException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService<T extends UserDetails> extends UserDetailsService {

  /**
   * Log in with given username and password
   *
   * @param username username
   * @param password hashed password
   * @return String short living JWT token
   * */
  String login(String username, String password) throws NoSuchUserException;

  /**
   * Find user by given token
   *
   * @param token token returned by {@link #login(String, String)}
   * @return Optional user
   * */
  Optional<T> findByToken(String token);

  /**
   * Log out given user from token, basically invalidate token
   *
   * @param token bound to user
   * */
  void logout(String token);
}

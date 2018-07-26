package jp.co.soramitsu.sora.bca.services;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.MINUTES;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultHeader;
import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.Optional;
import jp.co.soramitsu.sora.bca.domain.entities.User;
import jp.co.soramitsu.sora.bca.domain.repositories.UserRepository;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceWithCacheAndRepository implements UserService<User> {

  private Cache<String, UserDetails> userDetailsCache = CacheBuilder
      .newBuilder()
      .initialCapacity(1000)
      .expireAfterWrite(5, MINUTES)
      .build();

  private PasswordEncoder passwordEncoder;
  private UserRepository userRepository;

  public UserServiceWithCacheAndRepository(PasswordEncoder passwordEncoder,
      UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public String login(String username, String password) {
    requireNonNull(username, "Username should not be null");
    requireNonNull(password, "Password should not be null");
    val optionalUser = userRepository.findByUsername(username);
    if (optionalUser.isPresent()) {
      if (passwordEncoder.matches(password, optionalUser.get().getPassword())) {
        Jwts.builder().setExpiration(ZonedDateTime.now(Clock.systemUTC().instant().plus(30, SECONDS)).toInstant().)
      }
    }
  }

  @Override
  public Optional<User> findByToken(String token) {
    return Optional.empty();
  }

  @Override
  public void logout(User user) {

  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return null;
  }
}

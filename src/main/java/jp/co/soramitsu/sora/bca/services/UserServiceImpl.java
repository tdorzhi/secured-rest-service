package jp.co.soramitsu.sora.bca.services;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.isEmpty;

import java.util.Optional;
import jp.co.soramitsu.sora.bca.domain.entities.User;
import jp.co.soramitsu.sora.bca.domain.repositories.UserRepository;
import jp.co.soramitsu.sora.bca.exceptions.IncorrectCredentialsException;
import jp.co.soramitsu.sora.bca.exceptions.NoSuchUserException;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService<User> {

  private PasswordEncoder passwordEncoder;
  private UserRepository userRepository;
  private TokenService tokenService;

  public UserServiceImpl(
      PasswordEncoder passwordEncoder,
      UserRepository userRepository,
      TokenService tokenService
  ) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.tokenService = tokenService;
  }

  @Override
  public String login(String username, String password) throws NoSuchUserException {
    throwIfUsernameOrPasswordEmpty(username, password);

    String subject = username.trim();
    val passFromRepo = userRepository.findByUsername(subject);
    if (passFromRepo.isPresent()) {
      if (passwordEncoder.matches(password, passFromRepo.get().getPassword())) {
        return tokenService.generateToken(subject);
      } else {
        throw new IncorrectCredentialsException(username);
      }
    } else {
      throw new NoSuchUserException(username);
    }
  }

  @Override
  public Optional<User> findByToken(String token) {
    throwIfNotNull(token);
    String subject = tokenService.extractSubject(token);
    val userFromRepo = userRepository.findByUsername(subject);
    if (userFromRepo.isPresent()) {
      return userFromRepo;
    } else {
      throw new IllegalStateException("Token is valid, but user was not found");
    }
  }

  @Override
  public void logout(String token) {
    throwIfNotNull(token);
    tokenService.blacklist(token);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    throwIfNotNull(username);
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new NoSuchUserException(username));
  }

  private void throwIfUsernameOrPasswordEmpty(String username, String password) {
    if (isEmpty(username) || isEmpty(password)) {
      log.warn("Passed empty values: username: {}, password: {}", username, password);
      throw new IllegalArgumentException("Username or password is null or 0 sized");
    }
  }

  private void throwIfNotNull(Object object) {
    if (isNull(object)) {
      throw new IllegalArgumentException("Null is not permitted");
    }
  }
}

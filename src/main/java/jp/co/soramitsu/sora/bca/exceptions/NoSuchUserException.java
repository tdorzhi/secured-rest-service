package jp.co.soramitsu.sora.bca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class NoSuchUserException extends UsernameNotFoundException {

  public NoSuchUserException(String username) {
    super("User with username " + username + " not found");
  }
}

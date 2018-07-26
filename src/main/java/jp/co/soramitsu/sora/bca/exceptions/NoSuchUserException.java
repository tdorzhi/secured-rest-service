package jp.co.soramitsu.sora.bca.exceptions;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class NoSuchUserException extends UsernameNotFoundException {

  public NoSuchUserException(String username) {
    super("User with username " + username + " not found");
  }
}

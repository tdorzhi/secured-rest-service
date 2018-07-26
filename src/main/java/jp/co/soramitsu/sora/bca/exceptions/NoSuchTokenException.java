package jp.co.soramitsu.sora.bca.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class NoSuchTokenException extends BadCredentialsException {

  public NoSuchTokenException(String token) {
    super("User with " + token + " is not found");
  }
}

package jp.co.soramitsu.sora.bca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NoSuchTokenException extends BadCredentialsException {

  public NoSuchTokenException(String token) {
    super("User with " + token + " is not found");
  }
}

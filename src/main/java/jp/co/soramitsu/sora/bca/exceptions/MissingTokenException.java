package jp.co.soramitsu.sora.bca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class MissingTokenException extends BadCredentialsException {

  public MissingTokenException() {
    super("Missing token header!");
  }
}

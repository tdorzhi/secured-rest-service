package jp.co.soramitsu.sora.bca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class WrongPasswordException extends BadCredentialsException {

  public WrongPasswordException(String username) {
    super("Password for given user " + username + " is incorrect");
  }
}

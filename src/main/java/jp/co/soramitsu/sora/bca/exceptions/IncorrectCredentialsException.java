package jp.co.soramitsu.sora.bca.exceptions;

import org.springframework.security.authentication.BadCredentialsException;

public class IncorrectCredentialsException extends BadCredentialsException {

  public IncorrectCredentialsException(String username) {
    super("Credentials for given user " + username + " are incorrect");
  }
}

package jp.co.soramitsu.sora.bca.exceptions;

import org.springframework.security.core.userdetails.UserDetails;

public class IncorrectCredentialsException extends Exception{

  public IncorrectCredentialsException(UserDetails userDetails) {
    super("Credentials for given user " + userDetails.getUsername() + " are incorrect");
  }
}

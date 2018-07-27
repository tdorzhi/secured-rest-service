package jp.co.soramitsu.sora.bca.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.OK)
public class UserExistsException extends Exception{

  public UserExistsException(String username) {
    super("User with username: " + username + " already exists");
  }
}

package jp.co.soramitsu.sora.bca.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class TokenInvalidatedException extends TokenExpiredException {

  public TokenInvalidatedException(String token) {
    super("Token " + token + " is no longer active!");
  }
}

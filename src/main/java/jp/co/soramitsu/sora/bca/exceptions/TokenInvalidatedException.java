package jp.co.soramitsu.sora.bca.exceptions;

import com.auth0.jwt.exceptions.TokenExpiredException;

public class TokenInvalidatedException extends TokenExpiredException {

  public TokenInvalidatedException(String token) {
    super("Token " + token + " is invalidated!");
  }
}

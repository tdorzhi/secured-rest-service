package jp.co.soramitsu.sora.bca.exceptions;

public class NoSuchTokenException extends IllegalStateException{

  public NoSuchTokenException(String token) {
    super("Token " + token + " is invalid, authentication required");
  }
}

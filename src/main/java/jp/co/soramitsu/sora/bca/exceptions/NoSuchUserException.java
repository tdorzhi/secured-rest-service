package jp.co.soramitsu.sora.bca.exceptions;

public class NoSuchUserException extends Exception{

  public NoSuchUserException(String username) {
    super("User with username " + username + " not found");
  }
}

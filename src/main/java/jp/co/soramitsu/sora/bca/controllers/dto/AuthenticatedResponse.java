package jp.co.soramitsu.sora.bca.controllers.dto;

import lombok.Getter;

@Getter
public class AuthenticatedResponse extends GenericResponse{
  private static final Status status = Status
      .builder()
      .code(Code.OK)
      .message("Authentication successful")
      .build();

  private String token;

  public AuthenticatedResponse(String token) {
    super(status);
    this.token = token;
  }
}

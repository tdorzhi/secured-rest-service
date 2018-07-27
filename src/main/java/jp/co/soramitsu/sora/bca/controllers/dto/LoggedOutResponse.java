package jp.co.soramitsu.sora.bca.controllers.dto;

public class LoggedOutResponse extends GenericResponse{

  public LoggedOutResponse(String token) {
    super(
        Status.builder()
            .code(Code.OK)
            .message("Token " + token + " invalidated, session effectively stopped")
            .build()
    );
  }
}

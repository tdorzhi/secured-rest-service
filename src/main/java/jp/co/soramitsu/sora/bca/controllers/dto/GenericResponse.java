package jp.co.soramitsu.sora.bca.controllers.dto;

import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Value;

@EqualsAndHashCode
@Getter
public class GenericResponse {

  private Status status;

  public GenericResponse(
      Status status) {
    this.status = status;
  }

  @Value
  @Builder
  public static class Status {
    private Code code;
    private String message;
    private List<String> errors;
  }

  public enum Code {
    OK, ERROR
  }
}

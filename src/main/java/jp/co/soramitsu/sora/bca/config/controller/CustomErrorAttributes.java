package jp.co.soramitsu.sora.bca.config.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import jp.co.soramitsu.sora.bca.controllers.dto.GenericResponse.Code;
import jp.co.soramitsu.sora.bca.controllers.dto.GenericResponse.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * Used to make default error message conform to our DTO template
 *
 * see https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-error-handling
 */
@Component
@Slf4j
public class CustomErrorAttributes extends DefaultErrorAttributes {

  private static Map<String, Object> errorResponseTemplate = new HashMap<>();

  static {
    errorResponseTemplate.put("status", null);
  }

  @Override
  public Map<String, Object> getErrorAttributes(
      WebRequest webRequest,
      boolean includeStackTrace
  ) {
    Map<String, Object> defaultAttributes = super
        .getErrorAttributes(webRequest, includeStackTrace);
    log.warn("Exception during request processing:\nmessage: {}\noriginal status: {}, at path: {}",
        defaultAttributes.get("message"),
        defaultAttributes.get("status"),
        defaultAttributes.get("path")
    );
    Map<String, Object> errorAttributes = new LinkedHashMap<>(errorResponseTemplate);
    errorAttributes.put("status",
        Status
            .builder()
            .code(Code.ERROR)
            .message(defaultAttributes.get("message").toString())
            .errors(Collections.emptyList())
            .build()
    );
    return errorAttributes;
  }

}

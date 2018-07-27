package jp.co.soramitsu.sora.bca.config.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Used to change default response entity for controllers returning {@link MediaType#APPLICATION_JSON_UTF8_VALUE}
 *
 * see https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-error-handling
 * */
@Configuration
@Slf4j
public class ControllerConfig extends BasicErrorController {

  public ControllerConfig(ErrorAttributes customErrorAttributes) {
    super(customErrorAttributes, new ErrorProperties());
  }

  @RequestMapping(produces = APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity errorResponse(HttpServletRequest request) {
    Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
    return ResponseEntity.ok(body);
  }

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();

    return mapper;
  }

}

package jp.co.soramitsu.sora.bca.utils;

import static org.springframework.util.StringUtils.isEmpty;

import com.google.common.net.HttpHeaders;
import javax.servlet.http.HttpServletRequest;
import jp.co.soramitsu.sora.bca.exceptions.MissingTokenException;
import lombok.experimental.UtilityClass;
import org.springframework.web.context.request.WebRequest;

@UtilityClass
public class TokenExtractor {

  private String fromHeaderValue(String headerValue) {
    String token = headerValue.substring("Bearer".length()).trim();
    if (isEmpty(token)) {
      throw new MissingTokenException();
    }
    return token;
  }

  public String extractToken(HttpServletRequest request) {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (!isEmpty(authHeader)) {
      return fromHeaderValue(authHeader);
    } else {
      throw new MissingTokenException();
    }
  }

  public String extractToken(WebRequest request) {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (!isEmpty(authHeader)) {
      return fromHeaderValue(authHeader);
    } else {
      throw new MissingTokenException();
    }
  }

}

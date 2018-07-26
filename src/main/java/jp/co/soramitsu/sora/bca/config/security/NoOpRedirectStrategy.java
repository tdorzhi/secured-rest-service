package jp.co.soramitsu.sora.bca.config.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.web.RedirectStrategy;

public class NoOpRedirectStrategy implements RedirectStrategy {

  /**
   * Non-operational strategy - redirection doesn't make sense in REST API
   *
   * */
  @Override
  public void sendRedirect(HttpServletRequest request, HttpServletResponse response, String url)
      throws IOException {

  }
}

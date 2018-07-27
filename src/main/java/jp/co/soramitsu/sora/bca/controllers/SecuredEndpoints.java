package jp.co.soramitsu.sora.bca.controllers;

import jp.co.soramitsu.sora.bca.controllers.dto.GenericResponse;
import jp.co.soramitsu.sora.bca.controllers.dto.LoggedOutResponse;
import jp.co.soramitsu.sora.bca.domain.entities.User;
import jp.co.soramitsu.sora.bca.services.UserAuthService;
import jp.co.soramitsu.sora.bca.utils.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/users")
public class SecuredEndpoints {

  private UserAuthService<User> userAuthService;

  @Autowired
  public SecuredEndpoints(UserAuthService<User> userAuthService) {
    this.userAuthService = userAuthService;
  }

  @GetMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<GenericResponse> logout(WebRequest request) {
    String token = TokenExtractor.extractToken(request);
    userAuthService.logout(token);
    return ResponseEntity.ok(new LoggedOutResponse(token));
  }

}

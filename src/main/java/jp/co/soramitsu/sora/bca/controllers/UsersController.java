package jp.co.soramitsu.sora.bca.controllers;

import jp.co.soramitsu.sora.bca.controllers.dto.AuthenticatedResponse;
import jp.co.soramitsu.sora.bca.controllers.dto.GenericResponse;
import jp.co.soramitsu.sora.bca.domain.entities.User;
import jp.co.soramitsu.sora.bca.exceptions.UserExistsException;
import jp.co.soramitsu.sora.bca.services.UserAuthService;
import jp.co.soramitsu.sora.bca.services.UserCrudService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/public/users")
public class UsersController {

  public UsersController(
      UserAuthService<User> userAuthService,
      UserCrudService userCrudService) {
    this.userAuthService = userAuthService;
    this.userCrudService = userCrudService;
  }

  private UserAuthService<User> userAuthService;
  private UserCrudService userCrudService;

  @PostMapping("/register")
  public ResponseEntity<GenericResponse> register(
      @RequestParam String username,
      @RequestParam String password
  ) throws UserExistsException {
    log.info("Try to register user {}", username);
    userCrudService.createUser(username, password);
    return login(username, password);
  }

  @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<GenericResponse> login(
      @RequestParam String username,
      @RequestParam String password
  ) {
    log.info("Try to login user {}", username);
    return ResponseEntity.ok(new AuthenticatedResponse(userAuthService.login(username, password)));
  }

}

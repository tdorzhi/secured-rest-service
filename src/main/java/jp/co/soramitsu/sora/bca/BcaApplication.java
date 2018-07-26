package jp.co.soramitsu.sora.bca;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@RestController
@Slf4j
public class BcaApplication {

  public static void main(String[] args) {
    SpringApplication.run(BcaApplication.class, args);
  }

  @PostMapping("/login")
  public ResponseEntity postLoginData(@RequestBody LoginAs loginData) {
    return ResponseEntity.ok().build();
  }

  @Value
  public static class LoginAs {
    String username;
    String password;
  }

}

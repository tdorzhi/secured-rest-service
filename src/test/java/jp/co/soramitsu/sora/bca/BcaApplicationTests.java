package jp.co.soramitsu.sora.bca;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static com.jayway.jsonpath.JsonPath.read;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Optional;
import java.util.UUID;
import javax.sql.DataSource;
import jp.co.soramitsu.sora.bca.domain.entities.User;
import jp.co.soramitsu.sora.bca.domain.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(
    webEnvironment = WebEnvironment.RANDOM_PORT
)
@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@AutoConfigureMockMvc
@Slf4j
public class BcaApplicationTests {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private PlatformTransactionManager transactionManager;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @ClassRule
  public static PostgreSQLContainer postgres = new PostgreSQLContainer();

//  test data
  private String username = "superman";
  private String password = "lois lane";

  @TestConfiguration
  public static class TestDataSource {

    @Bean
    public DataSource dataSource() {
      HikariConfig config = new HikariConfig();
      config.setJdbcUrl(postgres.getJdbcUrl());
      config.setUsername(postgres.getUsername());
      config.setPassword(postgres.getPassword());

      return new HikariDataSource(config);
    }

  }

  @Before
  public void setUp() {
    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setUid(UUID.randomUUID());
    user.setActive(true);

    userRepository.save(user);
  }

  @After
  public void tearDown() {
    userRepository.deleteAll();
  }

  @Test
  public void givenExistingUser_assertCanLogin() {
    ResponseEntity<String> response = login(username, password);

    log.info(response.getBody());
    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(read(response.getBody(), "$.status.code"), is("OK"));
    assertThat(read(response.getBody(), "$.token"), not(isEmptyOrNullString()));
  }

  @Test
  public void givenNewUser_assertUserPersistedAndTokenReturned() throws Exception {
    String username = "batman";
    String password = "catgirl";

    ResponseEntity<String> response = registerUser(username, password);

    String code = read(response.getBody(), "$.status.code");
    String token = read(response.getBody(), "$.token");

    assertThat(response.getStatusCode(), is(OK));
    assertThat(code, is("OK"));
    assertThat(token, not(isEmptyOrNullString()));

    Optional<User> user = userRepository.findByUsername(username);
    assertThat(user.isPresent(), is(true));
    assertThat(user.get().getUid(), notNullValue());
    assertThat(passwordEncoder.matches(password, user.get().getPassword()), is(true));
  }

  @Test
  public void givenUnauthorizedRequest_assertCantAccessResource() throws Exception {
    ResponseEntity<String> response = logout(null);
    assertThat(response.getStatusCode(), is(OK));
    assertThat(read(response.getBody(), "$.status.code"), is("ERROR"));
    assertThat(read(response.getBody(), "$.status.message"), equalToIgnoringCase("Unauthorized"));
  }

  @Test
  public void givenCorruptedToken_assertCantAccessResource() throws Exception {
    String username = "batman";
    String password = "catgirl";
    ResponseEntity<String> response = registerUser(username, password);

    /*
     * Original token
     * */
    DecodedJWT jwt = JWT.decode(read(response.getBody(), "$.token"));

    /*
     * Create dummy token with wrong subject
     * */
    Algorithm algo = Algorithm.HMAC256("FAKE SECRET");
    String falsifiedToken = JWT.create().withSubject("FAKE SUBJECT").withIssuedAt(jwt.getIssuedAt())
        .withExpiresAt(jwt.getExpiresAt()).sign(algo);
    DecodedJWT falsifiedJwt = JWT.decode(falsifiedToken);

    String mixedToken =
        jwt.getHeader() + "." + falsifiedJwt.getPayload() + "." + jwt.getSignature();

    response = logout(mixedToken);

    assertThat(response.getStatusCode(), is(OK));
    assertThat(read(response.getBody(), "$.status.code"), is("ERROR"));
    assertThat(read(response.getBody(), "$.status.message"), containsString("invalid"));
  }

  @Test
  public void givenToken_assertCantLogoutTwice() throws Exception {
    String username = "batman";
    String password = "catgirl";

    String token = read(registerUser(username, password).getBody(), "$.token");

    logout(token);

    ResponseEntity<String> response = logout(token);

    assertThat(response.getStatusCode(), is(HttpStatus.OK));
    assertThat(read(response.getBody(), "$.status.code"), is("ERROR"));
    assertThat(read(response.getBody(), "$.status.message"), containsString("no longer active"));
  }

  private ResponseEntity<String> login(String username, String password) {
    return testRestTemplate.postForEntity(
        "/public/users/login?username={username}&password={password}",
        null,
        String.class,
        username,
        password
    );
  }

  private ResponseEntity<String> logout(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(singletonList(MediaType.APPLICATION_JSON_UTF8));
    if (token != null) {
      headers.set(AUTHORIZATION, "Bearer " + token);
    }
    HttpEntity<String> http = new HttpEntity<>(null, headers);

    return testRestTemplate.exchange("/users/logout", GET, http, String.class);
  }

  private ResponseEntity<String> registerUser(String username, String password) {
    return testRestTemplate
        .postForEntity(
            "/public/users/register?username={username}&password={password}",
            null,
            String.class,
            username,
            password
        );
  }

}

package jp.co.soramitsu.sora.bca.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "auth")
@Component
@Data
public class SecurityProperties {

  @NestedConfigurationProperty
  private BlacklistProperties blacklist = new BlacklistProperties();
  @NestedConfigurationProperty
  private JwtProperties jwt = new JwtProperties();

  @Data
  public static class BlacklistProperties {
    /**
     * Initial capacity of blacklist of {@link jp.co.soramitsu.sora.bca.services.UserAuthServiceImpl}
     * eliminates additional overhead when blacklist is being warmed up quickly
     * */
    private int initialCapacity = 1000;
  }

  @Data
  public static class JwtProperties {
    /**
     * Algorithm HMAC-256 requires this secret for signing JWT tokens
     *
     * @see jp.co.soramitsu.sora.bca.services.TokenServiceImpl
     * */
    private String secret;

    /**
     * Value in SECONDS within which JWT token is considered valid
     *
     * @see jp.co.soramitsu.sora.bca.services.TokenServiceImpl
     * */
    private int tokenExpiresIn = 180;

  }

}

package jp.co.soramitsu.sora.bca.services;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jp.co.soramitsu.sora.bca.config.properties.SecurityProperties;
import jp.co.soramitsu.sora.bca.exceptions.TokenInvalidatedException;
import jp.co.soramitsu.sora.bca.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{

  @Autowired
  public TokenServiceImpl(SecurityProperties properties) {
    if (properties.getJwt().getSecret() == null) {
      throw new IllegalStateException("Properties must contain secret for signing JWT tokens!");
    }
    this.algo =  Algorithm.HMAC256(properties.getJwt().getSecret());
    this.jwtVerifier = JWT.require(algo).build();
    this.tokenExpiresIn = properties.getJwt().getTokenExpiresIn();
    this.blacklist = CacheBuilder
        .newBuilder()
        .initialCapacity(properties.getBlacklist().getInitialCapacity())
        .expireAfterWrite(tokenExpiresIn, SECONDS)
        .build();
  }

  /**
   * Blacklist of tokens
   * Chosen Guava's cache because this is only viable solution for keeping something with specified
   * expiration time
   *
   * Allows setting expiration policy as there is no need to keep blacklisted tokens forever,
   * only limited time which is no longer than {{@link #tokenExpiresIn}}
   * */
  private Cache<String, Object> blacklist;

  /**
   * Blacklist's implementation {@link Cache} doesn't allow null for values
   * This dummy object will be used instead
   * */
  private Object dummyObject = new Object();

  private int tokenExpiresIn;
  private Algorithm algo;
  private JWTVerifier jwtVerifier;

  @Override
  public String generateToken(String subject) {
    return JWT.create()
        .withExpiresAt(DateUtil.nowPlusSeconds(tokenExpiresIn))
        .withIssuedAt(DateUtil.nowPlusSeconds(0))
        .withSubject(subject)
        .sign(algo);
  }

  /**
   * {@inheritDoc}
   *
   * @throws com.auth0.jwt.exceptions.JWTVerificationException if token is invalid
   * */
  @Override
  public String extractSubject(String token) {
    if (blacklist.getIfPresent(token) != null) {
      throw new TokenInvalidatedException(token);
    }
    return jwtVerifier.verify(token).getSubject();
  }

  /**
   * {@inheritDoc}
   *
   * @implNote doesn't persist token anywhere, it's only stored in local cache
   * */
  @Override
  public void blacklist(String token) {
    blacklist.put(token, dummyObject);
  }
}

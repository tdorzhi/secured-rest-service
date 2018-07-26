package jp.co.soramitsu.sora.bca.utils;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.GrantedAuthority;

/**
 * For initial implementation we don't want to make domain too complex
 * and burden RDBMS with additional tables. Decision upon whether we should keep authorities
 * hard-coded or release to persistence layer is delayed
 *
 * In case authorities are moved to persistence layer, this class no longer needed and can
 * be safely removed
 * */
public class AuthoritiesOf {

  public static Collection<? extends GrantedAuthority> user() {
    return Collections.singleton(new UserAuthority());
  }

  private static class UserAuthority implements GrantedAuthority {

    @Override
    public String getAuthority() {
      return "ROLE_USER";
    }
  }

}

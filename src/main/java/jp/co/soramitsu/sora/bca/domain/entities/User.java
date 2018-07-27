package jp.co.soramitsu.sora.bca.domain.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import jp.co.soramitsu.sora.bca.utils.AuthoritiesOf;
import lombok.Data;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "`users`")
@Data
public class User implements UserDetails, Serializable {
  @Id
  @Type(type = "org.hibernate.type.PostgresUUIDType")
  private UUID uid;

  @NaturalId
  private String username;
  private String password;
  private boolean isActive;

  /**
   * For initial implementation we don't want complex domain, so the granted authorities
   * */
  @Override
  @Transient
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return AuthoritiesOf.user();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  @Transient
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  @Transient
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  @Transient
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  @Transient
  public boolean isEnabled() {
    return isActive;
  }
}

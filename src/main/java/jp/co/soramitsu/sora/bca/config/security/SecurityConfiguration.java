package jp.co.soramitsu.sora.bca.config.security;

import static org.springframework.http.HttpMethod.POST;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
        .antMatchers(POST, "/login").permitAll()
        .anyRequest().authenticated();
  }
}

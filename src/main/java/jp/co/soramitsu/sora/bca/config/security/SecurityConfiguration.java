package jp.co.soramitsu.sora.bca.config.security;

import static java.util.Objects.requireNonNull;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * Details can be looked up here: https://octoperf.com/blog/2018/03/08/securing-rest-api-spring-security/#securityconfig
 * */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
      new AntPathRequestMatcher("/public/**"),
      new AntPathRequestMatcher("/error")
  );
  private static final RequestMatcher PROTECTED_URLS = new NegatedRequestMatcher(PUBLIC_URLS);

  private TokenAuthenticationProvider provider;

  public SecurityConfiguration(final TokenAuthenticationProvider provider) throws Exception {
    super();
    this.provider = requireNonNull(provider);
  }

  @Override
  protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(provider);
  }

  private TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
    TokenAuthenticationFilter filter = new TokenAuthenticationFilter(PROTECTED_URLS);
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(successHandler());
    return filter;
  }

  /**
   * https://github.com/spring-projects/spring-security/issues/3958
   *
   * It was supposed to ignore security filter chain completely for {{@link #PUBLIC_URLS}} if not
   * issue above
   *
   * Instead we are ignoring {{@link #PUBLIC_URLS}} in {{@link #configure(HttpSecurity)}}
   */
  @Override
  public void configure(final WebSecurity web) {
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http
        .sessionManagement().sessionCreationPolicy(STATELESS)
        .and()
        .exceptionHandling()
        .defaultAuthenticationEntryPointFor(forbiddenEntryPoint(), PROTECTED_URLS)
        .and()
        .authenticationProvider(provider)
        .addFilterBefore(tokenAuthenticationFilter(), AnonymousAuthenticationFilter.class)
        .authorizeRequests()
        .requestMatchers(PUBLIC_URLS).permitAll()
        .anyRequest().authenticated()
        .and()
        .csrf().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .logout().disable();
  }

  @Bean
  public SimpleUrlAuthenticationSuccessHandler successHandler() {
    final SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
    successHandler.setRedirectStrategy(new NoOpRedirectStrategy());
    return successHandler;
  }

  /**
   * Disable Spring boot automatic filter registration.
   */
  @Bean
  public FilterRegistrationBean disableAutoRegistration() throws Exception {
    final FilterRegistrationBean registration = new FilterRegistrationBean<>(
        tokenAuthenticationFilter());
    registration.setEnabled(false);
    return registration;
  }

  @Bean
  public AuthenticationEntryPoint forbiddenEntryPoint() {
    return new HttpStatusEntryPoint(UNAUTHORIZED);
  }
}

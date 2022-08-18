package com.github.ProfSchmergmann.TournamentWebApplication.security;

import com.github.ProfSchmergmann.TournamentWebApplication.views.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends VaadinWebSecurityConfigurerAdapter {

  /**
   * Allows access to static resources, bypassing Spring security.
   */
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/images/**");
    super.configure(web);
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    super.configure(http);
    this.setLoginView(http, LoginView.class);
  }

  /**
   * Demo UserDetailService, which only provides two hardcoded in-memory users and their roles.
   * NOTE: This should not be used in real-world applications.
   */
  @Bean
  @Override
  public UserDetailsService userDetailsService() {
    return new InMemoryUserDetailsManager(User.withUsername("user")
        .password("{noop}userpass")
        .roles("USER")
        .build());
  }
}
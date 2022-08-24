package com.github.profschmergmann.tournamentwebapplication.database.models.user;

import com.github.profschmergmann.tournamentwebapplication.database.models.Model;
import java.util.Collection;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
public class User implements UserDetails, Model {

  @Email
  private String email;
  @NotNull
  private String firstName;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @NotNull
  private String lastName;
  private String password;
  @Enumerated(EnumType.STRING)
  private Role role;
  @NotNull
  private String userName;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    var simpleGrantedAuthority = new SimpleGrantedAuthority(this.role.name());
    return List.of(simpleGrantedAuthority);
  }

  @Override
  public String getUsername() {
    return this.userName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public enum Role {
    USER, ADMIN, TEAM_CAPTAIN, COURT_MANAGER, PLAYER, TRAINER
  }

}

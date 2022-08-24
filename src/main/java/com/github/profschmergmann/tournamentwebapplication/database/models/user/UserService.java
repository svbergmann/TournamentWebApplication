package com.github.profschmergmann.tournamentwebapplication.database.models.user;

import com.github.profschmergmann.tournamentwebapplication.database.models.user.User.Role;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

  private final UserRepository userRepository;

  public User findByEmail(String email) {
    return this.userRepository.findByEmail(email);
  }

  public List<User> findByFirstAndLastName(String firstName, String lastName) {
    return this.userRepository.findAllByFirstNameAndLastName(firstName, lastName);
  }

  public List<User> findByRole(Role role) {
    return this.userRepository.findByRole(role);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return this.userRepository.findByUserName(username);
  }

  public User signUpUser(User user) {
    user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
    return this.userRepository.save(user);
  }

  public User update(User user, long id) {
    return null;
  }

}

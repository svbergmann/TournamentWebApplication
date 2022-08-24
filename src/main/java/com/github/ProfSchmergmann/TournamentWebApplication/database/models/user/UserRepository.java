package com.github.profschmergmann.tournamentwebapplication.database.models.user;

import com.github.profschmergmann.tournamentwebapplication.database.models.user.User.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  List<User> findAllByFirstNameAndLastName(String firstName, String lastName);

  User findByEmail(String email);

  List<User> findByRole(Role role);

  User findByUserName(String username);
}

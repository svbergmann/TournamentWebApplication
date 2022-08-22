package com.github.ProfSchmergmann.TournamentWebApplication.database.models.user;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.user.User.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	List<User> findAllByFirstNameAndLastName(String firstName, String lastName);

	User findByEmail(String email);

	List<User> findByRole(Role role);

	User findByUserName(String username);
}

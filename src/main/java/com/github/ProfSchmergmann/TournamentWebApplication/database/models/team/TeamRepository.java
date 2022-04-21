package com.github.ProfSchmergmann.TournamentWebApplication.database.models.team;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.Gym;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

}
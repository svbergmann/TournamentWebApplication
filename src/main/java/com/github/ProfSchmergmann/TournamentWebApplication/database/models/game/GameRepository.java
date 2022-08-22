package com.github.ProfSchmergmann.TournamentWebApplication.database.models.game;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.Gym;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.match.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

	Set<Game> findAllByGym(Gym gym);

	Set<Game> findAllByMatch(Match match);

}
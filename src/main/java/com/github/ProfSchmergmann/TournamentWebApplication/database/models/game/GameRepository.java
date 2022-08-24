package com.github.profschmergmann.tournamentwebapplication.database.models.game;

import com.github.profschmergmann.tournamentwebapplication.database.models.gym.Gym;
import com.github.profschmergmann.tournamentwebapplication.database.models.match.Match;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

  Set<Game> findAllByGym(Gym gym);

  Set<Game> findAllByMatch(Match match);

}
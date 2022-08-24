package com.github.profschmergmann.tournamentwebapplication.database;

import com.github.profschmergmann.tournamentwebapplication.database.models.game.GameService;
import com.github.profschmergmann.tournamentwebapplication.database.models.team.Team;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TournamentService {

  private static GameService game_Service;

  @Autowired
  private GameService gameService;

  public static int getPlayedGames(Team team) {
    // TODO: Find games by team
    throw new NotImplementedException();
  }

  @PostConstruct
  public void init() {
    this.gameService = game_Service;
  }

}

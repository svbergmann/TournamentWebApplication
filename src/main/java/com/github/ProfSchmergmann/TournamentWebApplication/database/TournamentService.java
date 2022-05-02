package com.github.ProfSchmergmann.TournamentWebApplication.database;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.game.GameService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TournamentService {

	private static GameService game_Service;

	@Autowired
	private GameService gameService;

	@PostConstruct
	public void init() {
		this.gameService = game_Service;
	}

	public static int getPlayedGames(Team team) {
		// TODO: Find games by team
		throw new NotImplementedException();
	}

}

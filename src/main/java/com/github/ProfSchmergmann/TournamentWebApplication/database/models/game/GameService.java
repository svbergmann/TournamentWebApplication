package com.github.ProfSchmergmann.TournamentWebApplication.database.models.game;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class GameService implements IModelService<Game> {

	@Autowired
	private GameRepository repository;

	@Override
	public Game create(Game game) {
		return this.findAll().stream().anyMatch(l -> l.equals(game)) ?
		       null : this.repository.save(game);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<Game> findAll() {
		return this.repository.findAll();
	}

	@Override
	public Game findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public Game findByName(String name) {
		return null;
	}

	@Override
	public Game update(Game game, long id) {
		var gameDB = this.repository.findById(id);

		if (gameDB.isPresent()) {
			var g = gameDB.get();
			g.setDate(game.getDate());
			g.setFinished(game.isFinished());
			g.setGym(game.getGym());
			g.setMatch(game.getMatch());
			return this.repository.save(g);
		}
		return null;
	}

	public Stream<Game> getFinishedGames(Team team) {
		return this.getGames(team).filter(Game::isFinished);
	}

	public Stream<Game> getGames(Team team) {
		return this.findAll()
		           .stream()
		           .filter(g -> g.getMatch().getTeamA().equals(team) ||
				           g.getMatch().getTeamB().equals(team));
	}

	public Stream<Game> getLostGames(Team team) {
		return this.getFinishedGames(team)
		           .filter(game -> {
			           var match = game.getMatch();
			           return match.getTeamA().equals(team) ?
			                  match.getScoreTeamA() < match.getScoreTeamB() :
			                  match.getScoreTeamA() > match.getScoreTeamB();
		           });
	}

	public int getPlusMinus(Team team) {
		return this.getFinishedGames(team)
		           .mapToInt(g -> {
			           var match = g.getMatch();
			           return match.getTeamA().equals(team) ?
			                  match.getScoreTeamA() - match.getScoreTeamB() :
			                  match.getScoreTeamB() - match.getScoreTeamA();
		           })
		           .sum();
	}

	public Stream<Game> getWonGames(Team team) {
		return this.getFinishedGames(team)
		           .filter(game -> {
			           var match = game.getMatch();
			           return match.getTeamA().equals(team) ?
			                  match.getScoreTeamA() > match.getScoreTeamB() :
			                  match.getScoreTeamA() < match.getScoreTeamB();
		           });
	}

}

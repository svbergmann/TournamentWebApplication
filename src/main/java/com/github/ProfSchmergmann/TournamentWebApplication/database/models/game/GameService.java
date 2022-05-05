package com.github.ProfSchmergmann.TournamentWebApplication.database.models.game;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class GameService extends IModelService<Game> {

	public GameService(@Autowired GameRepository repository) {
		super(repository);
	}

	public Stream<Game> findAll(Team team) {
		return this.findAll()
		           .stream()
		           .filter(g -> g.getMatch().getTeamA().equals(team) ||
				           g.getMatch().getTeamB().equals(team));
	}

	public Stream<Game> findAll(AgeGroup ageGroup, Gender gender) {
		return this.repository.findAll()
		                      .stream()
		                      .filter(game -> game.getMatch().getTeamA().getGender().equals(gender) &&
				                      game.getMatch().getTeamA().getAgeGroup().equals(ageGroup));
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
		return this.findAll(team).filter(Game::isFinished);
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

	public int getPosition(Team team) {
		return this.getSortedTeams(team.getAgeGroup(), team.getGender()).indexOf(team) + 1;
	}

	public List<Team> getSortedTeams(AgeGroup ageGroup, Gender gender) {
		return this.findAll(ageGroup, gender)
		           .map(game -> game.getMatch().getTeamA())
		           .sorted((t1, t2) ->
				                   (int) this.getWonGames(t1).count() -
						                   (int) this.getWonGames(t2).count())
		           .toList();
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

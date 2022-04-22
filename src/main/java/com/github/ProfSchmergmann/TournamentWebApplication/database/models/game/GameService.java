package com.github.ProfSchmergmann.TournamentWebApplication.database.models.game;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
			g.setId(game.getId());
			g.setScoreTeamA(game.getScoreTeamA());
			g.setScoreTeamB(game.getScoreTeamB());
			g.setTeamA(game.getTeamA());
			g.setTeamB(game.getTeamB());
			this.repository.deleteById(id);
			return this.repository.save(g);
		}
		return null;
	}
}

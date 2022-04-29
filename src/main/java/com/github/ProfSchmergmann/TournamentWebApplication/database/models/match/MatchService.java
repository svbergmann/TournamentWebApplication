package com.github.ProfSchmergmann.TournamentWebApplication.database.models.match;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService implements IModelService<Match> {

	@Autowired
	private MatchRepository repository;

	@Override
	public Match create(Match match) {
		return this.findAll().stream().anyMatch(l -> l.equals(match)) ?
		       null : this.repository.save(match);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<Match> findAll() {
		return this.repository.findAll();
	}

	@Override
	public Match findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public Match findByName(String name) {
		return null;
	}

	@Override
	public Match update(Match match, long id) {
		var gameDB = this.repository.findById(id);

		if (gameDB.isPresent()) {
			var m = gameDB.get();
			m.setScoreTeamA(match.getScoreTeamA());
			m.setScoreTeamB(match.getScoreTeamB());
			m.setTeamA(match.getTeamA());
			m.setTeamB(match.getTeamB());
			return this.repository.save(m);
		}
		return null;
	}
}

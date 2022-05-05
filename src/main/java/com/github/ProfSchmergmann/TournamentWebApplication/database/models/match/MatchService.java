package com.github.ProfSchmergmann.TournamentWebApplication.database.models.match;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService extends IModelService<Match> {

	public MatchService(@Autowired MatchRepository repository) {
		super(repository);
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

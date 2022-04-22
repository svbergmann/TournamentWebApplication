package com.github.ProfSchmergmann.TournamentWebApplication.database.models.team;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService implements IModelService<Team> {

	@Autowired
	private TeamRepository repository;

	@Override
	public Team create(Team team) {
		return this.findAll().stream().anyMatch(l -> l.equals(team)) ?
		       null : this.repository.save(team);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<Team> findAll() {
		return this.repository.findAll();
	}

	@Override
	public Team findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public Team findByName(String name) {
		return null;
	}

	@Override
	public Team update(Team team, long id) {
		var teamDB = this.repository.findById(id);

		if (teamDB.isPresent()) {
			var t = teamDB.get();
			t.setId(team.getId());
			t.setAmount(team.getAmount());
			t.setAgeGroup(team.getAgeGroup());
			t.setClub(team.getClub());
			t.setGender(team.getGender());
			this.repository.deleteById(id);
			return this.repository.save(t);
		}
		return null;
	}

	public List<Team> findAllFromAgeGroup(AgeGroup ageGroup) {
		return this.repository.findAll().stream().filter(team -> team.getAgeGroup().equals(ageGroup))
				.toList();
	}

	public List<Team> findAllFromClub(Club club) {
		return this.repository.findAll().stream().filter(team -> team.getClub().equals(club)).toList();
	}
}

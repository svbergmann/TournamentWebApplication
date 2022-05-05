package com.github.ProfSchmergmann.TournamentWebApplication.database.models.team;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService extends IModelService<Team> {

	public TeamService(@Autowired TeamRepository repository) {
		super(repository);
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
			t.setAmount(team.getAmount());
			t.setAgeGroup(team.getAgeGroup());
			t.setClub(team.getClub());
			t.setGender(team.getGender());
			return this.repository.save(t);
		}
		return null;
	}

	public List<Team> findAll(AgeGroup ageGroup) {
		return this.repository.findAll().stream()
		                      .filter(team -> team.getAgeGroup().equals(ageGroup))
		                      .toList();
	}

	public List<Team> findAll(Gender gender) {
		return this.repository.findAll().stream()
		                      .filter(team -> team.getGender().equals(gender))
		                      .toList();
	}

	public List<Team> findAll(Club club) {
		return this.repository.findAll().stream()
		                      .filter(team -> team.getClub().equals(club))
		                      .toList();
	}

	public List<Team> findAll(AgeGroup ageGroup, Gender gender) {
		return this.repository.findAll().stream()
		                      .filter(team -> team.getGender().equals(gender))
		                      .filter(team -> team.getAgeGroup().equals(ageGroup))
		                      .toList();
	}
}

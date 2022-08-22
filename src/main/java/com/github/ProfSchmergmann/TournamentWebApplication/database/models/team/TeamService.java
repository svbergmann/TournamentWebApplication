package com.github.ProfSchmergmann.TournamentWebApplication.database.models.team;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService extends IModelService<Team> {

	private final TeamRepository teamRepository;

	public TeamService(@Autowired TeamRepository repository) {
		super(repository);
		this.teamRepository = repository;
	}

	public List<Team> findAll(AgeGroup ageGroup) {
		return this.teamRepository.findByAgeGroup(ageGroup);
	}

	public List<Team> findAll(Gender gender) {
		return this.teamRepository.findByGender(gender);
	}

	public List<Team> findAll(Club club) {
		return this.teamRepository.findByClub(club);
	}

	public List<Team> findAll(AgeGroup ageGroup, Gender gender) {
		return this.teamRepository.findByAgeGroupAndGender(ageGroup, gender);
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
}

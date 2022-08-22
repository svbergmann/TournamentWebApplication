package com.github.ProfSchmergmann.TournamentWebApplication.database.models.team;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

	List<Team> findByAgeGroup(AgeGroup ageGroup);

	List<Team> findByAgeGroupAndGender(AgeGroup ageGroup, Gender gender);

	List<Team> findByClub(Club club);

	List<Team> findByGender(Gender gender);

}
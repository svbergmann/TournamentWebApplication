package com.github.ProfSchmergmann.TournamentWebApplication.database.models.match;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

  @Query("select distinct m from Match m where m.teamA.ageGroup = ?1 or m.teamB.ageGroup = ?1")
  Set<Match> findDistinctByAgeGroup(AgeGroup ageGroup);

  Set<Match> findDistinctByFinished(boolean finished);

  @Query("select distinct m from Match m where m.finished = ?1 and (m.teamA = ?2 or m.teamB = ?2)")
  Set<Match> findDistinctByFinishedAndTeam(boolean finished, Team team);

  @Query("select distinct m from Match m where m.teamA.gender = ?1 or m.teamB.gender = ?1")
  Set<Match> findDistinctByGender(Gender gender);

  @Query("select distinct m from Match m " +
      "where (m.teamA.gender = ?1 or m.teamB.gender = ?1)" +
      "and (m.teamA.ageGroup = ?2 or m.teamB.ageGroup = ?2)")
  Set<Match> findDistinctByGenderAndAgeGroup(Gender gender, AgeGroup ageGroup);

  @Query("select distinct m from Match m where m.teamA = ?1 or m.teamB = ?1")
  Set<Match> findDistinctByTeam(Team team);

  @Query("select distinct m from Match m " +
      "where (m.teamA = ?1 and m.scoreTeamA > m.scoreTeamB) " +
      "or (m.teamB = ?1 and m.scoreTeamB > m.scoreTeamA)")
  Set<Match> findWonMatches(Team team);

  @Query("select distinct m from Match m " +
      "where (m.teamA = ?1 and m.scoreTeamA < m.scoreTeamB) " +
      "or (m.teamB = ?1 and m.scoreTeamB < m.scoreTeamA)")
  Set<Match> findLostMatches(Team team);

}
package com.github.ProfSchmergmann.TournamentWebApplication.database.models.match;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService extends IModelService<Match> {

  private final MatchRepository matchRepository;

  public MatchService(@Autowired MatchRepository repository) {
    super(repository);
    this.matchRepository = repository;
  }

  public Set<Match> findByAgeGroup(AgeGroup ageGroup) {
    return this.matchRepository.findDistinctByAgeGroup(ageGroup);
  }

  public Set<Match> findByFinished(boolean finished) {
    return this.matchRepository.findDistinctByFinished(finished);
  }

  public Set<Match> findByFinishedAndTeam(boolean finished, Team team) {
    return this.matchRepository.findDistinctByFinishedAndTeam(finished, team);
  }

  public Set<Match> findByGender(Gender gender) {
    return this.matchRepository.findDistinctByGender(gender);
  }

  public Set<Match> findByGenderAndAgeGroup(Gender gender, AgeGroup ageGroup) {
    return this.matchRepository.findDistinctByGenderAndAgeGroup(gender, ageGroup);
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

  public Set<Match> findByTeam(Team team) {
    return this.matchRepository.findDistinctByTeam(team);
  }

  public Set<Match> findLostMatchesByTeam(Team team) {
    return this.matchRepository.findLostMatches(team);
  }

  public Set<Match> findWonMatchesByTeam(Team team) {
    return this.matchRepository.findWonMatches(team);
  }

  public int getPlusMinus(Team team) {
    return this.matchRepository.findDistinctByTeam(team)
        .stream()
        .mapToInt(m -> m.getTeamA().equals(team) ?
            m.getScoreTeamA() - m.getScoreTeamB() :
            m.getScoreTeamB() - m.getScoreTeamA())
        .sum();
  }

  public int getPosition(Team team) {
    return this.getSortedTeams(team.getAgeGroup(), team.getGender()).indexOf(team) + 1;
  }

  public List<Team> getSortedTeams(AgeGroup ageGroup, Gender gender) {
    return this.matchRepository.findDistinctByGenderAndAgeGroup(gender, ageGroup)
        .stream()
        .map(Match::getTeamA)
        .sorted((t1, t2) ->
            this.matchRepository.findWonMatches(t1).size() -
                this.matchRepository.findWonMatches(t2).size())
        .toList();
  }
}

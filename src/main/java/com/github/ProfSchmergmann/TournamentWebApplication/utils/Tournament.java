package com.github.ProfSchmergmann.TournamentWebApplication.utils;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.match.Match;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tournament {

  public static Set<Match> getRoundRobinGames(List<Team> teams) {
    var matches = new HashSet<Match>();
    for (Team team : teams) {
      for (Team team1 : teams) {
        if (!team.equals(team1)) {
          var match = new Match();
          match.setTeamA(team);
          match.setTeamB(team1);
          matches.add(match);
        }
      }
    }
    return matches;
  }

}
package com.github.ProfSchmergmann.TournamentWebApplication.utils;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.match.Match;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;

import java.util.ArrayList;
import java.util.List;

public class Tournament {

	public static List<Match> getRoundRobinGames(List<Team> teams) {
		var matches = new ArrayList<Match>();
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

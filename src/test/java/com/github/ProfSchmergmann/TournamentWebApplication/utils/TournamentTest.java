package com.github.ProfSchmergmann.TournamentWebApplication.utils;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class TournamentTest {

	private final int numberTeams = 10;
	private List<Team> teams;

	private int fac(int i) {
		var res = 1;
		for (var j = i; j > 1; j--) {
			res *= j;
		}
		return res;
	}

	@Test
	void getRoundRobinGames() {
		var games = Tournament.getRoundRobinGames(this.teams);
		games.forEach(System.out::println);
		Assertions.assertEquals(this.numberTeams * (this.numberTeams - 1), games.size());
	}

	@BeforeEach
	void setUp() {
		this.teams = new ArrayList<>();
		for (var i = 1; i <= this.numberTeams; i++) {
			var t = new Team();
			t.setName("team" + i);
			this.teams.add(t);
		}
	}
}
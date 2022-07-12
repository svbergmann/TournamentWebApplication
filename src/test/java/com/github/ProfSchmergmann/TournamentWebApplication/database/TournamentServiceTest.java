package com.github.ProfSchmergmann.TournamentWebApplication.database;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroupService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.game.Game;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.game.GameService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.GenderService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.GymService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.match.MatchService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.TeamService;
import com.github.ProfSchmergmann.TournamentWebApplication.utils.Tournament;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class TournamentServiceTest {

//	@Test
//	public void createTestGames(@Autowired GameService gameService,
//	                            @Autowired TeamService teamService,
//	                            @Autowired AgeGroupService ageGroupService,
//	                            @Autowired GenderService genderService,
//	                            @Autowired GymService gymService,
//	                            @Autowired MatchService matchService) {
//		var games = new ArrayList<Game>();
//		for (var match : Tournament.getRoundRobinGames(teamService.findAll(
//				ageGroupService.findByName("men"),
//				genderService.findByName("m")))) {
//			var m = matchService.create(match);
//			var game = new Game();
//			game.setMatch(m);
//			game.setDate(new Date());
//			game.setGym(gymService.findByName("OSZ Banken und Versicherungen"));
//			gameService.create(game);
//			games.add(game);
//		}
//		var counter = (int) gameService.findAll().stream().filter(games::contains).count();
//		assertEquals(games.size(), counter);
//	}

}
package com.github.ProfSchmergmann.TournamentWebApplication.database.models.team;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroupService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.ClubService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.GenderService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import org.apache.logging.log4j.util.PropertySource.Comparator;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class TeamServiceTest {

	@Test
	@Order(3)
	public void createTestAgeGroup(@Autowired AgeGroupService ageGroupService) {
		var ageGroupName = "men";
		var a = new AgeGroup();
		a.setName(ageGroupName);
		ageGroupService.create(a);
		assertNotNull(ageGroupService.findByName(ageGroupName));
	}

	@Test
	@Order(4)
	public void createTestClubs(@Autowired ClubService clubService,
	                            @Autowired CountryService countryService) {
		var testClubName = "TestClub";
		var countries = new ArrayList<>(countryService.findAll());
		int numberOfClubs = 10;
		for (var i = 1; i <= numberOfClubs; i++) {
			var club = new Club();
			club.setCountry(countries.get(0));
			club.setName(testClubName + i);
			clubService.create(club);
		}
		var clubs = new ArrayList<Club>();
		for (var i = 1; i <= numberOfClubs; i++) {
			var club = clubService.findByName(testClubName + i);
			clubs.add(club);
		}
		assertEquals(numberOfClubs, clubs.size());
	}

	@Test
	@Order(1)
	public void createTestCountry(@Autowired CountryService countryService) {
		var countryISO3Name = "DEU";
		var de = new Country();
		de.setIso3Name(countryISO3Name);
		countryService.create(de);
		assertNotNull(countryService.findByISO3Name(countryISO3Name));
	}

	@Test
	@Order(2)
	public void createTestGender(@Autowired GenderService genderService) {
		var genderName = "m";
		var g = new Gender();
		g.setName(genderName);
		genderService.create(g);
		assertNotNull(genderService.findByName(genderName));
	}

	@Test
	@Order(5)
	public void createTestTeams(@Autowired ClubService clubService,
	                            @Autowired GenderService genderService,
	                            @Autowired TeamService teamService,
	                            @Autowired AgeGroupService ageGroupService) {
		var genders = new ArrayList<>(genderService.findAll());
		var ageGroups = new ArrayList<>(ageGroupService.findAll());
		var clubs = new ArrayList<>(clubService.findAll());
		var testTeams = new ArrayList<Team>();

		int numberOfTeams = 10;
		for (var i = 1; i <= numberOfTeams; i++) {
			var team = new Team();
			team.setAmount(10);
			team.setClub(clubs.get(i - 1));
			team.setGender(genders.get(0));
			team.setAgeGroup(ageGroups.get(0));
			team.setName("TestTeam" + i);
			teamService.create(team);
			testTeams.add(team);
		}

		var counter = (int) teamService.findAll().stream().filter(testTeams::contains).count();

		assertEquals(numberOfTeams, counter);
	}

}
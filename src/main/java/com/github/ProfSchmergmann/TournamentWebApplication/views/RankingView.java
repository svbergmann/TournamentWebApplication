package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroupService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.game.GameService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.GenderService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.TeamService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.ProfSchmergmann.TournamentWebApplication.views.EntityView.notSet;

@AnonymousAllowed
@Route(value = "ranking", layout = MainLayout.class)
public class RankingView extends VerticalLayout implements LocaleChangeObserver, HasDynamicTitle {

	private final Select<AgeGroup> ageGroupSelect;
	private final AgeGroupService ageGroupService;
	private final GameService gameService;
	private final Select<Gender> genderSelect;
	private final GenderService genderService;
	private final TeamService teamService;
	private Grid<Team> grid;

	public RankingView(@Autowired AgeGroupService ageGroupService,
	                   @Autowired GenderService genderService,
	                   @Autowired TeamService teamService,
	                   @Autowired GameService gameService) {
		this.ageGroupService = ageGroupService;
		this.genderService = genderService;
		this.teamService = teamService;
		this.gameService = gameService;
		this.ageGroupSelect = new Select<>();
		this.ageGroupSelect.setLabel(this.getTranslation("age.group"));
		this.ageGroupSelect.setItems(ageGroupService.findAll());
		this.ageGroupSelect.setItemLabelGenerator(AgeGroup::getName);
		this.genderSelect = new Select<>();
		this.genderSelect.setLabel(this.getTranslation("gender"));
		this.genderSelect.setItems(genderService.findAll());
		this.genderSelect.setItemLabelGenerator(Gender::getName);
		this.generateGrid();
	}

	private void generateGrid() {
		this.grid = new Grid<>(Team.class, false);
		this.grid.addColumn(
				    team -> team.getClub() == null || team.getClub().getCountry() == null ?
				            notSet : team.getClub().getCountry().getName(this.getLocale()))
		         .setHeader(this.getTranslation("position"))
		         .setKey("position")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(
				    team -> team.getClub() == null || team.getClub().getCountry() == null ?
				            notSet : team.getClub().getCountry().getName(this.getLocale()))
		         .setHeader(this.getTranslation("country"))
		         .setKey("country")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(team -> team.getClub() == null ? notSet : team.getClub().getName())
		         .setHeader(this.getTranslation("club"))
		         .setKey("club")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(Team::getName)
		         .setHeader(this.getTranslation("name"))
		         .setKey("name")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(Team::getName)
		         .setHeader(this.getTranslation("games.played"))
		         .setKey("games.played")
		         .setSortable(true)
		         .setAutoWidth(true);
	}

	@Override
	public String getPageTitle() {
		return this.getTranslation("ranking" + " | " + this.getTranslation("application.name"));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.ageGroupSelect.setLabel(this.getTranslation("age.group"));
		this.ageGroupSelect.setItems(this.ageGroupService.findAll());
		this.ageGroupSelect.setItemLabelGenerator(AgeGroup::getName);
		UI.getCurrent().getPage().setTitle(this.getPageTitle());
	}
}

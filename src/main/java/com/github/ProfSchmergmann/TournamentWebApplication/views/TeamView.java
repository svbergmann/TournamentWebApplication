package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroupService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.ClubService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.GenderService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.TeamService;
import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Route(value = "teams", layout = MainLayout.class)
public class TeamView extends EntityView<Team> {

	private final AgeGroupService ageGroupService;
	private final ClubService clubService;
	private final GenderService genderService;

	public TeamView(@Autowired ClubService clubService,
	                @Autowired AgeGroupService ageGroupService,
	                @Autowired GenderService genderService,
	                @Autowired TeamService teamService,
	                @Autowired SecurityService securityService) {
		super("team.pl", new Grid<>(), teamService, securityService);
		this.clubService = clubService;
		this.ageGroupService = ageGroupService;
		this.genderService = genderService;
	}

	@Override
	VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
		final Select<Club> clubSelect = new Select<>();
		clubSelect.setLabel(this.getTranslation("club"));
		clubSelect.setItems(this.clubService.findAll());
		clubSelect.setItemLabelGenerator(Club::getName);
		final Select<AgeGroup> ageGroupSelect = new Select<>();
		ageGroupSelect.setLabel(this.getTranslation("age.group"));
		ageGroupSelect.setItems(this.ageGroupService.findAll());
		ageGroupSelect.setItemLabelGenerator(AgeGroup::getName);
		final Select<Gender> genderSelect = new Select<>();
		genderSelect.setLabel(this.getTranslation("gender"));
		genderSelect.setItems(this.genderService.findAll());
		genderSelect.setItemLabelGenerator(Gender::getName);
		final IntegerField amountIntegerField = new IntegerField(this.getTranslation("amount.of.players"));
		amountIntegerField.setMin(5);
		amountIntegerField.setStep(1);
		amountIntegerField.setHasControls(true);
		final TextField teamTextField = new TextField(this.getTranslation("name"));
		final VerticalLayout fields = new VerticalLayout(clubSelect,
		                                                 ageGroupSelect,
		                                                 genderSelect,
		                                                 amountIntegerField,
		                                                 teamTextField);
		fields.setPadding(true);
		addButton.addClickListener(click -> {
			if (teamTextField.getValue().length() > 2) {
				var team = new Team();
				team.setName(teamTextField.getValue());
				team.setClub(clubSelect.getValue());
				team.setAgeGroup(ageGroupSelect.getValue());
				team.setGender(genderSelect.getValue());
				team.setAmount(amountIntegerField.getValue());
				if (this.entityService.findAll().stream().noneMatch(t -> t.equals(team))) {
					this.entityService.create(team);
					this.updateGrid();
				}
				dialog.close();
			}
		});
		return fields;
	}

	@Override
	void setGridColumns() {
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
		this.grid.addColumn(
				    club -> club.getAgeGroup() == null ? notSet : club.getAgeGroup().getName())
		         .setHeader(this.getTranslation("age.group"))
		         .setKey("age")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(
				    club -> club.getGender() == null ? notSet : club.getGender().getName())
		         .setHeader(this.getTranslation("gender"))
		         .setKey("gender")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(Team::getAmount)
		         .setHeader(this.getTranslation("amount.of.players"))
		         .setKey("amount")
		         .setSortable(true)
		         .setAutoWidth(true);
	}

	@Override
	void updateGridColumnHeaders() {
		this.grid.getColumnByKey("country")
		         .setHeader(this.getTranslation("country"));
		this.grid.getColumnByKey("club")
		         .setHeader(this.getTranslation("club"));
		this.grid.getColumnByKey("name")
		         .setHeader(this.getTranslation("name"));
		this.grid.getColumnByKey("age")
		         .setHeader(this.getTranslation("age.group"));
		this.grid.getColumnByKey("gender")
		         .setHeader(this.getTranslation("gender"));
		this.grid.getColumnByKey("amount")
		         .setHeader(this.getTranslation("amount.of.players"));
	}
}


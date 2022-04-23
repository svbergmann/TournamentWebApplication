package com.github.ProfSchmergmann.TournamentWebApplication.views.admin;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroupService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.ClubService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.GenderService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.TeamService;
import com.github.ProfSchmergmann.TournamentWebApplication.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

import static com.github.ProfSchmergmann.TournamentWebApplication.views.admin.LocationView.notSet;

@PermitAll
@Route(value = "teams", layout = MainLayout.class)
@PageTitle("Teams | Tournament")
public class TeamView extends VerticalLayout {

	private final ClubService clubService;
	private final AgeGroupService ageGroupService;
	private final GenderService genderService;
	private final TeamService teamService;
	private Grid<Team> teamGrid;

	public TeamView(@Autowired ClubService clubService,
	                @Autowired AgeGroupService ageGroupService,
	                @Autowired GenderService genderService,
	                @Autowired TeamService teamService
	) {
		this.clubService = clubService;
		this.ageGroupService = ageGroupService;
		this.genderService = genderService;
		this.teamService = teamService;
		this.createTeamGrid();
		var addTeamButton = new Button("Add new Team");
		addTeamButton.addClickListener(click -> this.openTeamDialog());
		this.add(new H2("Teams"),
		         this.teamGrid,
		         addTeamButton);
	}

	private void createTeamGrid() {
		this.teamGrid = new Grid<>(Team.class, false);
		this.teamGrid.addColumn(team ->
				                        team.getClub() == null || team.getClub().getCountry() == null ?
				                        notSet : team.getClub().getCountry().getName())
		             .setHeader("Country")
		             .setSortable(true)
		             .setAutoWidth(true);
		this.teamGrid.addColumn(team -> team.getClub() == null ? notSet : team.getClub().getName())
		             .setHeader("Club Name")
		             .setSortable(true)
		             .setAutoWidth(true);
		this.teamGrid.addColumn(
				    club -> club.getAgeGroup() == null ? notSet : club.getAgeGroup().getName())
		             .setHeader("Age Group")
		             .setSortable(true)
		             .setAutoWidth(true);
		this.teamGrid.addColumn(
				    club -> club.getGender() == null ? notSet : club.getGender().getName())
		             .setHeader("Gender")
		             .setSortable(true)
		             .setAutoWidth(true);
		this.teamGrid.addColumn(Team::getAmount)
		             .setHeader("Amount of Players")
		             .setSortable(true)
		             .setAutoWidth(true);
		this.teamGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		this.updateGrid();
		final GridContextMenu<Team> teamGridContextMenu = this.teamGrid.addContextMenu();
		teamGridContextMenu.addItem("delete", event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem() + "?");
			final Button yesButton = new Button("Yes");
			final Button abortButton = new Button("Abort", e -> dialog.close());
			final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
			dialog.add(buttons);
			yesButton.addClickListener(click -> {
				this.teamService.deleteById(event.getItem().get().getId());
				this.updateGrid();
				dialog.close();
				teamGridContextMenu.close();
			});
			abortButton.addClickListener(click -> {
				dialog.close();
				teamGridContextMenu.close();
			});
			dialog.open();
		});
		teamGridContextMenu.addItem("refactor");
	}

	private void openTeamDialog() {
		final Dialog dialog = new Dialog();
		final Select<Club> clubSelect = new Select<>();
		clubSelect.setLabel("Club");
		clubSelect.setItems(this.clubService.findAll());
		clubSelect.setItemLabelGenerator(Club::getName);
		final Select<AgeGroup> ageGroupSelect = new Select<>();
		ageGroupSelect.setLabel("Age Group");
		ageGroupSelect.setItems(this.ageGroupService.findAll());
		ageGroupSelect.setItemLabelGenerator(AgeGroup::getName);
		final Select<Gender> genderSelect = new Select<>();
		genderSelect.setLabel("Gender");
		genderSelect.setItems(this.genderService.findAll());
		genderSelect.setItemLabelGenerator(Gender::getName);
		final IntegerField amountIntegerField = new IntegerField("Amount of players");
		amountIntegerField.setMin(5);
		amountIntegerField.setStep(1);
		amountIntegerField.setHasControls(true);
		final TextField teamTextField = new TextField("Name");
		final VerticalLayout fields = new VerticalLayout(clubSelect,
		                                                 ageGroupSelect,
		                                                 genderSelect,
		                                                 amountIntegerField,
		                                                 teamTextField);
		fields.setPadding(true);
		final Button addButton = new Button("Add");
		addButton.addClickShortcut(Key.ENTER);
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		buttons.setPadding(true);
		addButton.addClickListener(click -> {
			if (teamTextField.getValue().length() > 3) {
				var team = new Team();
				team.setClub(clubSelect.getValue());
				team.setAgeGroup(ageGroupSelect.getValue());
				team.setGender(genderSelect.getValue());
				team.setAmount(amountIntegerField.getValue());
				if (this.teamService.findAll().stream().noneMatch(t -> t.equals(team))) {
					this.teamService.create(team);
					this.updateGrid();
				}
				dialog.close();
			}
		});
		dialog.add(fields, buttons);
		dialog.open();
	}

	private void updateGrid() {
		this.teamGrid.setItems(this.teamService.findAll());
	}
}


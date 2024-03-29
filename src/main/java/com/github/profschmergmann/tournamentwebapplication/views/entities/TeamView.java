package com.github.profschmergmann.tournamentwebapplication.views.entities;

import com.github.profschmergmann.tournamentwebapplication.database.models.agegroup.AgeGroup;
import com.github.profschmergmann.tournamentwebapplication.database.models.agegroup.AgeGroupService;
import com.github.profschmergmann.tournamentwebapplication.database.models.club.Club;
import com.github.profschmergmann.tournamentwebapplication.database.models.club.ClubService;
import com.github.profschmergmann.tournamentwebapplication.database.models.gender.Gender;
import com.github.profschmergmann.tournamentwebapplication.database.models.gender.GenderService;
import com.github.profschmergmann.tournamentwebapplication.database.models.team.Team;
import com.github.profschmergmann.tournamentwebapplication.database.models.team.TeamService;
import com.github.profschmergmann.tournamentwebapplication.security.SecurityService;
import com.github.profschmergmann.tournamentwebapplication.views.MainLayout;
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

  public TeamView(@Autowired ClubService clubService, @Autowired AgeGroupService ageGroupService,
      @Autowired GenderService genderService, @Autowired TeamService teamService,
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
    final IntegerField amountIntegerField = new IntegerField(
        this.getTranslation("amount.of.players"));
    amountIntegerField.setMin(5);
    amountIntegerField.setStep(1);
    amountIntegerField.setHasControls(true);
    final TextField teamTextField = new TextField(this.getTranslation("name"));
    final VerticalLayout fields = new VerticalLayout(clubSelect, ageGroupSelect, genderSelect,
        amountIntegerField, teamTextField);
    fields.setPadding(true);
    addButton.addClickListener(click -> {
      if (teamTextField.getValue().length() > 2) {
        var team = new Team()
            .setName(teamTextField.getValue())
            .setClub(clubSelect.getValue())
            .setAgeGroup(ageGroupSelect.getValue())
            .setGender(genderSelect.getValue())
            .setAmount(amountIntegerField.getValue());
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
            team -> team.getClub() == null || team.getClub().getCountry() == null ? notSet
                : team.getClub().getCountry().getName(this.getLocale()))
        .setKey("country");
    this.grid.addColumn(team -> team.getClub() == null ? notSet : team.getClub().getName())
        .setKey("club");
    this.grid.addColumn(Team::getName)
        .setKey("name");
    this.grid.addColumn(club -> club.getAgeGroup() == null ? notSet : club.getAgeGroup().getName())
        .setKey("age");
    this.grid.addColumn(club -> club.getGender() == null ? notSet : club.getGender().getName())
        .setKey("gender");
    this.grid.addColumn(Team::getAmount)
        .setKey("amount");
  }
}


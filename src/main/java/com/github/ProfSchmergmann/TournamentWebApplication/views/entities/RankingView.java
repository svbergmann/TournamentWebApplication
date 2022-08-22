package com.github.ProfSchmergmann.TournamentWebApplication.views.entities;

import static com.github.ProfSchmergmann.TournamentWebApplication.views.entities.EntityView.notSet;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroupService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.game.GameService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.GenderService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.match.MatchService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.TeamService;
import com.github.ProfSchmergmann.TournamentWebApplication.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Route(value = "ranking", layout = MainLayout.class)
public class RankingView extends VerticalLayout implements LocaleChangeObserver, HasDynamicTitle {

  private final Select<AgeGroup> ageGroupSelect;
  private final AgeGroupService ageGroupService;
  private final Select<Gender> genderSelect;
  private final GenderService genderService;
  private final Grid<Team> grid;
  private final MatchService matchService;
  private final TeamService teamService;

  public RankingView(@Autowired AgeGroupService ageGroupService,
      @Autowired GenderService genderService,
      @Autowired TeamService teamService,
      @Autowired GameService gameService,
      @Autowired MatchService matchService) {
    this.ageGroupService = ageGroupService;
    this.genderService = genderService;
    this.teamService = teamService;
    this.matchService = matchService;
    this.ageGroupSelect = new Select<>();
    this.genderSelect = new Select<>();
    this.grid = new Grid<>(Team.class, false);
    this.ageGroupSelect.setLabel(this.getTranslation("age.group"));
    this.ageGroupSelect.setItems(this.ageGroupService.findAll());
    this.ageGroupSelect.setItemLabelGenerator(AgeGroup::getName);
    this.genderSelect.setLabel(this.getTranslation("gender"));
    this.genderSelect.setItems(this.genderService.findAll());
    this.genderSelect.setItemLabelGenerator(Gender::getName);
    this.generateGrid();
    this.ageGroupSelect
        .addValueChangeListener(event -> {
              if (!this.genderSelect.isEmpty()) {
                this.grid.setItems(this.teamService
                    .findAll(event.getValue(),
                        this.genderSelect.getValue()));
              }
            }
        );
    this.genderSelect
        .addValueChangeListener(event -> {
              if (!this.ageGroupSelect.isEmpty()) {
                this.grid.setItems(this.teamService
                    .findAll(this.ageGroupSelect.getValue(),
                        event.getValue()));
              }
            }
        );
    this.add(new HorizontalLayout(this.ageGroupSelect, this.genderSelect));
    this.add(this.grid);
  }

  private void generateGrid() {
    this.grid.setColumnReorderingAllowed(true);
    this.grid.setAllRowsVisible(true);
    this.grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    this.grid.addColumn(this.matchService::getPosition)
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
    this.grid.addColumn(team -> this.matchService.findByFinishedAndTeam(true, team).size())
        .setHeader(this.getTranslation("games.played"))
        .setKey("games.played")
        .setSortable(true)
        .setAutoWidth(true);
    this.grid.addColumn(team -> this.matchService.findWonMatchesByTeam(team).size())
        .setHeader("W")
        .setSortable(true)
        .setAutoWidth(true);
    this.grid.addColumn(team -> this.matchService.findLostMatchesByTeam(team).size())
        .setHeader("L")
        .setSortable(true)
        .setAutoWidth(true);
    this.grid.addColumn(this.matchService::getPlusMinus)
        .setHeader("+/-")
        .setSortable(true)
        .setAutoWidth(true);
  }

  @Override
  public String getPageTitle() {
    return this.getTranslation("ranking") + " | " + this.getTranslation("application.name");
  }

  @Override
  public void localeChange(LocaleChangeEvent event) {
    this.ageGroupSelect.setLabel(this.getTranslation("age.group"));
    this.ageGroupSelect.setItems(this.ageGroupService.findAll());
    this.ageGroupSelect.setItemLabelGenerator(AgeGroup::getName);
    this.genderSelect.setLabel(this.getTranslation("gender"));
    this.genderSelect.setItems(this.genderService.findAll());
    this.genderSelect.setItemLabelGenerator(Gender::getName);
    this.grid.getColumnByKey("position").setHeader(this.getTranslation("position"));
    this.grid.getColumnByKey("country").setHeader(this.getTranslation("country"));
    this.grid.getColumnByKey("club").setHeader(this.getTranslation("club"));
    this.grid.getColumnByKey("name").setHeader(this.getTranslation("name"));
    this.grid.getColumnByKey("games.played").setHeader(this.getTranslation("games.played"));
    UI.getCurrent().getPage().setTitle(this.getPageTitle());
  }
}

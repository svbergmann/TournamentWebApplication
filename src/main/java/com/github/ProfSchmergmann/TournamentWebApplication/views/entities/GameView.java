package com.github.profschmergmann.tournamentwebapplication.views.entities;

import com.github.profschmergmann.tournamentwebapplication.database.models.game.Game;
import com.github.profschmergmann.tournamentwebapplication.database.models.game.GameService;
import com.github.profschmergmann.tournamentwebapplication.database.models.match.MatchService;
import com.github.profschmergmann.tournamentwebapplication.security.SecurityService;
import com.github.profschmergmann.tournamentwebapplication.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Route(value = "games", layout = MainLayout.class)
public class GameView extends EntityView<Game> {

  private MatchService matchService;

  public GameView(@Autowired GameService gameService,
      @Autowired MatchService matchService,
      @Autowired SecurityService securityService) {
    super("game.pl", new Grid<>(), gameService, securityService);
    if (this.securityService.getAuthenticatedUser() != null) {
      this.addButton.setVisible(false);
      this.matchService = matchService;

      this.grid.addItemDoubleClickListener(event -> {
        final IntegerField teamaField = new IntegerField(
            this.getTranslation("score") + " " + this.getTranslation("home.team"));
        teamaField.setStep(1);
        teamaField.setMin(0);
        teamaField.setValue(event.getItem().getMatch().getScoreTeamA());
        final IntegerField teambfield = new IntegerField(
            this.getTranslation("score") + " " + this.getTranslation("away.team"));
        teambfield.setStep(1);
        teambfield.setMin(0);
        teambfield.setValue(event.getItem().getMatch().getScoreTeamB());
        final Label colons = new Label(":");

        final Dialog dialog = new Dialog();

        final Button confirmButton = new Button(this.getTranslation("confirm"), e -> {
          event.getItem().getMatch().setFinished(true);
          event.getItem().getMatch().setScoreTeamA(teamaField.getValue());
          event.getItem().getMatch().setScoreTeamB(teambfield.getValue());
          this.matchService.update(event.getItem().getMatch(), event.getItem().getMatch().getId());
          this.updateGridItems();
          dialog.close();
        });
        confirmButton.addClickShortcut(Key.ENTER);

        final Button abortButton =
            new Button(this.getTranslation("abort"), e -> dialog.close());

        final HorizontalLayout hLayout1 = new HorizontalLayout(teamaField, colons, teambfield);
        final HorizontalLayout hLayout2 = new HorizontalLayout(confirmButton, abortButton);

        dialog.add(hLayout1, hLayout2);
        dialog.open();
      });
    }
  }

  @Override
  VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
    return null;
  }

  @Override
  void setGridColumns() {
    this.grid.addColumn(game -> game.getMatch() == null ? notSet
            : game.getMatch().getTeamA().getAgeGroup().getName())
        .setKey("age.group");

    this.grid.addColumn(game -> game.getMatch() == null
            ? notSet : game.getMatch().getTeamA().getGender().getName())
        .setKey("gender");

    this.grid.addColumn(game -> game.getGym() == null ? notSet : game.getGym().getName())
        .setKey("gym");

    this.grid.addColumn(Game::getDate)
        .setKey("date");

    this.grid.addColumn(
            game -> game.getMatch() == null ? notSet : game.getMatch().getTeamA().getName())
        .setKey("home.team");

    this.grid.addColumn(
            game -> game.getMatch() != null ? game.getMatch().getScoreTeamA() + " : " + game.getMatch()
                .getScoreTeamB() : notSet)
        .setKey("score");

    this.grid.addColumn(
            game -> game.getMatch() == null ? notSet : game.getMatch().getTeamB().getName())
        .setKey("away.team");
  }
}

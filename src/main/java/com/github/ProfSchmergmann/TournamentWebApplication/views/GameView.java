package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.game.Game;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.game.GameService;
import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Route(value = "games", layout = MainLayout.class)
public class GameView extends EntityView<Game> {

	public GameView(@Autowired GameService gameService,
	                @Autowired SecurityService securityService) {
		super("game.pl", new Grid<>(), gameService, securityService);
		if (this.securityService.getAuthenticatedUser() != null) {
			this.addButton.setEnabled(false);
		}
	}

	@Override
	VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
		return null;
	}

	@Override
	void setGridColumns() {
		this.grid.addColumn(game -> game.getMatch() == null ? notSet : game.getMatch().getTeamA().getAgeGroup().getName())
		         .setKey("age.group")
		         .setHeader(this.getTranslation("age.group"))
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(game -> game.getMatch() == null ? notSet : game.getMatch().getTeamA().getGender().getName())
		         .setKey("gender")
		         .setHeader(this.getTranslation("gender"))
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(game -> game.getGym() == null ? notSet : game.getGym().getName())
		         .setKey("gym")
		         .setHeader(this.getTranslation("gym"))
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(Game::getDate)
		         .setKey("date")
		         .setHeader(this.getTranslation("date"))
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(game -> game.getMatch() == null ? notSet : game.getMatch().getTeamA().getName())
		         .setKey("home.team")
		         .setHeader(this.getTranslation("home.team"))
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(game -> game.getMatch() != null ?
		                            game.getMatch().getScoreTeamA() +
				                            " : " +
				                            game.getMatch().getScoreTeamB() :
		                            notSet)
		         .setKey("score")
		         .setHeader(this.getTranslation("score"))
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(game -> game.getMatch() == null ? notSet : game.getMatch().getTeamB().getName())
		         .setKey("away.team")
		         .setHeader(this.getTranslation("away.team"))
		         .setSortable(true)
		         .setAutoWidth(true);
	}

	@Override
	void updateGridColumnHeaders() {
		this.grid.getColumnByKey("age.group").setHeader(this.getTranslation("age.group"));
		this.grid.getColumnByKey("gender").setHeader(this.getTranslation("gender"));
		this.grid.getColumnByKey("gym").setHeader(this.getTranslation("gym"));
		this.grid.getColumnByKey("date").setHeader(this.getTranslation("date"));
		this.grid.getColumnByKey("home.team").setHeader(this.getTranslation("home.team"));
		this.grid.getColumnByKey("score").setHeader(this.getTranslation("score"));
		this.grid.getColumnByKey("away.team").setHeader(this.getTranslation("away.team"));
	}
}

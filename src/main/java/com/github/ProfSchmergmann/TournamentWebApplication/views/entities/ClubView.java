package com.github.ProfSchmergmann.TournamentWebApplication.views.entities;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.ClubService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.github.ProfSchmergmann.TournamentWebApplication.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "clubs", layout = MainLayout.class)
public class ClubView extends EntityView<Club> {

  private final CountryService countryService;

  public ClubView(@Autowired CountryService countryService,
      @Autowired ClubService clubService,
      @Autowired SecurityService securityService) {
    super("club.pl", new Grid<>(), clubService, securityService);
    this.countryService = countryService;
  }

  @Override
  VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
    final Select<Country> countrySelect = new Select<>();
    countrySelect.setLabel(this.getTranslation("country"));
    countrySelect.setItems(this.countryService.findAll());
    countrySelect.setItemLabelGenerator(c -> c.getName(this.getLocale()));
    final TextField clubTextField = new TextField(this.getTranslation("name"));
    final VerticalLayout fields = new VerticalLayout(countrySelect, clubTextField);
    fields.setPadding(true);
    addButton.addClickListener(click -> {
      if (clubTextField.getValue().length() > 1) {
        var club = new Club();
        club.setCountry(countrySelect.getValue());
        club.setName(clubTextField.getValue());
        if (this.entityService.findAll().stream().noneMatch(c -> c.equals(club))) {
          this.entityService.create(club);
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
            club -> club.getCountry() == null ? notSet : club.getCountry().getName(this.getLocale()))
        .setHeader(this.getTranslation("country"))
        .setKey("country")
        .setSortable(true)
        .setAutoWidth(true);
    this.grid.addColumn(Club::getName)
        .setHeader(this.getTranslation("name"))
        .setKey("name")
        .setSortable(true)
        .setAutoWidth(true);
  }

  @Override
  void updateGridColumnHeaders() {
    this.grid.getColumnByKey("country")
        .setHeader(this.getTranslation("country"));
    this.grid.getColumnByKey("name")
        .setHeader(this.getTranslation("name"));
  }
}
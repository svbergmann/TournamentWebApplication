package com.github.ProfSchmergmann.TournamentWebApplication.views.entities;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.Gym;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.GymService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.Location;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.LocationService;
import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.github.ProfSchmergmann.TournamentWebApplication.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@AnonymousAllowed
@Route(value = "", layout = MainLayout.class)
public class GymView extends EntityView<Gym> {

  private final LocationService locationService;

  public GymView(@Autowired GymService gymService,
      @Autowired LocationService locationService,
      @Autowired SecurityService securityService) {
    super("gym.pl", new Grid<>(), gymService, securityService);
    this.locationService = locationService;
  }

  @Override
  VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
    final Select<Location> locationSelect = new Select<>();
    locationSelect.setLabel(this.getTranslation("location"));
    locationSelect.setItems(this.locationService.findAll());
    locationSelect.setItemLabelGenerator(l ->
        l.getCity().getCountry().getName(this.getLocale()) + ", "
            + l.getPostalCode() + ", "
            + l.getCity().getName() + ", "
            + l.getStreet().getName() + ", "
            + l.getNumber());
    final Label gymLabel = new Label(this.getTranslation("gym"));
    final IntegerField gymIntegerField = new IntegerField(this.getTranslation("gym") +
        " " +
        this.getTranslation("number"));
    gymIntegerField.setMin(1);
    final IntegerField gymCapacityField = new IntegerField(this.getTranslation("gym") +
        " " +
        this.getTranslation("capacity"));
    gymCapacityField.setMin(0);
    final HorizontalLayout gymLayout = new HorizontalLayout(gymLabel,
        gymIntegerField,
        gymCapacityField);
    gymLayout.setAlignItems(Alignment.CENTER);
    final VerticalLayout fields = new VerticalLayout(locationSelect, gymLayout);
    fields.setPadding(true);
    addButton.addClickListener(click -> {
      var gym = new Gym();
      gym.setLocation(locationSelect.getValue());
      gym.setNumber(gymIntegerField.getValue());
      gym.setName(gymLabel.getText() + " " + gym.getNumber());
      if (this.entityService.findAll().stream().noneMatch(g -> g.getNumber() == gym.getNumber())) {
        this.entityService.create(gym);
        this.updateGrid();
      }
      dialog.close();
    });
    return fields;
  }

  @Override
  void setGridColumns() {
    this.grid.addColumn(Gym::getName)
        .setHeader(this.getTranslation("name"))
        .setKey("name")
        .setSortable(true)
        .setAutoWidth(true);
    this.grid.addColumn(gym -> this.getTranslation("gym") + gym.getNumber())
        .setHeader(this.getTranslation("gym"))
        .setKey("gym")
        .setSortable(true)
        .setAutoWidth(true);
    this.grid.addColumn(gym -> gym.getLocation() == null ?
            notSet : gym.getLocation().getPostalCode())
        .setHeader(this.getTranslation("plz"))
        .setKey("plz")
        .setSortable(true)
        .setAutoWidth(true);
    this.grid.addColumn(gym -> gym.getLocation() == null || gym.getLocation().getCity() == null ?
            notSet : gym.getLocation().getCity().getName())
        .setHeader(this.getTranslation("city"))
        .setKey("city")
        .setSortable(true)
        .setAutoWidth(true);
    this.grid.addColumn(
            gym -> gym.getLocation() == null || gym.getLocation().getStreet() == null ?
                notSet : gym.getLocation().getStreet().getName())
        .setHeader(this.getTranslation("street"))
        .setKey("street")
        .setSortable(true)
        .setAutoWidth(true);
    this.grid.addColumn(gym -> gym.getLocation() == null ?
            notSet : gym.getLocation().getNumber())
        .setHeader(this.getTranslation("number"))
        .setKey("number")
        .setSortable(true)
        .setAutoWidth(true);
  }

  @Override
  void updateGridColumnHeaders() {
    this.grid.getColumnByKey("name")
        .setHeader(this.getTranslation("name"));
    this.grid.getColumnByKey("gym")
        .setHeader(this.getTranslation("gym"));
    this.grid.getColumnByKey("plz")
        .setHeader(this.getTranslation("plz"));
    this.grid.getColumnByKey("city")
        .setHeader(this.getTranslation("city"));
    this.grid.getColumnByKey("street")
        .setHeader(this.getTranslation("street"));
    this.grid.getColumnByKey("number")
        .setHeader(this.getTranslation("number"));
  }
}

package com.github.profschmergmann.tournamentwebapplication.views.entities;

import com.github.profschmergmann.tournamentwebapplication.database.models.gym.Gym;
import com.github.profschmergmann.tournamentwebapplication.database.models.gym.GymService;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.Location;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.LocationService;
import com.github.profschmergmann.tournamentwebapplication.security.SecurityService;
import com.github.profschmergmann.tournamentwebapplication.views.MainLayout;
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

  public GymView(@Autowired GymService gymService, @Autowired LocationService locationService,
      @Autowired SecurityService securityService) {
    super("gym.pl", new Grid<>(), gymService, securityService);
    this.locationService = locationService;
  }

  @Override
  VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
    final Select<Location> locationSelect = new Select<>();
    locationSelect.setLabel(this.getTranslation("location"));
    locationSelect.setItems(this.locationService.findAll());
    locationSelect.setItemLabelGenerator(
        l -> l.getCity().getCountry().getName(this.getLocale()) + ", " + l.getPostalCode() + ", "
            + l.getCity().getName() + ", " + l.getStreet().getName() + ", " + l.getNumber());
    final Label gymLabel = new Label(this.getTranslation("gym"));
    final IntegerField gymIntegerField = new IntegerField(
        this.getTranslation("gym") + " " + this.getTranslation("number"));
    gymIntegerField.setMin(1);
    final IntegerField gymCapacityField = new IntegerField(
        this.getTranslation("gym") + " " + this.getTranslation("capacity"));
    gymCapacityField.setMin(0);
    final HorizontalLayout gymLayout = new HorizontalLayout(gymLabel, gymIntegerField,
        gymCapacityField);
    gymLayout.setAlignItems(Alignment.CENTER);
    final VerticalLayout fields = new VerticalLayout(locationSelect, gymLayout);
    fields.setPadding(true);
    addButton.addClickListener(click -> {
      var gym = new Gym()
          .setLocation(locationSelect.getValue())
          .setNumber(gymIntegerField.getValue())
          .setName(gymLabel.getText() + " " + gymIntegerField.getValue());
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
        .setKey("name");
    this.grid.addColumn(gym -> this.getTranslation("gym") + gym.getNumber())
        .setKey("gym");
    this.grid.addColumn(
            gym -> gym.getLocation() == null ? notSet : gym.getLocation().getPostalCode())
        .setKey("plz");
    this.grid.addColumn(
            gym -> gym.getLocation() == null || gym.getLocation().getCity() == null ? notSet
                : gym.getLocation().getCity().getName())
        .setKey("city");
    this.grid.addColumn(
            gym -> gym.getLocation() == null || gym.getLocation().getStreet() == null ? notSet
                : gym.getLocation().getStreet().getName())
        .setKey("street");
    this.grid.addColumn(gym -> gym.getLocation() == null ? notSet : gym.getLocation().getNumber())
        .setKey("number");
  }
}

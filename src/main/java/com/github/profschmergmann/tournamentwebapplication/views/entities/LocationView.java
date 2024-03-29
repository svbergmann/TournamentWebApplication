package com.github.profschmergmann.tournamentwebapplication.views.entities;

import com.github.profschmergmann.tournamentwebapplication.database.models.location.Location;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.LocationService;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.city.City;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.city.CityService;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.country.Country;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.country.CountryService;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.street.Street;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.street.StreetService;
import com.github.profschmergmann.tournamentwebapplication.security.SecurityService;
import com.github.profschmergmann.tournamentwebapplication.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "locations", layout = MainLayout.class)
public class LocationView extends EntityView<Location> {

  private final CityService cityService;
  private final CountryService countryService;
  private final StreetService streetService;

  public LocationView(@Autowired CountryService countryService, @Autowired CityService cityService,
      @Autowired StreetService streetService, @Autowired LocationService locationService,
      @Autowired SecurityService securityService) {
    super("location.pl", new Grid<>(), locationService, securityService);
    this.countryService = countryService;
    this.cityService = cityService;
    this.streetService = streetService;
  }

  @Override
  VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
    final Select<Country> countrySelect = new Select<>();
    countrySelect.setLabel("Country");
    countrySelect.setItems(this.countryService.findAll());
    countrySelect.setItemLabelGenerator(c -> c.getName(this.getLocale()));
    final Select<City> citySelect = new Select<>();
    citySelect.setLabel("City");
    citySelect.setItemLabelGenerator(City::getName);
    final Select<Street> streetSelect = new Select<>();
    streetSelect.setLabel("Street");
    streetSelect.setItemLabelGenerator(Street::getName);
    final IntegerField postalCodeNumberField = new IntegerField("Postal Code");
    postalCodeNumberField.setHelperText("Only Numbers");
    postalCodeNumberField.setMin(0);
    final IntegerField numberIntegerField = new IntegerField("Number");
    numberIntegerField.setHelperText("Only Numbers");
    numberIntegerField.setMin(0);
    final VerticalLayout fields = new VerticalLayout(countrySelect, citySelect, streetSelect,
        postalCodeNumberField, numberIntegerField);
    fields.setPadding(true);
    addButton.addClickListener(click -> {
      var location = new Location()
          .setCity(citySelect.getValue())
          .setStreet(streetSelect.getValue())
          .setPostalCode(postalCodeNumberField.getValue())
          .setNumber(numberIntegerField.getValue());
      if (this.entityService.findAll().stream().noneMatch(l -> l.equals(location))) {
        this.entityService.create(location);
        this.updateGrid();
      }
      dialog.close();
    });
    countrySelect.addValueChangeListener(event -> {
      citySelect.setItems(this.cityService.findAllFromCountry(event.getValue()));
      postalCodeNumberField.setValue(0);
      numberIntegerField.setValue(0);
    });
    citySelect.addValueChangeListener(event -> {
      streetSelect.setItems(this.streetService.findAll());
      postalCodeNumberField.setValue(0);
      numberIntegerField.setValue(0);
    });
    return fields;
  }

  @Override
  void setGridColumns() {
    this.grid.addColumn(l -> l.getCity() == null || l.getCity().getCountry() == null ? notSet
            : l.getCity().getCountry().getName(this.getLocale()))
        .setKey("country");
    this.grid.addColumn(Location::getPostalCode)
        .setKey("plz");
    this.grid.addColumn(l -> l.getCity() == null ? notSet : l.getCity().getName())
        .setKey("city");
    this.grid.addColumn(l -> l.getStreet() == null ? notSet : l.getStreet().getName())
        .setKey("street");
    this.grid.addColumn(Location::getNumber)
        .setKey("number");
  }
}

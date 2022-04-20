package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.Location;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.LocationService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street.StreetService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "locations")
public class LocationView extends VerticalLayout {

	private final CountryService countryService;
	private final CityService cityService;
	private final StreetService streetService;
	private final LocationService locationService;

	private final Grid<Location> locationGrid;

	public LocationView(@Autowired CountryService countryService, @Autowired CityService cityService,
			@Autowired StreetService streetService, @Autowired LocationService locationService) {
		this.countryService = countryService;
		this.cityService = cityService;
		this.streetService = streetService;
		this.locationService = locationService;
		this.locationGrid = new Grid<>();
	}

	private City getCityFromDialog() {
		final Dialog dialog = new Dialog();
		final ComboBox<Country> countryComboBox = new ComboBox<>("Countries");
		countryComboBox.setItems(this.countryService.findAll());
		countryComboBox.setItemLabelGenerator(Country::getName);
		final TextField cityTextField = new TextField("City");
		final Button addButton = new Button("Add");
		final Button abortButton = new Button("Abort");
		final HorizontalLayout h = new HorizontalLayout(addButton, abortButton);
		dialog.add(countryComboBox, cityTextField, h);
		addButton.addClickListener(click -> {
			if (countryComboBox.)
		})
	}

}

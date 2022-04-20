package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.Location;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.LocationService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street.StreetService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
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
		this.locationGrid.setItems(this.locationService.findAll());
		var addCountryButton = new Button("Add new Country");
		addCountryButton.addClickListener(click -> this.openCountryDialog());
		var addCityButton = new Button("Add new City");
		addCityButton.addClickListener(click -> this.openCityDialog());
		var horizontalLayout = new HorizontalLayout(addCountryButton, addCityButton);
		this.add(horizontalLayout, this.locationGrid);
	}

	private void openCityDialog() {
		final Dialog dialog = new Dialog();
		final Select<Country> countrySelect = new Select<>();
		countrySelect.setLabel("Country");
		countrySelect.setItems(this.countryService.findAll());
		countrySelect.setItemLabelGenerator(Country::getName);
		final TextField cityTextField = new TextField("City");
		final Button addButton = new Button("Add");
		final Button abortButton = new Button("Abort");
		final HorizontalLayout h = new HorizontalLayout(addButton, abortButton);
		dialog.add(countrySelect, cityTextField, h);
		addButton.addClickListener(click -> {
			if (cityTextField.getValue().trim().length() > 3) {
				var city = new City();
				city.setName(cityTextField.getValue().trim());
				city.setCountry(this.countryService.findByName(countrySelect.getValue().getName()));
				if (this.cityService.findAll().stream().noneMatch(c -> c.equals(city))) {
					this.cityService.create(city);
				}
				dialog.close();
			}
		});
		dialog.open();
	}

	private void openCountryDialog() {
		final Dialog dialog = new Dialog();
		final TextField countryTextField = new TextField("City");
		final Button addButton = new Button("Add");
		final Button abortButton = new Button("Abort");
		final HorizontalLayout h = new HorizontalLayout(addButton, abortButton);
		dialog.add(countryTextField, h);
		addButton.addClickListener(click -> {
			if (countryTextField.getValue().trim().length() > 3) {
				var country = new Country();
				country.setName(countryTextField.getValue().trim());
				if (this.countryService.findAll().stream().noneMatch(c -> c.equals(country))) {
					this.countryService.create(country);
				}
				dialog.close();
			}
		});
		dialog.open();
	}

}

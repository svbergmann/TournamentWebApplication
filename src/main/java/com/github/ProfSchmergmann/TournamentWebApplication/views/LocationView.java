package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.Location;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.LocationService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street.Street;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street.StreetService;
import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "locations", layout = MainLayout.class)
public class LocationView extends EntityView<Location> {

	private final CityService cityService;
	private final CountryService countryService;
	private final StreetService streetService;

	public LocationView(@Autowired CountryService countryService,
	                    @Autowired CityService cityService,
	                    @Autowired StreetService streetService,
	                    @Autowired LocationService locationService,
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
		final VerticalLayout fields = new VerticalLayout(countrySelect,
		                                                 citySelect,
		                                                 streetSelect,
		                                                 postalCodeNumberField,
		                                                 numberIntegerField);
		fields.setPadding(true);
		addButton.addClickListener(click -> {
			var location = new Location();
			location.setCountry(countrySelect.getValue());
			location.setCity(citySelect.getValue());
			location.setStreet(streetSelect.getValue());
			location.setPostalCode(postalCodeNumberField.getValue());
			location.setNumber(numberIntegerField.getValue());
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
		this.grid.addColumn(
				    l -> l.getCity() == null || l.getCity().getCountry() == null ?
				         notSet : l.getCity().getCountry().getName(this.getLocale()))
		         .setHeader(this.getTranslation("country"))
		         .setKey("country")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(Location::getPostalCode)
		         .setHeader(this.getTranslation("plz"))
		         .setKey("plz")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(l -> l.getCity() == null ? notSet : l.getCity().getName())
		         .setHeader(this.getTranslation("city"))
		         .setKey("city")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(l -> l.getStreet() == null ? notSet : l.getStreet().getName())
		         .setHeader(this.getTranslation("street"))
		         .setKey("street")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(Location::getNumber)
		         .setHeader(this.getTranslation("number"))
		         .setKey("number")
		         .setSortable(true)
		         .setAutoWidth(true);
	}

	@Override
	void updateGridColumnHeaders() {
		this.grid.getColumnByKey("country")
		         .setHeader(this.getTranslation("country"));
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

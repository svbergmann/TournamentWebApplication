package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.Location;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.LocationService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street.Street;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street.StreetService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "locations", layout = MainLayout.class)
@PageTitle("Locations | Tournament")
public class LocationView extends VerticalLayout {

	public static final String notSet = "not set";
	private final CountryService countryService;
	private final CityService cityService;
	private final StreetService streetService;
	private final LocationService locationService;
	private Grid<Location> locationGrid;

	public LocationView(@Autowired CountryService countryService, @Autowired CityService cityService,
	                    @Autowired StreetService streetService, @Autowired LocationService locationService) {
		this.countryService = countryService;
		this.cityService = cityService;
		this.streetService = streetService;
		this.locationService = locationService;
		this.createLocationGrid();
		var addLocationButton = new Button("Add new Location");
		addLocationButton.addClickListener(click -> this.openLocationDialog());
		this.add(new H2("Locations"),
		         this.locationGrid,
		         addLocationButton);
	}

	private void createLocationGrid() {
		this.locationGrid = new Grid<>(Location.class, false);
		this.locationGrid.addColumn(
				    l -> l.getCity() == null || l.getCity().getCountry() == null ?
				         notSet : l.getCity().getCountry().getName(this.getLocale()))
		                 .setHeader("Country")
		                 .setSortable(true)
		                 .setAutoWidth(true);
		this.locationGrid.addColumn(Location::getPostalCode)
		                 .setHeader("Postal Code");
		this.locationGrid.addColumn(l -> l.getCity() == null ? notSet : l.getCity().getName())
		                 .setHeader("City")
		                 .setSortable(true)
		                 .setAutoWidth(true);
		this.locationGrid.addColumn(l -> l.getStreet() == null ? notSet : l.getStreet().getName())
		                 .setHeader("Street")
		                 .setSortable(true)
		                 .setAutoWidth(true);
		this.locationGrid.addColumn(Location::getNumber)
		                 .setHeader("Number")
		                 .setSortable(true)
		                 .setAutoWidth(true);
		this.locationGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		this.updateGrid();
		final GridContextMenu<Location> locationGridContextMenu = this.locationGrid.addContextMenu();
		locationGridContextMenu.addItem("delete", event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem() + "?");
			final Button yesButton = new Button("Yes");
			final Button abortButton = new Button("Abort", e -> dialog.close());
			final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
			dialog.add(buttons);
			yesButton.addClickListener(click -> {
				this.locationService.deleteById(event.getItem().get().getId());
				this.updateGrid();
				dialog.close();
				locationGridContextMenu.close();
			});
			abortButton.addClickListener(click -> {
				dialog.close();
				locationGridContextMenu.close();
			});
			dialog.open();
		});
		locationGridContextMenu.addItem("refactor");
	}

	private void openLocationDialog() {
		final Dialog dialog = new Dialog();
		final Select<Country> countrySelect = new Select<>();
		countrySelect.setLabel("Country");
		countrySelect.setItems(this.countryService.findAll());
		countrySelect.setItemLabelGenerator(c-> c.getName(this.getLocale()));
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
		final Button addButton = new Button("Add");
		addButton.addClickShortcut(Key.ENTER);
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		addButton.addClickListener(click -> {
			var location = new Location();
			location.setCountry(countrySelect.getValue());
			location.setCity(citySelect.getValue());
			location.setStreet(streetSelect.getValue());
			location.setPostalCode(postalCodeNumberField.getValue());
			location.setNumber(numberIntegerField.getValue());
			if (this.locationService.findAll().stream().noneMatch(l -> l.equals(location))) {
				this.locationService.create(location);
				this.updateGrid();
			}
			dialog.close();
		});
		abortButton.addClickListener(click -> dialog.close());
		countrySelect.addValueChangeListener(event -> {
			this.locationGrid.setItems(this.locationService.findAll());
			citySelect.setItems(this.cityService.findAllFromCountry(event.getValue()));
			postalCodeNumberField.setValue(0);
			numberIntegerField.setValue(0);
		});
		citySelect.addValueChangeListener(event -> {
			streetSelect.setItems(this.streetService.findAllFromCity(event.getValue()));
			postalCodeNumberField.setValue(0);
			numberIntegerField.setValue(0);
		});
		dialog.add(fields, buttons);
		dialog.open();
	}

	private void updateGrid() {
		this.locationGrid.setItems(this.locationService.findAll());
	}
}

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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "locations")
public class LocationView extends VerticalLayout {

	public static final String notSet = "not set";
	private final CountryService countryService;
	private final CityService cityService;
	private final StreetService streetService;
	private final LocationService locationService;
	private Button addCountryButton;
	private Button addCityButton;
	private Button addStreetButton;
	private Button addLocationButton;
	private Grid<Location> locationGrid;
	private Grid<City> cityGrid;
	private Grid<Country> countryGrid;
	private Grid<Street> streetGrid;

	public LocationView(@Autowired CountryService countryService, @Autowired CityService cityService,
			@Autowired StreetService streetService, @Autowired LocationService locationService) {
		this.countryService = countryService;
		this.cityService = cityService;
		this.streetService = streetService;
		this.locationService = locationService;
		this.createGrids();
		this.createButtons();
		this.add(new H2("Locations"),
				this.locationGrid,
				this.addLocationButton,
				new H2("Countries"),
				this.countryGrid,
				this.addCountryButton,
				new H2("Cities"),
				this.cityGrid,
				this.addCityButton,
				new H2("Streets"),
				this.streetGrid,
				this.addStreetButton);
		this.updateGrids();
	}

	private void createButtons() {
		this.addCountryButton = new Button("Add new Country");
		this.addCountryButton.addClickListener(click -> this.openCountryDialog());
		this.addCountryButton.addClickShortcut(Key.ENTER);
		this.addCityButton = new Button("Add new City");
		this.addCityButton.addClickListener(click -> this.openCityDialog());
		this.addCityButton.addClickShortcut(Key.ENTER);
		this.addStreetButton = new Button("Add new Street");
		this.addStreetButton.addClickListener(click -> this.openStreetDialog());
		this.addStreetButton.addClickShortcut(Key.ENTER);
		this.addLocationButton = new Button("Add new Location");
		this.addLocationButton.addClickListener(click -> this.openLocationDialog());
		this.addLocationButton.addClickShortcut(Key.ENTER);
	}

	private void createCityGrid() {
		this.cityGrid = new Grid<>(City.class, false);
		this.cityGrid.addColumn(c -> c.getCountry() == null ?
						notSet : c.getCountry().getName())
				.setHeader("Country")
				.setSortable(true)
				.setAutoWidth(true);
		this.cityGrid.addColumn(City::getName)
				.setHeader("Name")
				.setSortable(true)
				.setAutoWidth(true);
		this.cityGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		final GridContextMenu<City> cityGridContextMenu = this.cityGrid.addContextMenu();
		cityGridContextMenu.addItem("delete", event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem() + "?");
			final Button yesButton = new Button("Yes");
			final Button abortButton = new Button("Abort", e -> dialog.close());
			final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
			dialog.add(buttons);
			yesButton.addClickListener(click -> {
				this.cityService.deleteById(event.getItem().get().getId());
				dialog.close();
				cityGridContextMenu.close();
			});
			abortButton.addClickListener(click -> {
				dialog.close();
				cityGridContextMenu.close();
			});
			dialog.open();
		});
		cityGridContextMenu.addItem("refactor");
	}

	private void createCountryGrid() {
		this.countryGrid = new Grid<>(Country.class, false);
		this.countryGrid.addColumn(Country::getName)
				.setHeader("Name")
				.setSortable(true)
				.setAutoWidth(true);
		this.countryGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		final GridContextMenu<Country> countryGridContextMenu = this.countryGrid.addContextMenu();
		countryGridContextMenu.addItem("delete", event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem() + "?");
			final Button yesButton = new Button("Yes");
			final Button abortButton = new Button("Abort", e -> dialog.close());
			final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
			dialog.add(buttons);
			yesButton.addClickListener(click -> {
				this.countryService.deleteById(event.getItem().get().getId());
				dialog.close();
				countryGridContextMenu.close();
			});
			abortButton.addClickListener(click -> {
				dialog.close();
				countryGridContextMenu.close();
			});
			dialog.open();
		});
		countryGridContextMenu.addItem("refactor");
	}

	private void createGrids() {
		this.createLocationGrid();
		this.createCountryGrid();
		this.createCityGrid();
		this.createStreetGrid();
	}

	private void createLocationGrid() {
		this.locationGrid = new Grid<>(Location.class, false);
		this.locationGrid.addColumn(
						l -> l.getCity() == null || l.getCity().getCountry() == null ?
								notSet
								: l.getCity().getCountry().getName())
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

	private void createStreetGrid() {
		this.streetGrid = new Grid<>(Street.class, false);
		this.streetGrid.addColumn(
						s -> s.getCity() == null || s.getCity().getCountry() == null ?
								notSet
								: s.getCity().getCountry().getName())
				.setHeader("Country")
				.setSortable(true)
				.setAutoWidth(true);
		this.streetGrid.addColumn(s -> s.getCity() == null ?
						notSet : s.getCity().getName())
				.setHeader("City")
				.setSortable(true)
				.setAutoWidth(true);
		this.streetGrid.addColumn(Street::getName)
				.setHeader("Name")
				.setSortable(true)
				.setAutoWidth(true);
		this.streetGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		final GridContextMenu<Street> streetGridContextMenu = this.streetGrid.addContextMenu();
		streetGridContextMenu.addItem("delete", event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem() + "?");
			final Button yesButton = new Button("Yes");
			final Button abortButton = new Button("Abort", e -> dialog.close());
			final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
			dialog.add(buttons);
			yesButton.addClickListener(click -> {
				this.streetService.deleteById(event.getItem().get().getId());
				dialog.close();
				streetGridContextMenu.close();
			});
			abortButton.addClickListener(click -> {
				dialog.close();
				streetGridContextMenu.close();
			});
			dialog.open();
		});
		streetGridContextMenu.addItem("refactor");
	}

	private void openCityDialog() {
		final Dialog dialog = new Dialog();
		final Select<Country> countrySelect = new Select<>();
		countrySelect.setLabel("Country");
		countrySelect.setItems(this.countryService.findAll());
		countrySelect.setItemLabelGenerator(Country::getName);
		final TextField cityTextField = new TextField("City");
		final VerticalLayout fields = new VerticalLayout(countrySelect, cityTextField);
		fields.setPadding(true);
		final Button addButton = new Button("Add");
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		buttons.setPadding(true);
		addButton.addClickListener(click -> {
			if (cityTextField.getValue().trim().length() > 3) {
				var city = new City();
				city.setName(cityTextField.getValue().trim());
				city.setCountry(this.countryService.findByName(countrySelect.getValue().getName()));
				if (this.cityService.findAll().stream().noneMatch(c -> c.equals(city))) {
					this.cityService.create(city);
					this.updateGrids();
				}
				dialog.close();
			}
		});
		dialog.add(fields, buttons);
		dialog.open();
	}

	private void openCountryDialog() {
		final Dialog dialog = new Dialog();
		final TextField countryTextField = new TextField("Country");
		final Button addButton = new Button("Add");
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		dialog.add(countryTextField, buttons);
		addButton.addClickListener(click -> {
			if (countryTextField.getValue().trim().length() > 3) {
				var country = new Country();
				country.setName(countryTextField.getValue().trim());
				if (this.countryService.findAll().stream().noneMatch(c -> c.equals(country))) {
					this.countryService.create(country);
					this.updateGrids();
				}
				dialog.close();
			}
		});
		abortButton.addClickListener(click -> dialog.close());
		dialog.open();
	}

	private void openLocationDialog() {
		final Dialog dialog = new Dialog();
		final Select<Country> countrySelect = new Select<>();
		countrySelect.setLabel("Country");
		countrySelect.setItems(this.countryService.findAll());
		countrySelect.setItemLabelGenerator(Country::getName);
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
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		addButton.addClickListener(click -> {
			var locationDB = new Location();
			locationDB.setCountry(countrySelect.getValue());
			locationDB.setCity(citySelect.getValue());
			locationDB.setStreet(streetSelect.getValue());
			locationDB.setPostalCode(postalCodeNumberField.getValue());
			locationDB.setNumber(numberIntegerField.getValue());
			if (this.locationService.findAll().stream().noneMatch(l -> l.equals(locationDB))) {
				this.locationService.create(locationDB);
				this.updateGrids();
			}
			dialog.close();
		});
		abortButton.addClickListener(click -> dialog.close());
		countrySelect.addValueChangeListener(event -> {
			citySelect.setItems(this.cityService.findAllFromCountry(event.getValue()));
			postalCodeNumberField.setValue(0);
			numberIntegerField.setValue(0);
		});
		citySelect.addValueChangeListener(event -> {
			streetSelect.setItems(this.streetService.findAllFromCity(citySelect.getValue()));
			postalCodeNumberField.setValue(0);
			numberIntegerField.setValue(0);
		});
		dialog.add(fields, buttons);
		dialog.open();
	}

	private void openStreetDialog() {
		final Dialog dialog = new Dialog();
		final Select<Country> countrySelect = new Select<>();
		countrySelect.setLabel("Country");
		countrySelect.setItems(this.countryService.findAll());
		countrySelect.setItemLabelGenerator(Country::getName);
		final Select<City> citySelect = new Select<>();
		citySelect.setLabel("City");
		citySelect.setItemLabelGenerator(City::getName);
		final TextField streetTextField = new TextField("Street");
		final VerticalLayout fields = new VerticalLayout(countrySelect, citySelect, streetTextField);
		fields.setPadding(true);
		final Button addButton = new Button("Add");
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		addButton.addClickListener(click -> {
			if (streetTextField.getValue().trim().length() > 3) {
				var street = new Street();
				street.setName(streetTextField.getValue());
				street.setCity(citySelect.getValue());
				if (this.streetService.findAll().stream().noneMatch(s -> s.equals(street))) {
					this.streetService.create(street);
					this.updateGrids();
				}
				dialog.close();
			}
		});
		abortButton.addClickListener(click -> dialog.close());
		countrySelect.addValueChangeListener(event -> {
			citySelect.setItems(this.cityService.findAllFromCountry(event.getValue()));
			streetTextField.clear();
		});
		dialog.add(fields, buttons);
		dialog.open();
	}

	private void updateGrids() {
		this.locationGrid.setItems(this.locationService.findAll());
		this.countryGrid.setItems(this.countryService.findAll());
		this.cityGrid.setItems(this.cityService.findAll());
		this.streetGrid.setItems(this.streetService.findAll());
	}
}

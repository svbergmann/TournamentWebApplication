package com.github.ProfSchmergmann.TournamentWebApplication.views.admin;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

import static com.github.ProfSchmergmann.TournamentWebApplication.views.admin.LocationView.notSet;

@PermitAll
@Route(value = "cities", layout = AdminMainLayout.class)
@PageTitle("Cities | Tournament")
public class CityView extends VerticalLayout {

	private final CountryService countryService;
	private final CityService cityService;
	private Grid<City> cityGrid;

	public CityView(@Autowired CountryService countryService, @Autowired CityService cityService) {
		this.countryService = countryService;
		this.cityService = cityService;
		this.createCityGrid();
		var addClubButton = new Button("Add new City");
		addClubButton.addClickListener(click -> this.openCityDialog());
		this.add(new H2("Cities"),
		         this.cityGrid,
		         addClubButton);
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
		this.updateGrid();
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
				this.updateGrid();
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

	private void openCityDialog() {
		final Dialog dialog = new Dialog();
		final Select<Country> countrySelect = new Select<>();
		countrySelect.setLabel("Country");
		countrySelect.setItems(this.countryService.findAll());
		countrySelect.setItemLabelGenerator(Country::getName);
		final TextField cityTextField = new TextField("Name");
		final VerticalLayout fields = new VerticalLayout(countrySelect, cityTextField);
		fields.setPadding(true);
		final Button addButton = new Button("Add");
		addButton.addClickShortcut(Key.ENTER);
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		buttons.setPadding(true);
		addButton.addClickListener(click -> {
			if (cityTextField.getValue().length() > 3) {
				var city = new City();
				city.setCountry(countrySelect.getValue());
				city.setName(cityTextField.getValue());
				if (this.cityService.findAll().stream().noneMatch(c -> c.equals(city))) {
					this.cityService.create(city);
					this.updateGrid();
				}
				dialog.close();
			}
		});
		dialog.add(fields, buttons);
		dialog.open();
	}

	private void updateGrid() {
		this.cityGrid.setItems(this.cityService.findAll());
	}
}

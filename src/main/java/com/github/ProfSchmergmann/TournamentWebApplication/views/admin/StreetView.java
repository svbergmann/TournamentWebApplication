package com.github.ProfSchmergmann.TournamentWebApplication.views.admin;

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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

import static com.github.ProfSchmergmann.TournamentWebApplication.views.admin.LocationView.notSet;

@PermitAll
@Route(value = "streets", layout = AdminMainLayout.class)
@PageTitle("Streets | Tournament")
public class StreetView extends VerticalLayout {

	private final CountryService countryService;
	private final CityService cityService;
	private final StreetService streetService;
	private Grid<Street> streetGrid;

	public StreetView(@Autowired CountryService countryService,
	                  @Autowired CityService cityService,
	                  @Autowired StreetService streetService) {
		this.countryService = countryService;
		this.cityService = cityService;
		this.streetService = streetService;
		this.createStreetGrid();
		var addStreetButton = new Button("Add new Street");
		addStreetButton.addClickListener(click -> this.openStreetDialog());
		this.add(new H2("Streets"),
		         this.streetGrid,
		         addStreetButton);
	}

	private void createStreetGrid() {
		this.streetGrid = new Grid<>(Street.class, false);
		this.streetGrid.addColumn(
				    s -> s.getCity() == null || s.getCity().getCountry() == null ?
				         notSet : s.getCity().getCountry().getName())
		               .setHeader("Country")
		               .setSortable(true)
		               .setAutoWidth(true);
		this.streetGrid.addColumn(s -> s.getCity() == null ? notSet : s.getCity().getName())
		               .setHeader("City")
		               .setSortable(true)
		               .setAutoWidth(true);
		this.streetGrid.addColumn(Street::getName)
		               .setHeader("Name")
		               .setSortable(true)
		               .setAutoWidth(true);
		this.streetGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		this.updateGrid();
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
				this.updateGrid();
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
		addButton.addClickShortcut(Key.ENTER);
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		addButton.addClickListener(click -> {
			if (streetTextField.getValue().trim().length() > 3) {
				var street = new Street();
				street.setName(streetTextField.getValue());
				street.setCity(citySelect.getValue());
				if (this.streetService.findAll().stream().noneMatch(s -> s.equals(street))) {
					this.streetService.create(street);
					this.updateGrid();
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

	private void updateGrid() {
		this.streetGrid.setItems(this.streetService.findAll());
	}
}

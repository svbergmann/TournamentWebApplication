package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

import static com.github.ProfSchmergmann.TournamentWebApplication.views.LocationView.notSet;

@PermitAll
@Route(value = "cities", layout = MainLayout.class)
@PageTitle("Cities | Tournament")
public class CityView extends EntityView<City> {

	private final CountryService countryService;

	public CityView(@Autowired CountryService countryService,
	                @Autowired CityService cityService) {
		super("city", new Grid<>(), cityService);
		this.countryService = countryService;
	}

	@Override
	protected VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
		final Select<Country> countrySelect = new Select<>();
		countrySelect.setLabel(this.getTranslation("country"));
		countrySelect.setItems(this.countryService.findAll());
		countrySelect.setItemLabelGenerator(Country::getName);
		final TextField cityTextField = new TextField(this.getTranslation("name"));
		final VerticalLayout fields = new VerticalLayout(countrySelect, cityTextField);
		fields.setPadding(true);
		addButton.addClickListener(click -> {
			if (cityTextField.getValue().length() > 3) {
				var city = new City();
				city.setCountry(countrySelect.getValue());
				city.setName(cityTextField.getValue());
				if (this.entityService.findAll().stream().noneMatch(c -> c.equals(city))) {
					this.entityService.create(city);
					this.updateGrid();
				}
				dialog.close();
			}
		});
		return fields;
	}

	@Override
	void updateGridHeaders() {
		this.grid.addColumn(c -> c.getCountry() == null ?
		                         notSet : c.getCountry().getName())
		         .setHeader(this.getTranslation("country"))
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(City::getName)
		         .setHeader(this.getTranslation("name"))
		         .setSortable(true)
		         .setAutoWidth(true);
	}
}

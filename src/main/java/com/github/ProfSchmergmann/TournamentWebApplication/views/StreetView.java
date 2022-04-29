package com.github.ProfSchmergmann.TournamentWebApplication.views;

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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "streets", layout = MainLayout.class)
public class StreetView extends EntityView<Street> {

	private final CountryService countryService;
	private final CityService cityService;

	public StreetView(@Autowired CountryService countryService,
	                  @Autowired CityService cityService,
	                  @Autowired StreetService streetService,
	                  @Autowired SecurityService securityService) {
		super("street.pl", new Grid<>(), streetService, securityService);
		this.countryService = countryService;
		this.cityService = cityService;
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
		final TextField streetTextField = new TextField("Street");
		final VerticalLayout fields = new VerticalLayout(countrySelect, citySelect, streetTextField);
		fields.setPadding(true);
		addButton.addClickListener(click -> {
			if (streetTextField.getValue().trim().length() > 3) {
				var street = new Street();
				street.setName(streetTextField.getValue());
				street.setCity(citySelect.getValue());
				if (this.entityService.findAll().stream().noneMatch(s -> s.equals(street))) {
					this.entityService.create(street);
					this.updateGrid();
				}
				dialog.close();
			}
		});
		countrySelect.addValueChangeListener(event -> {
			citySelect.setItems(this.cityService.findAllFromCountry(event.getValue()));
			streetTextField.clear();
		});
		return fields;
	}

	@Override
	void setGridColumns() {
		this.grid.addColumn(
				    s -> s.getCity() == null || s.getCity().getCountry() == null ?
				         notSet : s.getCity().getCountry().getName(this.getLocale()))
		         .setHeader(this.getTranslation("country"))
		         .setKey("country")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(s -> s.getCity() == null ? notSet : s.getCity().getName())
		         .setHeader(this.getTranslation("city"))
		         .setKey("city")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(Street::getName)
		         .setHeader(this.getTranslation("name"))
		         .setKey("name")
		         .setSortable(true)
		         .setAutoWidth(true);
	}

	@Override
	void updateGridColumnHeaders() {
		this.grid.getColumnByKey("country")
		         .setHeader(this.getTranslation("country"));
		this.grid.getColumnByKey("city")
		         .setHeader(this.getTranslation("city"));
		this.grid.getColumnByKey("name")
		         .setHeader(this.getTranslation("name"));
	}
}

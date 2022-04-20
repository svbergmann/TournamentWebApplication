package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "City")
@PageTitle("Cities | Vaadin CRM")
public class CityView extends VerticalLayout {

	private final CityService cityService;
	private final CountryService countryService;
	Grid<City> grid = new Grid<>(City.class);

	public CityView(@Autowired CityService cityService, @Autowired CountryService countryService) {
		this.cityService = cityService;
		this.countryService = countryService;
		this.addClassName("list-view");
		this.setSizeFull();
		this.configureGrid();

		this.add(this.getContent());
		this.grid.setItems(this.cityService.findAll());
		this.grid.setColumns("name", "country.name");
	}

	private void configureGrid() {
		this.grid.setSizeFull();
		this.grid.getColumns().forEach(col -> col.setAutoWidth(true));
	}

	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(this.grid);
		content.setFlexGrow(2, this.grid);
		content.addClassNames("content");
		content.setSizeFull();
		return content;
	}

	private void updateList() {
		this.grid.setItems(this.cityService.findAll());
	}
}

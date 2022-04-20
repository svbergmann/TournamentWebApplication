package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.models.database.service.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.models.location.City;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.DataGenerator;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import elemental.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "City")
@PageTitle("Cities | Vaadin CRM")
public class CityView extends VerticalLayout {

	private final CityService service;
	Grid<City> grid = new Grid<>(City.class);
	CityForm form;

	public CityView(@Autowired CityService service) {
		this.service = service;
		this.addClassName("list-view");
		this.setSizeFull();
		this.configureGrid();
		this.configureForm();

		this.add(this.getContent());
		this.grid.setItems(this.service.findAll());
		this.grid.addDataGenerator((DataGenerator<City>) (item, jsonObject) -> service.findAll());
	}

	private Component getContent() {
		HorizontalLayout content = new HorizontalLayout(this.grid, this.form);
		content.setFlexGrow(2, this.grid);
		content.setFlexGrow(1, this.form);
		content.addClassNames("content");
		content.setSizeFull();
		return content;
	}

	private void configureForm() {
		this.form = new CityForm(this.service);
		this.form.setWidth("25em");
	}

	private void configureGrid() {
		this.grid.setSizeFull();
		this.grid.getColumns().forEach(col -> col.setAutoWidth(true));
	}

	private void updateList() {
		this.grid.setItems(this.service.findAll());
	}
}

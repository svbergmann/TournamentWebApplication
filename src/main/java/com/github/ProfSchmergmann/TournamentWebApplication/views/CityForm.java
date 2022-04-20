package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.models.database.service.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.models.location.City;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;

public class CityForm extends FormLayout {

	TextField nameField = new TextField("Name");

	Button save = new Button("Add new");

	CityService service;

	public CityForm(CityService service) {
		this.service = service;
		this.add(this.nameField, this.createButtonsLayout());
		this.nameField.addValueChangeListener(event -> this.save.setEnabled(true));
	}

	private HorizontalLayout createButtonsLayout() {
		this.save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		this.save.addClickShortcut(Key.ENTER);

		this.save.setEnabled(false);

		this.save.addClickListener(click -> {
			var city = new City();
			city.setName(this.nameField.getValue());
			this.service.saveCity(city);
			this.nameField.clear();
			this.save.setEnabled(false);
		});

		return new HorizontalLayout(this.save);
	}
}
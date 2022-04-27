package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.GenderService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "gender", layout = MainLayout.class)
@PageTitle("Genders | Tournament")
public class GenderView extends EntityView<Gender> {

	public GenderView(@Autowired GenderService genderService) {
		super("gender", new Grid<>(), genderService);
	}

	@Override
	VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
		final TextField genderTextField = new TextField(this.getTranslation("name"));
		addButton.addClickListener(click -> {
			var gender = new Gender();
			gender.setName(genderTextField.getValue());
			if (this.entityService.findAll().stream()
			                      .noneMatch(ageGroup1 -> ageGroup1.equals(gender))) {
				this.entityService.create(gender);
				this.updateGrid();
			}
			dialog.close();
		});
		return new VerticalLayout(genderTextField);
	}

	@Override
	void updateGridHeaders() {
		this.grid.addColumn(Gender::getName)
		         .setHeader(this.getTranslation("name"))
		         .setSortable(true)
		         .setAutoWidth(true);
	}
}

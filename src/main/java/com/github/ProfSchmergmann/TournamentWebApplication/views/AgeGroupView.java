package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroupService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

@PermitAll
@Route(value = "agegroups", layout = MainLayout.class)
@PageTitle("Age Groups | Tournament")
public class AgeGroupView extends EntityView<AgeGroup> {

	public AgeGroupView(@Autowired AgeGroupService ageGroupService) {
		super("age.group", new Grid<>(), ageGroupService);
	}

	@Override
	protected VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
		final TextField ageGroupTextField = new TextField(this.getTranslation("name"));
		addButton.addClickListener(click -> {
			var ageGroup = new AgeGroup();
			ageGroup.setName(ageGroupTextField.getValue());
			if (this.entityService.findAll().stream()
			                      .noneMatch(ageGroup1 -> ageGroup1.equals(ageGroup))) {
				this.entityService.create(ageGroup);
				this.updateGridItems();
			}
			dialog.close();
		});
		return new VerticalLayout(ageGroupTextField);
	}

	@Override
	void updateGridHeaders() {
		this.grid.addColumn(AgeGroup::getName)
		         .setHeader(this.getTranslation("name"))
		         .setSortable(true)
		         .setAutoWidth(true);
	}
}

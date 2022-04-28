package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroupService;
import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
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

	public AgeGroupView(@Autowired AgeGroupService ageGroupService,
	                    @Autowired SecurityService securityService) {
		super("age.group.pl", new Grid<>(), ageGroupService, securityService);
	}

	@Override
	VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
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
	void setGridColumns() {
		this.grid.addColumn(AgeGroup::getName)
		         .setHeader(this.getTranslation("name"))
		         .setKey("name")
		         .setSortable(true)
		         .setAutoWidth(true);
	}

	@Override
	void updateGridColumnHeaders() {
		this.grid.getColumnByKey("name")
		         .setHeader(this.getTranslation("name"));
	}
}

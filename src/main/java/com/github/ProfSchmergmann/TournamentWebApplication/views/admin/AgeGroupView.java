package com.github.ProfSchmergmann.TournamentWebApplication.views.admin;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroupService;
import com.github.ProfSchmergmann.TournamentWebApplication.views.MainLayout;
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
@Route(value = "", layout = MainLayout.class)
@PageTitle("Gyms | Tournament")
public class AgeGroupView extends VerticalLayout {

	private final AgeGroupService ageGroupService;
	private Grid<AgeGroup> ageGroupGrid;

	public AgeGroupView(@Autowired AgeGroupService ageGroupService) {
		this.ageGroupService = ageGroupService;
		this.createAgeGroupsGrid();
		Button addNewAgeGroupButton = new Button("Add new Age Group");
		addNewAgeGroupButton.addClickListener(click -> this.openAgeGroupDialog());
		this.add(new H2("Age Groups"),
		         this.ageGroupGrid,
		         addNewAgeGroupButton);
	}

	private void createAgeGroupsGrid() {
		this.ageGroupGrid = new Grid<>(AgeGroup.class, false);
		this.ageGroupGrid.addColumn(AgeGroup::getName)
		                 .setHeader("Name")
		                 .setSortable(true)
		                 .setAutoWidth(true);
		this.ageGroupGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		final GridContextMenu<AgeGroup> ageGroupGridContextMenu = this.ageGroupGrid.addContextMenu();
		ageGroupGridContextMenu.addItem("delete", event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem() + "?");
			final Button yesButton = new Button("Yes");
			final Button abortButton = new Button("Abort", e -> dialog.close());
			final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
			dialog.add(buttons);
			yesButton.addClickListener(click -> {
				this.ageGroupService.deleteById(event.getItem().get().getId());
				dialog.close();
				ageGroupGridContextMenu.close();
			});
			abortButton.addClickListener(click -> {
				dialog.close();
				ageGroupGridContextMenu.close();
			});
			dialog.open();
		});
		ageGroupGridContextMenu.addItem("refactor");
	}

	private void openAgeGroupDialog() {
		final Dialog dialog = new Dialog();
		final TextField ageGroupTextField = new TextField("Name");
		final Button addButton = new Button("Add");
		addButton.addClickShortcut(Key.ENTER);
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		buttons.setPadding(true);
		addButton.addClickListener(click -> {
			var ageGroup = new AgeGroup();
			ageGroup.setName(ageGroupTextField.getValue());
			if (this.ageGroupService.findAll().stream()
			                        .noneMatch(ageGroup1 -> ageGroup1.equals(ageGroup))) {
				this.ageGroupService.create(ageGroup);
				this.ageGroupGrid.setItems(this.ageGroupService.findAll());
			}
			dialog.close();
		});
		dialog.add(ageGroupTextField, buttons);
		dialog.open();
	}
}

package com.github.ProfSchmergmann.TournamentWebApplication.views.admin;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.GenderService;
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
@Route(value = "gender", layout = MainLayout.class)
@PageTitle("Genders | Tournament")
public class GenderView extends VerticalLayout {

	private final GenderService genderService;
	private Grid<Gender> genderGrid;

	public GenderView(@Autowired GenderService genderService) {
		this.genderService = genderService;
		this.createGenderGrid();
		Button addGenderButton = new Button("Add new Gender");
		addGenderButton.addClickListener(click -> this.openGenderDialog());
		this.add(new H2("Genders"),
		         this.genderGrid,
		         addGenderButton);
	}

	private void createGenderGrid() {
		this.genderGrid = new Grid<>(Gender.class, false);
		this.genderGrid.addColumn(Gender::getName)
		               .setHeader("Name")
		               .setSortable(true)
		               .setAutoWidth(true);
		this.genderGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		final GridContextMenu<Gender> ageGroupGridContextMenu = this.genderGrid.addContextMenu();
		ageGroupGridContextMenu.addItem("delete", event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem() + "?");
			final Button yesButton = new Button("Yes");
			final Button abortButton = new Button("Abort", e -> dialog.close());
			final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
			dialog.add(buttons);
			yesButton.addClickListener(click -> {
				this.genderService.deleteById(event.getItem().get().getId());
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

	private void openGenderDialog() {
		final Dialog dialog = new Dialog();
		final TextField genderTextField = new TextField("Name");
		final Button addButton = new Button("Add");
		addButton.addClickShortcut(Key.ENTER);
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		buttons.setPadding(true);
		addButton.addClickListener(click -> {
			var gender = new Gender();
			gender.setName(genderTextField.getValue());
			if (this.genderService.findAll().stream()
			                      .noneMatch(ageGroup1 -> ageGroup1.equals(gender))) {
				this.genderService.create(gender);
				this.genderGrid.setItems(this.genderService.findAll());
			}
			dialog.close();
		});
		dialog.add(genderTextField, buttons);
		dialog.open();
	}
}

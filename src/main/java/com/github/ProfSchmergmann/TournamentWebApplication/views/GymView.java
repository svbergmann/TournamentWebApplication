package com.github.ProfSchmergmann.TournamentWebApplication.views;

import static com.github.ProfSchmergmann.TournamentWebApplication.views.LocationView.notSet;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.Gym;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.GymService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.Location;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.LocationService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "gyms")
public class GymView extends VerticalLayout {

	private final GymService gymService;
	private final LocationService locationService;

	private Grid<Gym> gymGrid;

	public GymView(@Autowired GymService gymService, @Autowired LocationService locationService) {
		this.gymService = gymService;
		this.locationService = locationService;
		this.createGymGrid();
		Button addGymButton = new Button("Add new Gym");
		addGymButton.addClickListener(click -> this.openGymDialog());
		this.add(new H2("Gyms"),
				this.gymGrid,
				addGymButton);
	}

	private void createGymGrid() {
		this.gymGrid = new Grid<>(Gym.class, false);
		this.gymGrid.addColumn(Gym::getName)
				.setHeader("Name")
				.setSortable(true)
				.setAutoWidth(true);
		this.gymGrid.addColumn(
						gym -> gym.getLocation() == null || gym.getLocation().getCountry() == null ?
								notSet
								: gym.getLocation().getCountry().getName())
				.setHeader("Country")
				.setSortable(true)
				.setAutoWidth(true);
		this.gymGrid.addColumn(gym -> gym.getLocation() == null ?
						notSet
						: gym.getLocation().getPostalCode())
				.setHeader("Postal Code")
				.setSortable(true)
				.setAutoWidth(true);
		this.gymGrid.addColumn(gym -> gym.getLocation() == null || gym.getLocation().getCity() == null ?
						notSet
						: gym.getLocation().getCity().getName())
				.setHeader("City")
				.setSortable(true)
				.setAutoWidth(true);
		this.gymGrid.addColumn(
						gym -> gym.getLocation() == null || gym.getLocation().getStreet() == null ?
								notSet
								: gym.getLocation().getStreet().getName())
				.setHeader("Street")
				.setSortable(true)
				.setAutoWidth(true);
		this.gymGrid.addColumn(gym -> gym.getLocation() == null ?
						notSet
						: gym.getLocation().getNumber())
				.setHeader("Number")
				.setSortable(true)
				.setAutoWidth(true);
		this.gymGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		final GridContextMenu<Gym> countryGridContextMenu = this.gymGrid.addContextMenu();
		countryGridContextMenu.addItem("delete", event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem() + "?");
			final Button yesButton = new Button("Yes");
			final Button abortButton = new Button("Abort", e -> dialog.close());
			final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
			dialog.add(buttons);
			yesButton.addClickListener(click -> {
				this.gymService.deleteById(event.getItem().get().getId());
				dialog.close();
				countryGridContextMenu.close();
			});
			abortButton.addClickListener(click -> {
				dialog.close();
				countryGridContextMenu.close();
			});
			dialog.open();
		});
		countryGridContextMenu.addItem("refactor");
	}

	private void openGymDialog() {
		final Dialog dialog = new Dialog();
		final Select<Location> locationSelect = new Select<>();
		locationSelect.setLabel("Location");
		locationSelect.setItems(this.locationService.findAll());
		locationSelect.setItemLabelGenerator(l ->
				l.getCountry().getName() + ", "
						+ l.getPostalCode() + ", "
						+ l.getCity().getName() + ", "
						+ l.getStreet().getName() + ", "
						+ l.getNumber());
		final Label gymLabel = new Label("Gym");
		final IntegerField gymIntegerField = new IntegerField("Gym Number");
		gymIntegerField.setMin(1);
		final IntegerField gymCapacityField = new IntegerField("Gym Capacity");
		gymCapacityField.setMin(0);
		final HorizontalLayout gymLayout = new HorizontalLayout(gymLabel,
				gymIntegerField,
				gymCapacityField);
		gymLayout.setAlignItems(Alignment.CENTER);
		final VerticalLayout fields = new VerticalLayout(locationSelect, gymLayout);
		fields.setPadding(true);
		final Button addButton = new Button("Add");
		addButton.addClickShortcut(Key.ENTER);
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		buttons.setPadding(true);
		addButton.addClickListener(click -> {
			var gym = new Gym();
			gym.setLocation(locationSelect.getValue());
			gym.setNumber(gymIntegerField.getValue());
			gym.setName(gymLabel.getText() + " " + gym.getNumber());
			if (this.gymService.findAll().stream().noneMatch(g -> g.getNumber() == gym.getNumber())) {
				this.gymService.create(gym);
				this.gymGrid.setItems(this.gymService.findAll());
			}
			dialog.close();
		});
		dialog.add(fields, buttons);
		dialog.open();
	}
}

package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.Gym;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.GymService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.Location;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.LocationService;
import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
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
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import static com.github.ProfSchmergmann.TournamentWebApplication.views.LocationView.notSet;

@AnonymousAllowed
@Route(value = "gyms", layout = MainLayout.class)
@PageTitle("Gyms | Tournament")
public class GymView extends VerticalLayout implements LocaleChangeObserver {

	private final GymService gymService;
	private final LocationService locationService;
	private final SecurityService securityService;
	private Grid<Gym> gymGrid;

	public GymView(@Autowired GymService gymService,
	               @Autowired LocationService locationService,
	               @Autowired SecurityService securityService) {
		this.gymService = gymService;
		this.locationService = locationService;
		this.securityService = securityService;
		this.createGymGrid();
		this.add(new H2(this.getTranslation("gym.pl")), this.gymGrid);
		if (this.securityService.getAuthenticatedUser() != null) {
			var addGymButton = new Button(this.getTranslation("add.new.gym"));
			addGymButton.addClickListener(click -> this.openGymDialog());
			this.add(addGymButton);
		}
	}

	private void createGymGrid() {
		this.gymGrid = new Grid<>(Gym.class, false);
		this.gymGrid.addColumn(Gym::getName)
		            .setHeader(this.getTranslation("name"))
		            .setSortable(true)
		            .setAutoWidth(true);
		this.gymGrid.addColumn(
				    gym -> gym.getLocation() == null || gym.getLocation().getCountry() == null ?
				           notSet : gym.getLocation().getCountry().getName())
		            .setHeader(this.getTranslation("country"))
		            .setSortable(true)
		            .setAutoWidth(true);
		this.gymGrid.addColumn(gym -> gym.getLocation() == null ?
		                              notSet : gym.getLocation().getPostalCode())
		            .setHeader(this.getTranslation("plz"))
		            .setSortable(true)
		            .setAutoWidth(true);
		this.gymGrid.addColumn(gym -> gym.getLocation() == null || gym.getLocation().getCity() == null ?
		                              notSet : gym.getLocation().getCity().getName())
		            .setHeader(this.getTranslation("city"))
		            .setSortable(true)
		            .setAutoWidth(true);
		this.gymGrid.addColumn(
				    gym -> gym.getLocation() == null || gym.getLocation().getStreet() == null ?
				           notSet : gym.getLocation().getStreet().getName())
		            .setHeader(this.getTranslation("street"))
		            .setSortable(true)
		            .setAutoWidth(true);
		this.gymGrid.addColumn(gym -> gym.getLocation() == null ?
		                              notSet : gym.getLocation().getNumber())
		            .setHeader(this.getTranslation("number"))
		            .setSortable(true)
		            .setAutoWidth(true);
		this.gymGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		this.updateGrid();
		if (this.securityService.getAuthenticatedUser() != null) {
			final GridContextMenu<Gym> countryGridContextMenu = this.gymGrid.addContextMenu();
			countryGridContextMenu.addItem("delete", event -> {
				final Dialog dialog = new Dialog();
				dialog.add("Are you sure you want to delete " + event.getItem() + "?");
				final Button yesButton = new Button(this.getTranslation("yes"));
				final Button abortButton = new Button(this.getTranslation("abort"), e -> dialog.close());
				final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
				dialog.add(buttons);
				yesButton.addClickListener(click -> {
					this.gymService.deleteById(event.getItem().get().getId());
					this.updateGrid();
					dialog.close();
					countryGridContextMenu.close();
				});
				abortButton.addClickListener(click -> {
					dialog.close();
					countryGridContextMenu.close();
				});
				dialog.open();
			});
			countryGridContextMenu.addItem(this.getTranslation("refactor"));
		}
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
//		UI.getCurrent().getPage().reload();
	}

	private void openGymDialog() {
		final Dialog dialog = new Dialog();
		final Select<Location> locationSelect = new Select<>();
		locationSelect.setLabel(this.getTranslation("location"));
		locationSelect.setItems(this.locationService.findAll());
		locationSelect.setItemLabelGenerator(l ->
				                                     l.getCountry().getName() + ", "
						                                     + l.getPostalCode() + ", "
						                                     + l.getCity().getName() + ", "
						                                     + l.getStreet().getName() + ", "
						                                     + l.getNumber());
		final Label gymLabel = new Label(this.getTranslation("gym"));
		final IntegerField gymIntegerField = new IntegerField(this.getTranslation("gym") +
				                                                      " " +
				                                                      this.getTranslation("number"));
		gymIntegerField.setMin(1);
		final IntegerField gymCapacityField = new IntegerField(this.getTranslation("gym") +
				                                                       " " +
				                                                       this.getTranslation("capacity"));
		gymCapacityField.setMin(0);
		final HorizontalLayout gymLayout = new HorizontalLayout(gymLabel,
		                                                        gymIntegerField,
		                                                        gymCapacityField);
		gymLayout.setAlignItems(Alignment.CENTER);
		final VerticalLayout fields = new VerticalLayout(locationSelect, gymLayout);
		fields.setPadding(true);
		final Button addButton = new Button(this.getTranslation("add"));
		addButton.addClickShortcut(Key.ENTER);
		final Button abortButton = new Button(this.getTranslation("abort"), e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		buttons.setPadding(true);
		addButton.addClickListener(click -> {
			var gym = new Gym();
			gym.setLocation(locationSelect.getValue());
			gym.setNumber(gymIntegerField.getValue());
			gym.setName(gymLabel.getText() + " " + gym.getNumber());
			if (this.gymService.findAll().stream().noneMatch(g -> g.getNumber() == gym.getNumber())) {
				this.gymService.create(gym);
				this.updateGrid();
			}
			dialog.close();
		});
		dialog.add(fields, buttons);
		dialog.open();
	}

	private void updateGrid() {
		this.gymGrid.setItems(this.gymService.findAll());
	}
}

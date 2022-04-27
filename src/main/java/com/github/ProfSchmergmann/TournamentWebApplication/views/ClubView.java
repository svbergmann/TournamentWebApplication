package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.ClubService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

import static com.github.ProfSchmergmann.TournamentWebApplication.views.LocationView.notSet;

@PermitAll
@Route(value = "clubs", layout = MainLayout.class)
@PageTitle("Clubs | Tournament")
public class ClubView extends VerticalLayout implements LocaleChangeObserver {

	private final CountryService countryService;
	private final ClubService clubService;
	private final Button addButton;
	private final H2 header;
	private Grid<Club> grid;

	public ClubView(@Autowired CountryService countryService, @Autowired ClubService clubService) {
		this.countryService = countryService;
		this.clubService = clubService;
		this.createClubGrid();
		this.header = new H2(this.getTranslation("club"));
		this.addButton = new Button(this.getTranslation("add.new.club"));
		this.addButton.addClickListener(click -> this.openClubDialog());
		this.add(this.header,
		         this.grid,
		         this.addButton);
	}

	private void createClubGrid() {
		this.grid = new Grid<>(Club.class, false);
		this.grid.addColumn(Club::getName)
		         .setHeader("Name")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(
				    club -> club.getCountry() == null ? notSet : club.getCountry().getName())
		         .setHeader("Country")
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		this.updateGrid();
		final GridContextMenu<Club> clubGridContextMenu = this.grid.addContextMenu();
		clubGridContextMenu.addItem("delete", event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem() + "?");
			final Button yesButton = new Button("Yes");
			final Button abortButton = new Button("Abort", e -> dialog.close());
			final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
			dialog.add(buttons);
			yesButton.addClickListener(click -> {
				this.clubService.deleteById(event.getItem().get().getId());
				this.updateGrid();
				dialog.close();
				clubGridContextMenu.close();
			});
			abortButton.addClickListener(click -> {
				dialog.close();
				clubGridContextMenu.close();
			});
			dialog.open();
		});
		clubGridContextMenu.addItem("refactor");
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.header.setText(this.getTranslation("club"));
		this.updateGrid();
		this.addButton.setText(this.getTranslation("add.new.club"));
	}

	private void openClubDialog() {
		final Dialog dialog = new Dialog();
		final Select<Country> countrySelect = new Select<>();
		countrySelect.setLabel("Country");
		countrySelect.setItems(this.countryService.findAll());
		countrySelect.setItemLabelGenerator(Country::getName);
		final TextField clubTextField = new TextField("Name");
		final VerticalLayout fields = new VerticalLayout(countrySelect, clubTextField);
		fields.setPadding(true);
		final Button addButton = new Button("Add");
		addButton.addClickShortcut(Key.ENTER);
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		buttons.setPadding(true);
		addButton.addClickListener(click -> {
			if (clubTextField.getValue().length() > 3) {
				var club = new Club();
				club.setCountry(countrySelect.getValue());
				club.setName(clubTextField.getValue());
				if (this.clubService.findAll().stream().noneMatch(c -> c.equals(club))) {
					this.clubService.create(club);
					this.updateGrid();
				}
				dialog.close();
			}
		});
		dialog.add(fields, buttons);
		dialog.open();
	}

	private void updateGrid() {
		this.grid.setItems(this.clubService.findAll());
	}
}

package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.ClubService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;

import static com.github.ProfSchmergmann.TournamentWebApplication.views.LocationView.notSet;

@PermitAll
@Route(value = "clubs", layout = MainLayout.class)
@PageTitle("Clubs | Tournament")
public class ClubView extends EntityView<Club> {

	private final CountryService countryService;

	public ClubView(@Autowired CountryService countryService,
	                @Autowired ClubService clubService) {
		super("club", new Grid<>(), clubService);
		this.countryService = countryService;
	}

	@Override
	VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
		final Select<Country> countrySelect = new Select<>();
		countrySelect.setLabel(this.getTranslation("country"));
		countrySelect.setItems(this.countryService.findAll());
		countrySelect.setItemLabelGenerator(c -> c.getName(this.getLocale()));
		final TextField clubTextField = new TextField(this.getTranslation("name"));
		final VerticalLayout fields = new VerticalLayout(countrySelect, clubTextField);
		fields.setPadding(true);
		addButton.addClickListener(click -> {
			if (clubTextField.getValue().length() > 3) {
				var club = new Club();
				club.setCountry(countrySelect.getValue());
				club.setName(clubTextField.getValue());
				if (this.entityService.findAll().stream().noneMatch(c -> c.equals(club))) {
					this.entityService.create(club);
					this.updateGrid();
				}
				dialog.close();
			}
		});
		return fields;
	}

	@Override
	void updateGridHeaders() {
		this.grid.addColumn(Club::getName)
		         .setHeader(this.getTranslation("name"))
		         .setSortable(true)
		         .setAutoWidth(true);
		this.grid.addColumn(
				    club -> club.getCountry() == null ? notSet : club.getCountry().getName(this.getLocale()))
		         .setHeader(this.getTranslation("country"))
		         .setSortable(true)
		         .setAutoWidth(true);
	}
}

package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.CountryService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.security.PermitAll;
import java.util.Arrays;
import java.util.Locale;

@PermitAll
@Route(value = "countries", layout = MainLayout.class)
@PageTitle("Countries | Tournament")
public class CountryView extends VerticalLayout implements LocaleChangeObserver {

	private final CountryService countryService;
	private Grid<Country> countryGrid;

	public CountryView(@Autowired CountryService countryService) {
		this.countryService = countryService;
		this.createCountryGrid();
		var addCountryButton = new Button("Add new Country");
		addCountryButton.addClickListener(click -> this.openCountryDialog());
		this.add(new H2("Countries"),
		         this.countryGrid,
		         addCountryButton);
	}

	private void createCountryGrid() {
		this.countryGrid = new Grid<>(Country.class, false);
		this.countryGrid.addColumn(Country::getName)
		                .setHeader("Name")
		                .setSortable(true)
		                .setAutoWidth(true);
		this.countryGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		this.updateGrid();
		final GridContextMenu<Country> countryGridContextMenu = this.countryGrid.addContextMenu();
		countryGridContextMenu.addItem("delete", event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem().get() + "?");
			final Button yesButton = new Button("Yes");
			final Button abortButton = new Button("Abort", e -> dialog.close());
			final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
			dialog.add(buttons);
			yesButton.addClickListener(click -> {
				this.countryService.deleteById(event.getItem().get().getId());
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
		countryGridContextMenu.addItem("refactor");
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {

	}

	private void openCountryDialog() {
		final Dialog dialog = new Dialog();
		final ComboBox<Locale> countryComboBox = new ComboBox<>("Name");
		countryComboBox.setItems(Arrays.stream(Locale.getISOCountries())
		                               .map(code -> new Locale(UI.getCurrent().getLocale().getLanguage(), code))
		                               .toList()
		);
		countryComboBox.setItemLabelGenerator(Locale::getDisplayCountry);
		final Button addButton = new Button("Add");
		addButton.addClickShortcut(Key.ENTER);
		final Button abortButton = new Button("Abort", e -> dialog.close());
		final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
		buttons.setPadding(true);
		addButton.addClickListener(click -> {
			if (countryComboBox.getLabel() != null) {
				var country = new Country();
				country.setIso3Name(countryComboBox.getValue().getISO3Country());
				if (this.countryService.findAll().stream().noneMatch(c -> c.equals(country))) {
					this.countryService.create(country);
					this.updateGrid();
				}
				dialog.close();
			}
		});
		dialog.add(countryComboBox, buttons);
		dialog.open();
	}

	private void updateGrid() {
		this.countryGrid.setItems(this.countryService.findAll());
	}

}

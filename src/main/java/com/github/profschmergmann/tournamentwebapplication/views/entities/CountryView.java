package com.github.profschmergmann.tournamentwebapplication.views.entities;

import com.github.profschmergmann.tournamentwebapplication.database.models.location.country.Country;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.country.CountryService;
import com.github.profschmergmann.tournamentwebapplication.security.SecurityService;
import com.github.profschmergmann.tournamentwebapplication.views.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import java.util.Arrays;
import java.util.Locale;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "countries", layout = MainLayout.class)
public class CountryView extends EntityView<Country> {

  public CountryView(@Autowired CountryService countryService,
      @Autowired SecurityService securityService) {
    super("country.pl", new Grid<>(), countryService, securityService);
  }

  @Override
  VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
    final ComboBox<Locale> countryComboBox = new ComboBox<>(this.getTranslation("name"));
    countryComboBox.setItems(Arrays.stream(Locale.getISOCountries())
        .map(code -> new Locale(UI.getCurrent().getLocale().getLanguage(), code)).toList());
    countryComboBox.setItemLabelGenerator(locale -> locale.getDisplayCountry(this.getLocale()));
    addButton.addClickListener(click -> {
      if (countryComboBox.getLabel() != null) {
        var country = new Country()
            .setIso3Name(countryComboBox.getValue().getISO3Country());
        if (this.entityService.findAll().stream().noneMatch(c -> c.equals(country))) {
          this.entityService.create(country);
          this.updateGrid();
        }
        dialog.close();
      }
    });
    return new VerticalLayout(countryComboBox);
  }

  @Override
  void setGridColumns() {
    this.grid.addColumn(c -> c.getName(this.getLocale()))
        .setKey("name");
  }
}

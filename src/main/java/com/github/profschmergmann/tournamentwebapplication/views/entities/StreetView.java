package com.github.profschmergmann.tournamentwebapplication.views.entities;

import com.github.profschmergmann.tournamentwebapplication.database.models.location.street.Street;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.street.StreetService;
import com.github.profschmergmann.tournamentwebapplication.security.SecurityService;
import com.github.profschmergmann.tournamentwebapplication.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PermitAll
@Route(value = "streets", layout = MainLayout.class)
public class StreetView extends EntityView<Street> {

  public StreetView(@Autowired StreetService streetService,
      @Autowired SecurityService securityService) {
    super("street.pl", new Grid<>(), streetService, securityService);
  }

  @Override
  VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
    final TextField streetTextField = new TextField("Street");
    final VerticalLayout fields = new VerticalLayout(streetTextField);
    fields.setPadding(true);
    addButton.addClickListener(click -> {
      if (streetTextField.getValue().trim().length() > 3) {
        var street = new Street()
            .setName(streetTextField.getValue());
        if (this.entityService.findAll().stream().noneMatch(s -> s.equals(street))) {
          this.entityService.create(street);
          this.updateGrid();
        }
        dialog.close();
      }
    });
    return fields;
  }

  @Override
  void setGridColumns() {
    this.grid.addColumn(Street::getName)
        .setKey("name");
  }
}

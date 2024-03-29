package com.github.profschmergmann.tournamentwebapplication.views.entities;

import com.github.profschmergmann.tournamentwebapplication.database.models.gender.Gender;
import com.github.profschmergmann.tournamentwebapplication.database.models.gender.GenderService;
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
@Route(value = "gender", layout = MainLayout.class)
public class GenderView extends EntityView<Gender> {

  public GenderView(@Autowired GenderService genderService,
      @Autowired SecurityService securityService) {
    super("gender.pl", new Grid<>(), genderService, securityService);
  }

  @Override
  VerticalLayout getDialogComponents(Dialog dialog, Button addButton) {
    final TextField genderTextField = new TextField(this.getTranslation("name"));
    addButton.addClickListener(click -> {
      var gender = new Gender()
          .setName(genderTextField.getValue());
      if (this.entityService.findAll().stream().noneMatch(ageGroup1 -> ageGroup1.equals(gender))) {
        this.entityService.create(gender);
        this.updateGrid();
      }
      dialog.close();
    });
    return new VerticalLayout(genderTextField);
  }

  @Override
  void setGridColumns() {
    this.grid.addColumn(Gender::getName)
        .setKey("name");
  }
}

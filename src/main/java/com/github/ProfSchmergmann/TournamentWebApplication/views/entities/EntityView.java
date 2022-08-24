package com.github.profschmergmann.tournamentwebapplication.views.entities;

import com.github.profschmergmann.tournamentwebapplication.database.models.Model;
import com.github.profschmergmann.tournamentwebapplication.database.models.ModelService;
import com.github.profschmergmann.tournamentwebapplication.security.SecurityService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.HasDynamicTitle;

public abstract class EntityView<T extends Model> extends VerticalLayout implements
    LocaleChangeObserver, HasDynamicTitle {

  public static final String notSet = "not set";
  protected SecurityService securityService;
  protected Button addButton;
  protected H2 header;
  protected String headerProperty;
  protected Grid<T> grid;
  protected ModelService<T> entityService;

  public EntityView(String headerProperty, Grid<T> grid, ModelService<T> entityService,
      SecurityService securityService) {
    this.securityService = securityService;
    this.entityService = entityService;
    this.headerProperty = headerProperty;
    this.header = new H2(this.getTranslation(this.headerProperty));
    this.grid = grid;
    this.grid.setColumnReorderingAllowed(true);
    this.grid.setAllRowsVisible(true);
    this.grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    this.setGridColumns();
    this.setGridColumnsProperties();
    this.updateGridColumnHeaders();
    this.updateGridContextMenu();
    this.updateGridItems();
    this.add(this.header, this.grid);
    if (this.securityService.getAuthenticatedUser() != null) {
      this.addButton = new Button(this.getTranslation("add"));
      this.addButton.setId("add.button");
      this.addButton.addClickListener(click -> this.openAddDialog());
      this.add(this.addButton);
    }
  }

  abstract VerticalLayout getDialogComponents(Dialog dialog, Button addButton);

  @Override
  public String getPageTitle() {
    return this.getTranslation(this.headerProperty) + " | " + this.getTranslation(
        "application.name");
  }

  @Override
  public void localeChange(LocaleChangeEvent event) {
    this.header.setText(this.getTranslation(this.headerProperty));
    this.updateGridColumnHeaders();
    this.updateGridContextMenu();
    if (this.addButton != null) {
      this.addButton.setText(this.getTranslation("add"));
    }
    UI.getCurrent().getPage().setTitle(this.getPageTitle());
  }

  protected void openAddDialog() {
    final Dialog dialog = new Dialog();
    final Button addButton = new Button(this.getTranslation("add"));
    addButton.addClickShortcut(Key.ENTER);
    final Button abortButton = new Button(this.getTranslation("abort"), e -> dialog.close());
    final HorizontalLayout buttons = new HorizontalLayout(addButton, abortButton);
    buttons.setPadding(true);
    dialog.add(this.getDialogComponents(dialog, addButton), buttons);
    dialog.open();
  }

  abstract void setGridColumns();

  protected void setGridColumnsProperties() {
    this.grid.getColumns().forEach(column -> {
      column.setSortable(true);
      column.setAutoWidth(true);
    });
  }

  protected void updateGrid() {
    this.updateGridColumnHeaders();
    this.updateGridItems();
    this.updateGridContextMenu();
  }

  protected void updateGridColumnHeaders() {
    this.grid.getColumns()
        .forEach(column -> column.setHeader(this.getTranslation(column.getKey())));
  }

  protected void updateGridContextMenu() {
    if (this.securityService.getAuthenticatedUser() != null) {
      final GridContextMenu<T> gridContextMenu = this.grid.addContextMenu();
      gridContextMenu.addItem(this.getTranslation("delete"), event -> {
        final Dialog dialog = new Dialog();
        dialog.add(this.getTranslation("delete.question", event.getItem().get()));
        final Button yesButton = new Button(this.getTranslation("yes"));
        final Button abortButton = new Button(this.getTranslation("abort"), e -> dialog.close());
        final HorizontalLayout buttons = new HorizontalLayout(yesButton, abortButton);
        dialog.add(buttons);
        yesButton.addClickListener(click -> {
          this.entityService.deleteById(event.getItem().get().getId());
          this.updateGridItems();
          dialog.close();
          gridContextMenu.close();
        });
        abortButton.addClickListener(click -> {
          dialog.close();
          gridContextMenu.close();
        });
        dialog.open();
      });
      gridContextMenu.addItem(this.getTranslation("abort"));
    }
  }

  protected void updateGridItems() {
    this.grid.setItems(this.entityService.findAll());
  }

}

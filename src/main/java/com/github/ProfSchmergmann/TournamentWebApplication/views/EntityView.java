package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
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

public abstract class EntityView<T extends IModel> extends VerticalLayout implements LocaleChangeObserver {

	protected Button addButton;
	protected H2 header;
	protected String headerProperty;
	protected Grid<T> grid;
	protected IModelService<T> entityService;

	public EntityView(String headerProperty, Grid<T> grid, IModelService<T> entityService) {
		this.entityService = entityService;
		this.headerProperty = headerProperty;
		this.header = new H2(this.getTranslation(this.headerProperty));
		this.grid = grid;
		this.grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
		this.updateGrid();
		this.addButton = new Button(this.getTranslation("add.new"));
		this.addButton.addClickListener(click -> this.openAddDialog());
		this.add(this.header,
		         this.grid,
		         this.addButton);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.header.setText(this.getTranslation(this.headerProperty));
		this.grid.removeAllColumns();
		this.updateGridHeaders();
		this.updateGridContextMenu();
		this.addButton.setText("add");
	}

	abstract VerticalLayout getDialogComponents(Dialog dialog, Button addButton);
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

	protected void updateGrid() {
		this.grid.removeAllColumns();
		this.updateGridHeaders();
		this.updateGridItems();
		this.updateGridContextMenu();
	}

	protected void updateGridContextMenu() {
		final GridContextMenu<T> gridContextMenu = this.grid.addContextMenu();
		gridContextMenu.addItem(this.getTranslation("delete"), event -> {
			final Dialog dialog = new Dialog();
			dialog.add("Are you sure you want to delete " + event.getItem() + "?");
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

	abstract void updateGridHeaders();

	protected void updateGridItems() {
		this.grid.setItems(this.entityService.findAll());
	}

}

package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.logging.Logger;

public class MainLayout extends AppLayout implements LocaleChangeObserver {

	private static final Logger LOGGER = Logger.getLogger(MainLayout.class.getName());
	private final I18NProvider i18NProvider;
	private final SecurityService securityService;

	public MainLayout(@Autowired I18NProvider i18NProvider, @Autowired SecurityService securityService) {
		this.securityService = securityService;
		this.i18NProvider = i18NProvider;
		this.createHeader();
		this.createDrawer();
	}

	private void createDrawer() {
		if (this.securityService.getAuthenticatedUser() != null) {
			this.addToDrawer(new VerticalLayout(
					new RouterLink(this.getTranslation("age.group.pl"), AgeGroupView.class),
					new RouterLink(this.getTranslation("city.pl"), CityView.class),
					new RouterLink(this.getTranslation("club.pl"), ClubView.class),
					new RouterLink(this.getTranslation("country.pl"), CountryView.class),
					new RouterLink(this.getTranslation("gender.pl"), GenderView.class),
					new RouterLink(this.getTranslation("location.pl"), LocationView.class),
					new RouterLink(this.getTranslation("street.pl"), StreetView.class))
			);
		}
		this.addToDrawer(new VerticalLayout(
				new RouterLink(this.getTranslation("gym.pl"), GymView.class),
				new RouterLink(this.getTranslation("team.pl"), TeamView.class))
		);
	}

	private void createHeader() {
		var logo = new H1("Tournament Application");
		logo.addClassNames("text-l", "m-m");

		var languageSelect = new Select<Locale>();
		languageSelect.setItems(this.i18NProvider.getProvidedLocales());
		languageSelect.setItemLabelGenerator(l -> this.getTranslation(l.toLanguageTag()));
		languageSelect.setValue(UI.getCurrent().getLocale() == null ? Locale.ENGLISH : UI.getCurrent().getLocale());
		languageSelect.addValueChangeListener(event -> this.saveLocalePreference(event.getValue()));

		var header = new HorizontalLayout(new DrawerToggle(), logo, languageSelect);

		if (this.securityService.getAuthenticatedUser() != null) {
			var logout = new Button(this.getTranslation("log.out"), e -> this.securityService.logout());
			header.add(logout);
		} else {
			var login = new Button(this.getTranslation("log.in"), e -> UI.getCurrent().navigate(LoginView.class));
			header.add(login);
		}

		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		header.expand(logo);
		header.setWidth("100%");
		header.addClassNames("py-0", "px-m");
		header.setPadding(true);

		this.addToNavbar(header);
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		// TODO: Update Routerlinks
	}

	private void saveLocalePreference(Locale locale) {
		UI.getCurrent().getSession().setLocale(locale);
		//TODO: Save locale inside cookie
	}

}

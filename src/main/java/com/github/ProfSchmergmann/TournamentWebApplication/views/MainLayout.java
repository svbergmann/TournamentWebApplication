package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.github.ProfSchmergmann.TournamentWebApplication.views.admin.AgeGroupView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.admin.CityView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.admin.ClubView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.admin.CountryView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.admin.GenderView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.admin.GymView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.admin.LocationView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.admin.StreetView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.admin.TeamView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.Cookie;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
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
		this.addToDrawer(new VerticalLayout(
				new RouterLink("Age Groups", AgeGroupView.class),
				new RouterLink("Cities", CityView.class),
				new RouterLink("Clubs", ClubView.class),
				new RouterLink("Countries", CountryView.class),
				new RouterLink("Genders", GenderView.class),
				new RouterLink("Gyms", GymView.class),
				new RouterLink("Locations", LocationView.class),
				new RouterLink("Streets", StreetView.class),
				new RouterLink("Teams", TeamView.class))
		);
	}

	private void createHeader() {
		var logo = new H1("Tournament Application");
		logo.addClassNames("text-l", "m-m");

		var logout = new Button("Log out", e -> this.securityService.logout());
		var login = new Button("Log in", e -> this.getUI().ifPresent(ui -> ui.navigate("login")));
		var languageSelect = new Select<Locale>();
		languageSelect.setItems(this.i18NProvider.getProvidedLocales());
		languageSelect.setItemLabelGenerator(l -> this.getTranslation(l.toLanguageTag()));
		languageSelect.setValue(UI.getCurrent().getLocale());
		languageSelect.addValueChangeListener(event -> this.saveLocalePreference(event.getValue()));
		var header = new HorizontalLayout(new DrawerToggle(), logo, languageSelect, login, logout);
		header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
		header.expand(logo);
		header.setWidth("100%");
		header.addClassNames("py-0", "px-m");
		header.setPadding(true);

		this.addToNavbar(header);
	}

	private String findLocaleFromCookie() {
		final Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
		if (cookies == null) {
			return "";
		}
		final Optional<String> cookie = Arrays.stream(cookies).filter(c -> "locale".equals(c.getName())).map(Cookie::getValue).findAny();
		return cookie.orElse("");
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		//TODO: change language based on preferences
	}

	private void saveLocalePreference(Locale locale) {
		this.getUI().get().setLocale(locale);
		VaadinService.getCurrentResponse().addCookie(new Cookie("locale", locale.toLanguageTag()));
		Notification.show(this.getTranslation("view.help.localesaved"));
	}

}

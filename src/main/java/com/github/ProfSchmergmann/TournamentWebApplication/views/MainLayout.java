package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
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
	private final Span helpCookieNotFound = new Span();
	private final Span helpCookieFound = new Span();
	private final Span helpSelectLang = new Span();
	private final String cookieLang;

	public MainLayout(@Autowired I18NProvider i18NProvider, @Autowired SecurityService securityService) {
		this.securityService = securityService;
		this.i18NProvider = i18NProvider;
		this.cookieLang = this.findLocaleFromCookie();
		if ("".equals(this.cookieLang)) {
			this.addToNavbar(this.helpCookieNotFound);
		} else {
			this.addToNavbar(this.helpCookieFound);
		}
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
		languageSelect.setValue(this.findLocaleFromCookie().equals("en") ?
		                        Locale.ENGLISH :
		                        Locale.GERMAN);
		languageSelect.setItemLabelGenerator(l -> this.getTranslation(l.toLanguageTag()));
		languageSelect.addValueChangeListener(event -> this.saveLocalePreference(event.getValue()));

		var header = new HorizontalLayout(new DrawerToggle(),
		                                  logo,
		                                  languageSelect,
		                                  this.securityService.getAuthenticatedUser() != null ?
		                                  new Button(this.getTranslation("log.out"),
		                                             e -> this.securityService.logout()) :
		                                  new Button(this.getTranslation("log.in"),
		                                             e -> UI.getCurrent().navigate(LoginView.class))
		);

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
		final Optional<String> cookie = Arrays.stream(cookies)
		                                      .filter(c -> "locale".equals(c.getName()))
		                                      .map(Cookie::getValue)
		                                      .findFirst();
		return cookie.orElse(Locale.ENGLISH.getLanguage());
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {

	}

	private void saveLocalePreference(Locale locale) {
		this.getUI().get().setLocale(locale);
		VaadinService.getCurrentResponse().addCookie(new Cookie("locale", locale.toLanguageTag()));
		Notification.show(this.getTranslation("view.help.localesaved"));
	}

}

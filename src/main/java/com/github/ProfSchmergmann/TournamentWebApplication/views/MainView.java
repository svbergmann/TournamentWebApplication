package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.services.GreetService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;

@Route
public class MainView extends VerticalLayout implements LocaleChangeObserver {

	private static final Logger LOGGER = Logger.getLogger(MainView.class.getName());

	private final TextField nameField;
	private final Button greetingButton;
	private final Select<Locale> languageSelect;

	private final Span helpCookieNotFound = new Span();
	private final Span helpCookieFound = new Span();
	private final Span helpSelectLang = new Span();
	private final String cookieLang;

	public MainView(@Autowired GreetService service, @Autowired I18NProvider i18NProvider) {
		H1 heading = new H1("Tournament games");

		LOGGER.info("Current locale is " + UI.getCurrent().getLocale());

		this.cookieLang = this.findLocaleFromCookie();
		if ("".equals(this.cookieLang)) {

			this.add(this.helpCookieNotFound);
		} else {

			this.add(this.helpCookieFound);

			final Button clearCookieButton = new Button(this.getTranslation("view.help.clearcookie"),
					e -> this.clearLocalePreference());
			clearCookieButton.addThemeVariants(ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ERROR);
			this.add(clearCookieButton);
		}

		// component for selecting another language
		this.languageSelect = new Select<>();
		this.languageSelect.setItems(i18NProvider.getProvidedLocales());
		this.languageSelect.setItemLabelGenerator(l -> this.getTranslation(l.getLanguage()));

		this.languageSelect.setValue(UI.getCurrent().getLocale());
		this.languageSelect.addValueChangeListener(
				event -> this.saveLocalePreference(event.getValue()));
		this.add(this.languageSelect);

		this.add(this.helpSelectLang);

		// Examples how to use translations.
		this.nameField = new TextField();
		this.add(this.nameField);

		// The service can use the i18nProvider too.
		this.greetingButton = new Button();
		this.greetingButton.addClickListener(
				e -> Notification.show(service.greet(this.nameField.getValue(), this.getLocale())));
		this.greetingButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		this.greetingButton.addClickShortcut(Key.ENTER);
		this.add(this.greetingButton);
	}

	private String findLocaleFromCookie() {
		final Cookie[] cookies = VaadinRequest.getCurrent().getCookies();
		if (cookies == null) {
			return "";
		}
		final Optional<String> cookie = Arrays.stream(cookies).filter(c -> "locale".equals(c.getName()))
				.map(Cookie::getValue).findAny();
		return cookie.orElse("");
	}

	private void clearLocalePreference() {
		VaadinService.getCurrentResponse().addCookie(new Cookie("locale", null));
		this.getUI().get().getPage().reload();
	}

	private void saveLocalePreference(Locale locale) {
		this.getUI().get().setLocale(locale);
		VaadinService.getCurrentResponse().addCookie(new Cookie("locale", locale.toLanguageTag()));
		Notification.show(this.getTranslation("view.help.localesaved"));
	}

	@Override
	public void localeChange(LocaleChangeEvent event) {
		this.helpCookieNotFound.setText(this.getTranslation("view.help.nopreferencefound"));
		this.helpCookieFound.setText(
				this.getTranslation("view.help.localefoundfromcookie", this.cookieLang));
		this.helpSelectLang.setText(this.getTranslation("view.help.selectlang"));

		this.languageSelect.setLabel(this.getTranslation("view.langSelect"));
		this.nameField.setLabel(this.getTranslation("view.nameField"));
		this.greetingButton.setText(this.getTranslation("view.helloButton"));
	}

}

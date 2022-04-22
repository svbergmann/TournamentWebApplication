package com.github.ProfSchmergmann.TournamentWebApplication;

import com.vaadin.flow.i18n.I18NProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import static java.util.Locale.GERMAN;
import static java.util.ResourceBundle.getBundle;

@Component
public class TournamentI18NProvider implements I18NProvider {

	public static final String RESOURCE_BUNDLE_NAME = "words";

	private static final ResourceBundle RESOURCE_BUNDLE_DE = getBundle(RESOURCE_BUNDLE_NAME, GERMAN);
	private static final ResourceBundle RESOURCE_BUNDLE_EN = getBundle(RESOURCE_BUNDLE_NAME, ENGLISH);
	private static final ResourceBundle RESOURCE_BUNDLE_FR = getBundle(RESOURCE_BUNDLE_NAME, FRENCH);
	private static final Logger LOGGER = Logger.getLogger(TournamentI18NProvider.class.getName());

	@Override
	public List<Locale> getProvidedLocales() {
		return List.of(ENGLISH, GERMAN, FRENCH);
	}

	@Override
	public String getTranslation(String key, Locale locale, Object... params) {
		var resourceBundle = RESOURCE_BUNDLE_EN;
		if (GERMAN.equals(locale)) {
			resourceBundle = RESOURCE_BUNDLE_DE;
		} else if (FRENCH.equals(locale)) {
			resourceBundle = RESOURCE_BUNDLE_FR;
		}

		if (!resourceBundle.containsKey(key)) {
			LOGGER.info("missing resource key (i18n) " + key);
			return key + " - " + locale;
		} else {
			return (resourceBundle.containsKey(key)) ? resourceBundle.getString(key) : key;
		}
	}

}

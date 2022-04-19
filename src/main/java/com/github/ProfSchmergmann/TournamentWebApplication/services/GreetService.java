package com.github.ProfSchmergmann.TournamentWebApplication.services;

import com.vaadin.flow.i18n.I18NProvider;
import java.io.Serializable;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GreetService implements Serializable {

	@Autowired
	private I18NProvider i18NProvider;

	public String greet(String name, Locale locale) {

		if (name == null || name.isEmpty()) {
			return this.i18NProvider.getTranslation("service.anonymousGreeting", locale);
		} else {
			return this.i18NProvider.getTranslation("service.greeting", locale, name);
		}
	}
}
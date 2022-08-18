package com.github.ProfSchmergmann.TournamentWebApplication;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServiceInitListener;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceInitListener implements VaadinServiceInitListener {

  @Autowired
  private I18NProvider i18nProvider;

  private void initLanguage(UI ui) {
    Optional<Cookie> localeCookie = Optional.empty();

    Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
    if (cookies != null) {
      localeCookie = Arrays.stream(cookies)
          .filter(cookie -> "locale".equals(cookie.getName()))
          .findFirst();
    }

    Locale locale;

    if (localeCookie.isPresent() && !"".equals(localeCookie.get().getValue())) {
      // Cookie found, use that
      locale = Locale.forLanguageTag(localeCookie.get().getValue());
    } else {
      // Try to use Vaadin's browser locale detection
      locale = VaadinService.getCurrentRequest().getLocale();
    }

    // If the detection fails, default to the first language we support.
    if (locale.getLanguage().equals("")) {
      locale = this.i18nProvider.getProvidedLocales().get(0);
    }

    ui.setLocale(locale);
  }

  @Override
  public void serviceInit(ServiceInitEvent event) {
    event.getSource().addUIInitListener(e -> this.initLanguage(e.getUI()));
  }
}

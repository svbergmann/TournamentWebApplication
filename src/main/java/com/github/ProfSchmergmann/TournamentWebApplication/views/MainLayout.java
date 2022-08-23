package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.github.ProfSchmergmann.TournamentWebApplication.TournamentI18NProvider;
import com.github.ProfSchmergmann.TournamentWebApplication.security.SecurityService;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.AgeGroupView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.CityView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.ClubView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.CountryView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.GameView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.GenderView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.GymView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.LocationView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.RankingView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.StreetView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.entities.TeamView;
import com.github.ProfSchmergmann.TournamentWebApplication.views.security.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinService;
import java.util.Locale;
import java.util.logging.Logger;
import javax.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;

public class MainLayout extends AppLayout implements LocaleChangeObserver {

  private static final Logger LOGGER = Logger.getLogger(MainLayout.class.getName());
  private final I18NProvider i18NProvider;
  private final SecurityService securityService;
  private RouterLink ageGroupRouterLink;
  private RouterLink cityRouterLink;
  private RouterLink clubRouterLink;
  private RouterLink countryRouterLink;
  private RouterLink gameRouterLink;
  private RouterLink genderRouterLink;
  private RouterLink gymRouterLink;
  private H1 headerText;
  private RouterLink locationRouterLink;
  private Button logButton;
  private RouterLink rankingLink;
  private RouterLink streetRouterLink;
  private RouterLink teamRouterLink;

  public MainLayout(@Autowired I18NProvider i18NProvider,
      @Autowired SecurityService securityService) {
    this.securityService = securityService;
    this.i18NProvider = i18NProvider;
    this.createHeader();
    this.createDrawer();
  }

  private void createDrawer() {
    if (this.securityService.getAuthenticatedUser() != null) {
      this.ageGroupRouterLink = new RouterLink(this.getTranslation("age.group.pl"),
          AgeGroupView.class);
      this.cityRouterLink = new RouterLink(this.getTranslation("city.pl"), CityView.class);
      this.clubRouterLink = new RouterLink(this.getTranslation("club.pl"), ClubView.class);
      this.countryRouterLink = new RouterLink(this.getTranslation("country.pl"), CountryView.class);
      this.genderRouterLink = new RouterLink(this.getTranslation("gender.pl"), GenderView.class);
      this.locationRouterLink = new RouterLink(this.getTranslation("location.pl"),
          LocationView.class);
      this.streetRouterLink = new RouterLink(this.getTranslation("street.pl"), StreetView.class);
      this.addToDrawer(
          new VerticalLayout(this.ageGroupRouterLink, this.cityRouterLink, this.clubRouterLink,
              this.countryRouterLink, this.genderRouterLink, this.locationRouterLink,
              this.streetRouterLink));
    }
    this.gameRouterLink = new RouterLink(this.getTranslation("game.pl"), GameView.class);
    this.gymRouterLink = new RouterLink(this.getTranslation("gym.pl"), GymView.class);
    this.rankingLink = new RouterLink(this.getTranslation("ranking"), RankingView.class);
    this.teamRouterLink = new RouterLink(this.getTranslation("team.pl"), TeamView.class);
    this.addToDrawer(new VerticalLayout(this.gameRouterLink, this.gymRouterLink, this.rankingLink,
        this.teamRouterLink));
  }

  private void createHeader() {
    this.headerText = new H1(this.getTranslation("application.name"));
    this.headerText.addClassNames("text-l", "m-m");

    Select<Locale> languageSelect = new Select<>();
    languageSelect.setWidth("5rem");
    languageSelect.setRenderer(new ComponentRenderer<>(locale -> {
      var img = new Image(TournamentI18NProvider.getImageStreamResourceFromLocale(locale),
          locale.toLanguageTag());
      img.setAlt(locale.toLanguageTag());
      img.setWidth("var(--lumo-size-s)");
      return img;
    }));
    languageSelect.setItems(this.i18NProvider.getProvidedLocales());
    var currentLocale = UI.getCurrent().getLocale();
    languageSelect.setValue(
        this.i18NProvider.getProvidedLocales().contains(currentLocale) ? currentLocale
            : this.i18NProvider.getProvidedLocales().get(0));
    languageSelect.addValueChangeListener(event -> this.saveLocalePreference(event.getValue()));

    var authenticated = this.securityService.getAuthenticatedUser() != null;
    this.logButton = new Button(
        authenticated ? this.getTranslation("log.out") : this.getTranslation("log.in"),
        authenticated ? e -> this.securityService.logout()
            : e -> UI.getCurrent().navigate(LoginView.class));

    var header = new HorizontalLayout(new DrawerToggle(), this.headerText, languageSelect,
        this.logButton);

    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.expand(this.headerText);
    header.setWidth("100%");
    header.addClassNames("py-0", "px-m");
    header.setPadding(true);

    this.addToNavbar(header);
  }

  @Override
  public void localeChange(LocaleChangeEvent event) {
    this.headerText.setText(this.getTranslation("application.name"));
    var authenticated = this.securityService.getAuthenticatedUser() != null;
    this.logButton.setText(
        authenticated ? this.getTranslation("log.out") : this.getTranslation("log.in"));
    if (authenticated) {
      this.ageGroupRouterLink.setText(this.getTranslation("age.group.pl"));
      this.cityRouterLink.setText(this.getTranslation("city.pl"));
      this.clubRouterLink.setText(this.getTranslation("club.pl"));
      this.countryRouterLink.setText(this.getTranslation("country.pl"));
      this.genderRouterLink.setText(this.getTranslation("gender.pl"));
      this.locationRouterLink.setText(this.getTranslation("location.pl"));
      this.streetRouterLink.setText(this.getTranslation("street.pl"));
    }
    this.gameRouterLink.setText(this.getTranslation("game.pl"));
    this.gymRouterLink.setText(this.getTranslation("gym.pl"));
    this.rankingLink.setText(this.getTranslation("ranking"));
    this.teamRouterLink.setText(this.getTranslation("team.pl"));
  }

  private void saveLocalePreference(Locale locale) {
    UI.getCurrent().setLocale(locale);
    VaadinService.getCurrentResponse().addCookie(new Cookie("locale", locale.toLanguageTag()));
    Notification.show(this.getTranslation("view.help.localesaved"));
  }
}

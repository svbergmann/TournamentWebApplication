package com.github.ProfSchmergmann.TournamentWebApplication.views;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login | Tournament")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  private final LoginForm login = new LoginForm();

  public LoginView() {
    this.addClassName("login-view");
    this.setSizeFull();
    this.setAlignItems(Alignment.CENTER);
    this.setJustifyContentMode(JustifyContentMode.CENTER);

    this.login.setAction("login");

    this.add(new H1("Tournament"), this.login);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
    // inform the user about an authentication error
    if (beforeEnterEvent.getLocation()
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
      this.login.setError(true);
    }
  }
}
/*
 * Copyright 2000-2017 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.ProfSchmergmann.TournamentWebApplication;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.i18n.LocaleChangeEvent;
import com.vaadin.flow.i18n.LocaleChangeObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import java.util.Locale;

@Route
public class MainView extends VerticalLayout implements LocaleChangeObserver {

  private final RouterLink link;

  public MainView() {
    H1 heading = new H1("Vaadin + Spring examples");

    Button button = new Button("Switch language to German",
        event -> getUI().get().setLocale(Locale.GERMAN));

    link = new RouterLink();

    Style linkStyle = link.getElement().getStyle();
    linkStyle.set("display", "block");
    linkStyle.set("margin-bottom", "10px");

    add(heading, button, link);
  }

  @Override
  public void localeChange(LocaleChangeEvent event) {
    link.setText(
        getTranslation("root.navigate_to_component"));
  }

}

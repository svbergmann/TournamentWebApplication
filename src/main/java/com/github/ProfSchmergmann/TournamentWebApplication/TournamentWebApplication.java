package com.github.ProfSchmergmann.TournamentWebApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@PWA(name = "Tournament Web Application",
		shortName = "Tournament")
public class TournamentWebApplication extends SpringBootServletInitializer implements
		AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(TournamentWebApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TournamentWebApplication.class);
	}

}



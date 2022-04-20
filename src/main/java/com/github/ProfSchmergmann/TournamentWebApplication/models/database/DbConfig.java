package com.github.ProfSchmergmann.TournamentWebApplication.models.database;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DbConfig {

	@Autowired
	Environment env;

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(this.env.getProperty("driverClassName"));
		dataSource.setUrl(this.env.getProperty("url"));
		dataSource.setUsername(this.env.getProperty("user"));
		dataSource.setPassword(this.env.getProperty("password"));
		return dataSource;
	}

}

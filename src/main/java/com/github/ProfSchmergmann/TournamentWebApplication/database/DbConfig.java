package com.github.ProfSchmergmann.TournamentWebApplication.database;

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
		dataSource.setDriverClassName(this.env.getProperty("spring.datasource.driver-class-name"));
		dataSource.setUrl(this.env.getProperty("spring.datasource.url"));
		dataSource.setUsername(this.env.getProperty("spring.datasource.username"));
		dataSource.setPassword(this.env.getProperty("spring.datasource.password"));
		return dataSource;
	}
}

package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {

	Country findByName(String name);
}

package com.github.ProfSchmergmann.TournamentWebApplication.models.database.service.city;

import com.github.ProfSchmergmann.TournamentWebApplication.models.location.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

	City findByName(String name);

}

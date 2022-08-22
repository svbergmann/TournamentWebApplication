package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService extends IModelService<City> {

	public CityService(@Autowired CityRepository repository) {
		super(repository);
	}

	public List<City> findAllFromCountry(Country country) {
		return this.repository
				.findAll()
				.stream()
				.filter(c -> c.getCountry().equals(country))
				.toList();
	}

	@Override
	public City findByName(String name) {
		return this.repository
				.findAll()
				.stream()
				.filter(c -> c.getName().equals(name))
				.findFirst()
				.orElse(null);
	}

	@Override
	public City update(City city, long cityId) {
		var cityDB = this.repository.findById(cityId);

		if (cityDB.isPresent()) {
			var c = cityDB.get();
			c.setName(city.getName());
			return this.repository.save(c);
		}
		return null;
	}
}

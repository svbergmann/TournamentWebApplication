package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.ModelService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CityService implements ModelService<City> {

	@Autowired
	private CityRepository repository;

	@Override
	public City create(City city) {
		if (this.repository.findByName(city.getName()) != null) {
			return null;
		}
		return this.repository.save(city);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<City> findAll() {
		return this.repository.findAll(Sort.by("name"));
	}

	@Override
	public City findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public City findByName(String name) {
		return this.repository.findByName(name);
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

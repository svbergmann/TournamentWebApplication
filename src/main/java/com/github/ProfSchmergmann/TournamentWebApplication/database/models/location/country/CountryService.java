package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.ModelService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CountryService implements ModelService<Country> {

	@Autowired
	private CountryRepository repository;

	@Override
	public Country create(Country country) {
		if (this.repository.findByName(country.getName()) != null) {
			return null;
		}
		return this.repository.save(country);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<Country> findAll() {
		return this.repository.findAll(Sort.by("name"));
	}

	@Override
	public Country findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public Country findByName(String name) {
		return this.repository.findByName(name);
	}

	@Override
	public Country update(Country country, long countryId) {
		var countryDB = this.repository.findById(countryId);

		if (countryDB.isPresent()) {
			var c = countryDB.get();
			c.setName(country.getName());
			return this.repository.save(c);
		}
		return null;
	}
}

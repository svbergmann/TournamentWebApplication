package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService implements IModelService<Country> {

	@Autowired
	private CountryRepository repository;

	@Override
	public Country create(Country country) {
		return this.repository.findAll().stream().anyMatch(c -> c.equals(country)) ?
		       null : this.repository.save(country);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<Country> findAll() {
		return this.repository.findAll(Sort.by("iso3Name"));
	}

	@Override
	public Country findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public Country findByName(String name) {
		return this.repository.findAll()
		                      .stream()
		                      .filter(c -> c.getName().equals(name))
		                      .findFirst()
		                      .orElse(null);
	}

	@Override
	public Country update(Country country, long countryId) {
		var countryDB = this.repository.findById(countryId);

		if (countryDB.isPresent()) {
			var c = countryDB.get();
			c.setIso3Name(country.getIso3Name());
			return this.repository.save(c);
		}
		return null;
	}

	public Country findByISO3Name(String iso3Name) {
		return this.repository.findAll()
		                      .stream()
		                      .filter(c -> c.getIso3Name().equals(iso3Name))
		                      .findFirst()
		                      .orElse(null);
	}
}

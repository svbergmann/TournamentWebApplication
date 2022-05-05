package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CountryService extends IModelService<Country> {

	public CountryService(@Autowired CountryRepository repository) {
		super(repository);
	}

	@Override
	public Country findByName(String name) {
		return this.repository.findAll()
		                      .stream()
		                      .filter(c -> c.getName(Locale.ENGLISH).equals(name))
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

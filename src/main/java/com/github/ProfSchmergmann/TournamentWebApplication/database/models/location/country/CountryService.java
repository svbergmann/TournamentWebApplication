package com.github.profschmergmann.tournamentwebapplication.database.models.location.country;

import com.github.profschmergmann.tournamentwebapplication.database.models.ModelService;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CountryService extends ModelService<Country> {

  public CountryService(@Autowired CountryRepository repository) {
    super(repository);
  }

  public Country findByISO3Name(String iso3Name) {
    return this.repository
        .findAll()
        .stream()
        .filter(c -> c.getIso3Name().equals(iso3Name))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Country findByName(String name) {
    return this.repository
        .findAll()
        .stream()
        .filter(c -> c.getName(Locale.ENGLISH).equals(name))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Country update(Country country, long countryId) {
    var countryDb = this.repository.findById(countryId);

    if (countryDb.isPresent()) {
      var c = countryDb.get();
      c.setIso3Name(country.getIso3Name());
      return this.repository.save(c);
    }
    return null;
  }
}

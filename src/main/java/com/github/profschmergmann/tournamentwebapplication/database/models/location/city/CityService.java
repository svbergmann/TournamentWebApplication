package com.github.profschmergmann.tournamentwebapplication.database.models.location.city;

import com.github.profschmergmann.tournamentwebapplication.database.models.IModelService;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.country.Country;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

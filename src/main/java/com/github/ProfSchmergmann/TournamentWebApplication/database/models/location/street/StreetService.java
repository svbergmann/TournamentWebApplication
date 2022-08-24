package com.github.profschmergmann.tournamentwebapplication.database.models.location.street;

import com.github.profschmergmann.tournamentwebapplication.database.models.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreetService extends ModelService<Street> {

  public StreetService(@Autowired StreetRepository repository) {
    super(repository);
  }

  @Override
  public Street findByName(String name) {
    return this.repository
        .findAll()
        .stream()
        .filter(s -> s.getName().equals(name))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Street update(Street street, long id) {
    var streetDb = this.repository.findById(id);

    if (streetDb.isPresent()) {
      var s = streetDb.get();
      s.setName(street.getName());
      return this.repository.save(s);
    }
    return null;
  }
}

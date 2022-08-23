package com.github.profschmergmann.tournamentwebapplication.database.models.location;

import com.github.profschmergmann.tournamentwebapplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService extends IModelService<Location> {

  public LocationService(@Autowired LocationRepository repository) {
    super(repository);
  }

  public Location find(int postalCode, String streetName, int number) {
    return this.findAll()
        .stream()
        .filter(l ->
            l.getStreet().getName().equals(streetName) &&
                l.getPostalCode() == postalCode &&
                l.getNumber() == number)
        .findFirst()
        .orElse(null);
  }

  @Override
  public Location findByName(String name) {
    return null;
  }

  @Override
  public Location update(Location location, long id) {
    var locationDB = this.repository.findById(id);

    if (locationDB.isPresent()) {
      var l = locationDB.get();
      l.setCity(location.getCity());
      l.setNumber(location.getNumber());
      l.setPostalCode(l.getPostalCode());
      l.setStreet(location.getStreet());
      return this.repository.save(l);
    }

    return null;
  }
}

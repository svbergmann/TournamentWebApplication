package com.github.profschmergmann.tournamentwebapplication.database.models.gym;

import com.github.profschmergmann.tournamentwebapplication.database.models.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GymService extends ModelService<Gym> {

  public GymService(@Autowired GymRepository repository) {
    super(repository);
  }

  @Override
  public Gym findByName(String name) {
    return this.repository
        .findAll()
        .stream()
        .filter(g -> g.getName().equals(name))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Gym update(Gym gym, long id) {
    var gymDb = this.repository.findById(id);

    if (gymDb.isPresent()) {
      var g = gymDb.get();
      g.setName(gym.getName());
      g.setCapacity(gym.getCapacity());
      g.setLocation(gym.getLocation());
      return this.repository.save(g);
    }
    return null;
  }
}

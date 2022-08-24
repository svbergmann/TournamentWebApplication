package com.github.profschmergmann.tournamentwebapplication.database.models.gender;

import com.github.profschmergmann.tournamentwebapplication.database.models.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenderService extends ModelService<Gender> {

  public GenderService(@Autowired GenderRepository genderRepository) {
    super(genderRepository);
  }

  @Override
  public Gender findByName(String name) {
    return this.repository
        .findAll()
        .stream()
        .filter(g -> g.getName().equals(name))
        .findFirst()
        .orElse(null);
  }

  @Override
  public Gender update(Gender gender, long id) {
    var genderDb = this.repository.findById(id);

    if (genderDb.isPresent()) {
      var g = genderDb.get();
      g.setName(gender.getName());
      return this.repository.save(g);
    }
    return null;
  }
}

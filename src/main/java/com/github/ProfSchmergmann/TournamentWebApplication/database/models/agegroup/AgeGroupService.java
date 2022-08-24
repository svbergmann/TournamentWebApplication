package com.github.profschmergmann.tournamentwebapplication.database.models.agegroup;

import com.github.profschmergmann.tournamentwebapplication.database.models.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgeGroupService extends ModelService<AgeGroup> {

  public AgeGroupService(@Autowired AgeGroupRepository repository) {
    super(repository);
  }

  @Override
  public AgeGroup findByName(String name) {
    return this.repository.findAll()
        .stream()
        .filter(a -> a.getName().equals(name))
        .findFirst()
        .orElse(null);
  }

  @Override
  public AgeGroup update(AgeGroup ageGroup, long id) {
    var ageGroupDb = this.repository.findById(id);

    if (ageGroupDb.isPresent()) {
      var a = ageGroupDb.get();
      a.setName(ageGroup.getName());
      return this.repository.save(a);
    }
    return null;
  }
}

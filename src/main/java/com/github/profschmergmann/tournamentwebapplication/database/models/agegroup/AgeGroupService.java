package com.github.profschmergmann.tournamentwebapplication.database.models.agegroup;

import com.github.profschmergmann.tournamentwebapplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgeGroupService extends IModelService<AgeGroup> {

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
    var ageGroupDB = this.repository.findById(id);

    if (ageGroupDB.isPresent()) {
      var a = ageGroupDB.get();
      a.setName(ageGroup.getName());
      return this.repository.save(a);
    }
    return null;
  }
}

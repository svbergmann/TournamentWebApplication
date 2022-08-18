package com.github.ProfSchmergmann.TournamentWebApplication.database.models.club;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClubService extends IModelService<Club> {

  public ClubService(@Autowired ClubRepository repository) {
    super(repository);
  }

  @Override
  public Club findByName(String name) {
    return null;
  }

  @Override
  public Club update(Club club, long id) {
    var clubDB = this.repository.findById(id);

    if (clubDB.isPresent()) {
      var c = clubDB.get();
      c.setName(club.getName());
      c.setCountry(club.getCountry());
      return this.repository.save(c);
    }
    return null;
  }
}

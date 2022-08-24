package com.github.profschmergmann.tournamentwebapplication.database.models.club;

import com.github.profschmergmann.tournamentwebapplication.database.models.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClubService extends ModelService<Club> {

  public ClubService(@Autowired ClubRepository repository) {
    super(repository);
  }

  @Override
  public Club findByName(String name) {
    return null;
  }

  @Override
  public Club update(Club club, long id) {
    var clubDb = this.repository.findById(id);

    if (clubDb.isPresent()) {
      var c = clubDb.get();
      c.setName(club.getName());
      c.setCountry(club.getCountry());
      return this.repository.save(c);
    }
    return null;
  }
}

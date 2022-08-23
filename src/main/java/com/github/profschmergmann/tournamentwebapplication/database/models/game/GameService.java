package com.github.profschmergmann.tournamentwebapplication.database.models.game;

import com.github.profschmergmann.tournamentwebapplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService extends IModelService<Game> {

  public GameService(@Autowired GameRepository repository) {
    super(repository);
  }

  @Override
  public Game findByName(String name) {
    return null;
  }

  @Override
  public Game update(Game game, long id) {
    var gameDB = this.repository.findById(id);

    if (gameDB.isPresent()) {
      var g = gameDB.get();
      g.setDate(game.getDate());
      g.setGym(game.getGym());
      g.setMatch(game.getMatch());
      return this.repository.save(g);
    }
    return null;
  }

}

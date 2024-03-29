package com.github.profschmergmann.tournamentwebapplication.database.models.game;

import com.github.profschmergmann.tournamentwebapplication.database.models.Model;
import com.github.profschmergmann.tournamentwebapplication.database.models.gym.Gym;
import com.github.profschmergmann.tournamentwebapplication.database.models.match.Match;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@Entity
public class Game implements Model {

  private Date date;
  @ManyToOne
  private Gym gym;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @ManyToOne
  private Match match;

  @Override
  public int hashCode() {
    return Objects.hash(this.date, this.gym, this.match);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Game game)) {
      return false;
    }
    return this.date.equals(game.date)
        && this.gym.equals(game.gym)
        && this.match.equals(game.match);
  }
}

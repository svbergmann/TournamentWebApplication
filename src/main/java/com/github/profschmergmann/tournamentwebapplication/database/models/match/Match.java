package com.github.profschmergmann.tournamentwebapplication.database.models.match;

import com.github.profschmergmann.tournamentwebapplication.database.models.Model;
import com.github.profschmergmann.tournamentwebapplication.database.models.game.Game;
import com.github.profschmergmann.tournamentwebapplication.database.models.team.Team;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@Entity
public class Match implements Model {

  private boolean finished;
  @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
  @Exclude
  private Set<Game> games;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private int scoreTeamA;
  private int scoreTeamB;
  @ManyToOne
  private Team teamA;
  @ManyToOne
  private Team teamB;

  @Override
  public int hashCode() {
    return Objects.hash(this.finished, this.scoreTeamA, this.scoreTeamB, this.teamA, this.teamB);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Match match)) {
      return false;
    }
    return this.finished == match.finished
        && this.scoreTeamA == match.scoreTeamA
        && this.scoreTeamB == match.scoreTeamB
        && this.teamA.equals(match.teamA)
        && this.teamB.equals(match.teamB);
  }
}

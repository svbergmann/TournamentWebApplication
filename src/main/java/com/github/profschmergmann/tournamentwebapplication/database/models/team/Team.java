package com.github.profschmergmann.tournamentwebapplication.database.models.team;

import com.github.profschmergmann.tournamentwebapplication.database.models.IModel;
import com.github.profschmergmann.tournamentwebapplication.database.models.agegroup.AgeGroup;
import com.github.profschmergmann.tournamentwebapplication.database.models.club.Club;
import com.github.profschmergmann.tournamentwebapplication.database.models.gender.Gender;
import com.github.profschmergmann.tournamentwebapplication.database.models.match.Match;
import java.io.Serializable;
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

@Getter
@Setter
@ToString
@Entity
public class Team implements Serializable, IModel {

  @ManyToOne
  private AgeGroup ageGroup;
  private int amount;
  @ManyToOne
  private Club club;
  @ManyToOne
  private Gender gender;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @OneToMany(mappedBy = "teamA", cascade = CascadeType.ALL, orphanRemoval = true)
  @Exclude
  private Set<Match> matchesA;
  @OneToMany(mappedBy = "teamB", cascade = CascadeType.ALL, orphanRemoval = true)
  @Exclude
  private Set<Match> matchesB;

  private String name;

  @Override
  public int hashCode() {
    return Objects.hash(this.ageGroup, this.amount, this.club, this.gender, this.name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Team team)) {
      return false;
    }
    return this.amount == team.amount &&
        Objects.equals(this.ageGroup, team.ageGroup) &&
        Objects.equals(this.club, team.club) &&
        Objects.equals(this.gender, team.gender) &&
        Objects.equals(this.name, team.name);
  }
}

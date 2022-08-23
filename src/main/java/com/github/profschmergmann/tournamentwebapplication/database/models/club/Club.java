package com.github.profschmergmann.tournamentwebapplication.database.models.club;

import com.github.profschmergmann.tournamentwebapplication.database.models.IModel;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.country.Country;
import com.github.profschmergmann.tournamentwebapplication.database.models.team.Team;
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
public class Club implements Serializable, IModel {

  @ManyToOne
  private Country country;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String name;
  @OneToMany(mappedBy = "club", cascade = CascadeType.ALL, orphanRemoval = true)
  @Exclude
  private Set<Team> teams;

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.country);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Club club)) {
      return false;
    }
    return Objects.equals(this.name, club.name) &&
        Objects.equals(this.country, club.country);
  }
}

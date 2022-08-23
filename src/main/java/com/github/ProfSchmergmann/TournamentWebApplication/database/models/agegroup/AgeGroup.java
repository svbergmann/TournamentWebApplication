package com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

@Getter
@Setter
@ToString
@Entity
public class AgeGroup implements Serializable, IModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String name;
  @OneToMany(mappedBy = "ageGroup", cascade = CascadeType.ALL, orphanRemoval = true)
  @Exclude
  private Set<Team> teams;

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof AgeGroup ageGroup)) {
      return false;
    }
    return Objects.equals(this.name.toLowerCase(), ageGroup.name.toLowerCase());
  }
}

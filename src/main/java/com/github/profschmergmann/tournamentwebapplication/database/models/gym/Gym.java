package com.github.profschmergmann.tournamentwebapplication.database.models.gym;

import com.github.profschmergmann.tournamentwebapplication.database.models.Model;
import com.github.profschmergmann.tournamentwebapplication.database.models.game.Game;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.Location;
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
public class Gym implements Model {

  private int capacity;
  @OneToMany(mappedBy = "gym", cascade = CascadeType.ALL, orphanRemoval = true)
  @Exclude
  private Set<Game> games;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @ManyToOne
  private Location location;
  private String name;
  private int number;

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.capacity, this.location);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Gym gym)) {
      return false;
    }
    return this.capacity == gym.capacity && Objects.equals(this.name, gym.name) && Objects.equals(
        this.location, gym.location);
  }
}

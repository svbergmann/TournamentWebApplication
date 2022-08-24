package com.github.profschmergmann.tournamentwebapplication.database.models.location;

import com.github.profschmergmann.tournamentwebapplication.database.models.Model;
import com.github.profschmergmann.tournamentwebapplication.database.models.gym.Gym;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.city.City;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.street.Street;
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
public class Location implements Model {

  @ManyToOne
  private City city;
  @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
  @Exclude
  private Set<Gym> gyms;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private int number;
  private int postalCode;
  @ManyToOne
  private Street street;

  @Override
  public int hashCode() {
    return Objects.hash(this.city, this.number, this.postalCode, this.street);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Location location)) {
      return false;
    }
    return this.number == location.number
        && this.postalCode == location.postalCode
        && this.city.equals(location.city)
        && this.street.equals(location.street);
  }
}

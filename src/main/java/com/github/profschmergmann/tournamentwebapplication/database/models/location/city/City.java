package com.github.profschmergmann.tournamentwebapplication.database.models.location.city;

import com.github.profschmergmann.tournamentwebapplication.database.models.Model;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.Location;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.country.Country;
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
public class City implements Model {

  @ManyToOne
  private Country country;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
  @Exclude
  private Set<Location> locations;
  private String name;

  @Override
  public int hashCode() {
    return Objects.hash(this.name, this.country);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof City city)) {
      return false;
    }
    return this.name.equalsIgnoreCase(city.name) && this.country.equals(city.country);
  }
}

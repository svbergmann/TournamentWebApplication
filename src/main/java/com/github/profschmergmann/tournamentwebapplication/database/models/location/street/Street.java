package com.github.profschmergmann.tournamentwebapplication.database.models.location.street;

import com.github.profschmergmann.tournamentwebapplication.database.models.Model;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.Location;
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
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@ToString
@Entity
public class Street implements Model {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @OneToMany(mappedBy = "street", cascade = CascadeType.ALL, orphanRemoval = true)
  @Exclude
  private Set<Location> locations;
  private String name;

  @Override
  public int hashCode() {
    return Objects.hash(this.name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Street street)) {
      return false;
    }
    return this.name.equalsIgnoreCase(street.name);
  }
}

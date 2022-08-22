package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.Gym;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street.Street;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@Entity
public class Location implements Serializable, IModel {

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
		return this.number == location.number &&
				this.postalCode == location.postalCode &&
				Objects.equals(this.city, location.city) &&
				Objects.equals(this.street, location.street);
	}
}

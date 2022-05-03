package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street.Street;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Location implements Serializable, IModel {

	@ManyToOne
	private City city;
	@ManyToOne
	private Country country;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private int number;
	private int postalCode;
	@ManyToOne
	private Street street;

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.country, this.city, this.street, this.number,
		                    this.postalCode);
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
				Objects.equals(this.country, location.country) &&
				Objects.equals(this.city, location.city) &&
				Objects.equals(this.street, location.street);
	}
}

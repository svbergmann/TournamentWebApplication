package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class City implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	@ManyToOne
	private Country country;

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(
			Country country) {
		this.country = country;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

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
		return Objects.equals(this.name, city.name) && Objects.equals(this.country,
				city.country);
	}
}

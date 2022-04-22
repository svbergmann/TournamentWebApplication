package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.country.Country;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street.Street;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

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

	public City getCity() {
		return this.city;
	}

	public void setCity(City city) {
		this.city = city;
	}

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

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getPostalCode() {
		return this.postalCode;
	}

	public void setPostalCode(int postalCode) {
		this.postalCode = postalCode;
	}

	public Street getStreet() {
		return this.street;
	}

	public void setStreet(Street street) {
		this.street = street;
	}

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
		return this.number == location.number && this.postalCode == location.postalCode
				&& Objects.equals(this.country, location.country) && Objects.equals(this.city,
				                                                                    location.city) && Objects.equals(this.street, location.street);
	}
}

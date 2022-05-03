package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
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
public class Street implements Serializable, IModel {

	@ManyToOne
	private City city;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String name;

	@Override
	public int hashCode() {
		return Objects.hash(this.id, this.name, this.city);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Street street)) {
			return false;
		}
		return Objects.equals(this.name, street.name) &&
				Objects.equals(this.city, street.city);
	}
}

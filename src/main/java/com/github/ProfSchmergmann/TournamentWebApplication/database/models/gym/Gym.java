package com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.Location;
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
public class Gym implements Serializable, IModel {

	private int capacity;
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
		return this.capacity == gym.capacity &&
				Objects.equals(this.name, gym.name) &&
				Objects.equals(this.location, gym.location);
	}
}

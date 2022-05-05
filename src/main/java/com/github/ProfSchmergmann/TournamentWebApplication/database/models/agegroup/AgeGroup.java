package com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class AgeGroup implements Serializable, IModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

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
		if (!(o instanceof AgeGroup ageGroup)) {
			return false;
		}
		return Objects.equals(this.name.toLowerCase(), ageGroup.name.toLowerCase());
	}
}

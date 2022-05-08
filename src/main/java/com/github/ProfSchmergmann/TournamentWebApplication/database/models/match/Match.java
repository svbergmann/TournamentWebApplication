package com.github.ProfSchmergmann.TournamentWebApplication.database.models.match;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.game.Game;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
public class Match implements Serializable, IModel {

	private boolean finished;
	@OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
	@Exclude
	private Set<Game> games;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private int scoreTeamA;
	private int scoreTeamB;
	@ManyToOne
	private Team teamA;
	@ManyToOne
	private Team teamB;

	@Override
	public int hashCode() {
		return Objects.hash(this.finished, this.scoreTeamA, this.scoreTeamB, this.teamA, this.teamB);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Match match)) return false;
		return this.finished == match.finished &&
				this.scoreTeamA == match.scoreTeamA &&
				this.scoreTeamB == match.scoreTeamB &&
				Objects.equals(this.teamA, match.teamA) &&
				Objects.equals(this.teamB, match.teamB);
	}
}

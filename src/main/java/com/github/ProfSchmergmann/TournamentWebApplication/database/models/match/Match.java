package com.github.ProfSchmergmann.TournamentWebApplication.database.models.match;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Match implements Serializable, IModel {

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
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getScoreTeamA() {
		return this.scoreTeamA;
	}

	public void setScoreTeamA(int scoreTeamA) {
		this.scoreTeamA = scoreTeamA;
	}

	public int getScoreTeamB() {
		return this.scoreTeamB;
	}

	public void setScoreTeamB(int scoreTeamB) {
		this.scoreTeamB = scoreTeamB;
	}

	public Team getTeamA() {
		return this.teamA;
	}

	public void setTeamA(Team teamA) {
		this.teamA = teamA;
	}

	public Team getTeamB() {
		return this.teamB;
	}

	public void setTeamB(Team teamB) {
		this.teamB = teamB;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.scoreTeamA, this.scoreTeamB, this.teamA, this.teamB);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Match match)) return false;
		return this.scoreTeamA == match.scoreTeamA &&
				this.scoreTeamB == match.scoreTeamB &&
				Objects.equals(this.teamA, match.teamA) &&
				Objects.equals(this.teamB, match.teamB);
	}

	@Override
	public String toString() {
		return "Match{" +
				"scoreTeamA=" + this.scoreTeamA +
				", scoreTeamB=" + this.scoreTeamB +
				", teamA=" + this.teamA +
				", teamB=" + this.teamB +
				'}';
	}
}

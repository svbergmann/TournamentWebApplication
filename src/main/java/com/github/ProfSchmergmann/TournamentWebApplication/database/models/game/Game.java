package com.github.ProfSchmergmann.TournamentWebApplication.database.models.game;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.Gym;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.team.Team;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
public class Game implements Serializable, IModel {

	private Date date;
	private boolean finished;
	@ManyToOne
	private Gym gym;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private int scoreTeamA;
	private int scoreTeamB;
	@ManyToOne
	private Team teamA;
	@ManyToOne
	private Team teamB;

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Gym getGym() {
		return this.gym;
	}

	public void setGym(Gym gym) {
		this.gym = gym;
	}

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
		return Objects.hash(this.date, this.finished, this.gym, this.scoreTeamA, this.scoreTeamB, this.teamA, this.teamB);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Game game)) return false;
		return this.finished == game.finished &&
				this.scoreTeamA == game.scoreTeamA &&
				this.scoreTeamB == game.scoreTeamB &&
				Objects.equals(this.date, game.date) &&
				Objects.equals(this.gym, game.gym) &&
				Objects.equals(this.teamA, game.teamA) &&
				Objects.equals(this.teamB, game.teamB);
	}

	public boolean isFinished() {
		return this.finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}

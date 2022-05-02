package com.github.ProfSchmergmann.TournamentWebApplication.database.models.game;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym.Gym;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.match.Match;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Game implements Serializable, IModel {

	private Date date;
	private boolean finished;
	@ManyToOne
	private Gym gym;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
	private Match match;

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

	public boolean isFinished() {
		return this.finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}
}

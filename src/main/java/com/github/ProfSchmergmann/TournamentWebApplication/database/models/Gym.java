package com.github.ProfSchmergmann.TournamentWebApplication.database.models;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.Location;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Gym {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	private int capacity;

	@ManyToOne
	private Location location;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
}

package com.github.ProfSchmergmann.TournamentWebApplication.database.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public enum AgeGroup {
	U10, U12, U14, U16, U18, U20, U22, MEN, MASTERS;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
}

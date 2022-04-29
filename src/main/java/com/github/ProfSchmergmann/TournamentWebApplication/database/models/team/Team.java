package com.github.ProfSchmergmann.TournamentWebApplication.database.models.team;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModel;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.club.Club;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class Team implements Serializable, IModel {

	@ManyToOne
	private AgeGroup ageGroup;
	private int amount;
	@ManyToOne
	private Club club;
	@ManyToOne
	private Gender gender;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;

	public AgeGroup getAgeGroup() {
		return this.ageGroup;
	}

	public void setAgeGroup(AgeGroup ageGroup) {
		this.ageGroup = ageGroup;
	}

	public int getAmount() {
		return this.amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public Club getClub() {
		return this.club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public Gender getGender() {
		return this.gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.ageGroup, this.amount, this.club, this.gender, this.name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Team team)) return false;
		return this.amount == team.amount &&
				Objects.equals(this.ageGroup, team.ageGroup) &&
				Objects.equals(this.club, team.club) &&
				Objects.equals(this.gender, team.gender) &&
				Objects.equals(this.name, team.name);
	}

	@Override
	public String toString() {
		return "Team{" +
				"ageGroup=" + this.ageGroup +
				", amount=" + this.amount +
				", club=" + this.club +
				", gender=" + this.gender +
				", name='" + this.name + '\'' +
				'}';
	}
}

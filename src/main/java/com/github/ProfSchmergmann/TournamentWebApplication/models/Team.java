package com.github.ProfSchmergmann.TournamentWebApplication.models;

import com.github.ProfSchmergmann.TournamentWebApplication.models.location.Country;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Team {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Integer amount;

  private String name;

  private AgeGroup ageGroup;

  private Gender gender;

  @ManyToOne
  private Country country;

  public Country getCountry() {
    return country;
  }

  public void setCountry(
      Country country) {
    this.country = country;
  }

  public Gender getGender() {
    return gender;
  }

  public void setGender(Gender gender) {
    this.gender = gender;
  }

  public AgeGroup getAgeGroup() {
    return ageGroup;
  }

  public void setAgeGroup(AgeGroup ageGroup) {
    this.ageGroup = ageGroup;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}

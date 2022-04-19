package com.github.ProfSchmergmann.TournamentWebApplication.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public enum AgeGroup {
  U10, U12, U14, U16, U18, U20, U22, MEN, MASTERS;
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}

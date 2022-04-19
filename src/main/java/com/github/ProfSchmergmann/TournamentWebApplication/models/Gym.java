package com.github.ProfSchmergmann.TournamentWebApplication.models;

import com.github.ProfSchmergmann.TournamentWebApplication.models.location.Location;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Gym {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String name;

  private int capacity;

  @ManyToOne
  private Location location;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}

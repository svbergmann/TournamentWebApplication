package com.github.ProfSchmergmann.TournamentWebApplication.models.location;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Location {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private Country country;

  @ManyToOne
  private City city;
  @ManyToOne
  private Street street;
  private int number;
  private int postalCode;

  public Street getStreet() {
    return street;
  }

  public void setStreet(Street street) {
    this.street = street;
  }

  public City getCity() {
    return city;
  }

  public void setCity(City city) {
    this.city = city;
  }

  public int getNumber() {
    return number;
  }

  public void setNumber(int number) {
    this.number = number;
  }

  public int getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(int postalCode) {
    this.postalCode = postalCode;
  }

  public Country getCountry() {
    return country;
  }

  public void setCountry(
      Country country) {
    this.country = country;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}

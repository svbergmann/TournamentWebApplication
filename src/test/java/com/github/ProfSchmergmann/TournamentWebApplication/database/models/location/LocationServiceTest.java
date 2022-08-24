package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.profschmergmann.tournamentwebapplication.database.models.location.Location;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.LocationService;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.city.City;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.city.CityService;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.country.Country;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.country.CountryService;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.street.Street;
import com.github.profschmergmann.tournamentwebapplication.database.models.location.street.StreetService;
import com.vaadin.flow.internal.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class LocationServiceTest {

  public static final HashMap<Pair<String, Integer>, Integer> STREET_NAMES_AND_PLZ =
      new HashMap<>() {{
        this.put(new Pair<>("Alt-Moabit", 10), 10557);
        this.put(new Pair<>("Gerhardtstr.", 4), 10557);
        this.put(new Pair<>("Rathenower Str.", 18), 10559);
        this.put(new Pair<>("Turmstr.", 85), 10559);
        this.put(new Pair<>("Siemensstr.", 20), 10551);
        this.put(new Pair<>("Altonaer Str.", 26), 10555);
        this.put(new Pair<>("Wullenweber Str.", 15), 10555);
        this.put(new Pair<>("Lützowstr.", 83), 10785);
      }};

  @Test
  @Order(4)
  public void createEastercupLocations(@Autowired StreetService streetService,
      @Autowired CityService cityService,
      @Autowired LocationService locationService,
      @Autowired CountryService countryService) {
    var berlin = cityService.findByName("Berlin");
    var locations = new ArrayList<Location>();
    for (var entry : STREET_NAMES_AND_PLZ.entrySet()) {
      var street = streetService.findByName(entry.getKey().getFirst());
      var location = new Location();
      location.setCity(berlin);
      location.setPostalCode(entry.getValue());
      location.setStreet(street);
      location.setNumber(entry.getKey().getSecond());
      locationService.create(location);
      locations.add(location);
    }
    var counter = (int) locationService.findAll().stream().filter(locations::contains).count();
    assertEquals(8, counter);
  }

  @Test
  @Order(3)
  public void createEastercupStreets(@Autowired StreetService streetService) {
    STREET_NAMES_AND_PLZ.keySet().forEach(streetName -> {
      var s = new Street();
      s.setName(streetName.getFirst());
      streetService.create(s);
    });
    var counter = (int) STREET_NAMES_AND_PLZ.keySet()
        .stream()
        .filter(name -> streetService.findByName(name.getFirst()) != null)
        .count();
    assertEquals(8, counter);
  }

  @Test
  @Order(2)
  public void createTestCity(@Autowired CountryService countryService,
      @Autowired CityService cityService) {
    var cityName = "Berlin";
    var berlin = new City();
    berlin.setCountry(countryService.findByISO3Name("DEU"));
    berlin.setName(cityName);
    cityService.create(berlin);
    assertNotNull(cityService.findByName(cityName));
  }

  @Test
  @Order(1)
  public void createTestCountry(@Autowired CountryService countryService) {
    var countryISO3Name = "DEU";
    var de = new Country();
    de.setIso3Name(countryISO3Name);
    countryService.create(de);
    assertNotNull(countryService.findByISO3Name(countryISO3Name));
  }
}
package com.github.ProfSchmergmann.TournamentWebApplication.models.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.ProfSchmergmann.TournamentWebApplication.models.database.service.city.CityRepository;
import com.github.ProfSchmergmann.TournamentWebApplication.models.database.service.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.models.location.City;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

	@InjectMocks
	CityService cityService;
	@Mock
	CityRepository cityRepository;
	private City berlin;
	private City aachen;
	private City aschaffenburg;

	@BeforeEach
	void beforeEach() {
		this.berlin = new City();
		this.berlin.setName("Berlin");
		this.aachen = new City();
		this.aachen.setName("Aachen");
		this.aschaffenburg = new City();
		this.aschaffenburg.setName("Aschaffenburg");
	}

	@Test
	void saveCity() {
		this.cityService.saveCity(this.berlin);
		verify(this.cityRepository).save(this.berlin);
	}

	@Test
	void fetchCitiesList() {
		var list = new ArrayList<City>();
		list.add(this.berlin);
		list.add(this.aachen);
		list.add(this.aschaffenburg);
		when(this.cityRepository.findAll()).thenReturn(list);

		var cityList = this.cityService.findAll();

		assertEquals(3, cityList.size());
		verify(this.cityRepository).findAll();
	}

	@Test
	void deleteCityById() {
		when(this.cityService.saveCity(this.aachen)).thenReturn(this.aachen);
		var savedCity = this.cityService.saveCity(this.aachen);
		this.cityService.deleteCityById(savedCity.getId());
		Assertions.assertNull(this.cityService.findCityByName("Aachen"));
	}
}
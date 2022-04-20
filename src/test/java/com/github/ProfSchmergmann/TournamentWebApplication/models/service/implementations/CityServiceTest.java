package com.github.ProfSchmergmann.TournamentWebApplication.models.service.implementations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.github.ProfSchmergmann.TournamentWebApplication.models.database.service.city.CityRepository;
import com.github.ProfSchmergmann.TournamentWebApplication.models.database.service.city.CityService;
import com.github.ProfSchmergmann.TournamentWebApplication.models.location.City;
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

	@Test
	void saveCity() {
		var city = new City();
		city.setName("Berlin");
		this.cityService.saveCity(city);
		verify(this.cityRepository, times(1)).save(city);
	}

	@Test
	void fetchCitiesList() {
	}

	@Test
	void updateCity() {
	}

	@Test
	void deleteCityById() {
	}
}
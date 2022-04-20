package com.github.ProfSchmergmann.TournamentWebApplication.models.database.service.city;

import com.github.ProfSchmergmann.TournamentWebApplication.models.location.City;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;

	public City saveCity(City city) {
		return this.cityRepository.save(city);
	}

	public List<City> findAll() {
		return this.cityRepository.findAll();
	}

	public City updateCity(City city, Long cityId) {
		var cityDB = this.cityRepository.findById(cityId).get();

		if (Objects.nonNull(city.getName()) && !"".equalsIgnoreCase(city.getName())) {
			cityDB.setName(city.getName());
		}

		return this.cityRepository.save(cityDB);
	}

	public void deleteCityById(Long cityId) {
		this.cityRepository.deleteById(cityId);
	}

	public City findCityByName(String name) {
		return this.cityRepository.findByName(name);
	}
}

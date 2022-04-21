package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.city.City;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class StreetService implements IModelService<Street> {

	@Autowired
	private StreetRepository repository;

	@Override
	public Street create(Street street) {
		if (this.repository.findByName(street.getName()) != null) {
			return null;
		}
		return this.repository.save(street);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<Street> findAll() {
		return this.repository.findAll(Sort.by("name"));
	}

	@Override
	public Street findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public Street findByName(String name) {
		return this.repository.findByName(name);
	}

	@Override
	public Street update(Street street, long id) {
		var streetDB = this.repository.findById(id);

		if (streetDB.isPresent()) {
			var s = streetDB.get();
			s.setName(street.getName());
			return this.repository.save(s);
		}
		return null;
	}

	public List<Street> findAllFromCity(City city) {
		return this.repository.findAll().stream().filter(s -> s.getCity().equals(city)).toList();
	}
}

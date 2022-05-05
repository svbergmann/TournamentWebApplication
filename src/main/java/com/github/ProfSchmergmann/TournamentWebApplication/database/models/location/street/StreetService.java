package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location.street;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StreetService extends IModelService<Street> {
	public StreetService(@Autowired StreetRepository repository) {
		super(repository);
	}

	@Override
	public Street findByName(String name) {
		return this.repository
				.findAll()
				.stream()
				.filter(s -> s.getName().equals(name))
				.findFirst()
				.orElse(null);
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
}

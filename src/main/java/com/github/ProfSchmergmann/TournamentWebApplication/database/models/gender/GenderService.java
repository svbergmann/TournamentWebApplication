package com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GenderService extends IModelService<Gender> {

	public GenderService(@Autowired GenderRepository genderRepository) {
		super(genderRepository);
	}

	@Override
	public Gender findByName(String name) {
		return this.repository
				.findAll()
				.stream()
				.filter(g -> g.getName().equals(name))
				.findFirst()
				.orElse(null);
	}

	@Override
	public Gender update(Gender gender, long id) {
		var genderDB = this.repository.findById(id);

		if (genderDB.isPresent()) {
			var g = genderDB.get();
			g.setName(gender.getName());
			return this.repository.save(g);
		}
		return null;
	}
}

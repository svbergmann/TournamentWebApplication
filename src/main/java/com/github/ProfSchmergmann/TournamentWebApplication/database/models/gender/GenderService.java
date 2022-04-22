package com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenderService implements IModelService<Gender> {

	@Autowired
	private GenderRepository repository;

	@Override
	public Gender create(Gender gender) {
		return this.findAll().stream().anyMatch(l -> l.equals(gender)) ?
		       null : this.repository.save(gender);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<Gender> findAll() {
		return this.repository.findAll();
	}

	@Override
	public Gender findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public Gender findByName(String name) {
		return null;
	}

	@Override
	public Gender update(Gender gender, long id) {
		var genderDB = this.repository.findById(id);

		if (genderDB.isPresent()) {
			var a = genderDB.get();
			a.setId(gender.getId());
			a.setName(gender.getName());
			this.repository.deleteById(id);
			return this.repository.save(a);
		}
		return null;
	}
}

package com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AgeGroupService implements IModelService<AgeGroup> {

	@Autowired
	private AgeGroupRepository repository;

	@Override
	public AgeGroup create(AgeGroup ageGroup) {
		return this.findAll().stream().anyMatch(l -> l.equals(ageGroup)) ?
		       null : this.repository.save(ageGroup);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<AgeGroup> findAll() {
		return this.repository.findAll();
	}

	@Override
	public AgeGroup findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public AgeGroup findByName(String name) {
		return null;
	}

	@Override
	public AgeGroup update(AgeGroup ageGroup, long id) {
		var ageGroupDB = this.repository.findById(id);

		if (ageGroupDB.isPresent()) {
			var a = ageGroupDB.get();
			a.setId(ageGroup.getId());
			a.setName(ageGroup.getName());
			this.repository.deleteById(id);
			return this.repository.save(a);
		}
		return null;
	}
}

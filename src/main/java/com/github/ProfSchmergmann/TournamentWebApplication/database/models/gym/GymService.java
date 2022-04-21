package com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GymService implements IModelService<Gym> {

	@Autowired
	private GymRepository repository;

	@Override
	public Gym create(Gym gym) {
		if (this.findAll().stream().anyMatch(l -> l.equals(gym))) {
			return null;
		}
		return this.repository.save(gym);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<Gym> findAll() {
		return this.repository.findAll();
	}

	@Override
	public Gym findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public Gym findByName(String name) {
		return null;
	}

	@Override
	public Gym update(Gym gym, long id) {
		var gymDB = this.repository.findById(id);

		if (gymDB.isPresent()) {
			var g = gymDB.get();
			g.setId(gym.getId());
			g.setName(gym.getName());
			g.setCapacity(gym.getCapacity());
			g.setLocation(gym.getLocation());
			this.repository.deleteById(id);
			return this.repository.save(g);
		}
		return null;
	}
}

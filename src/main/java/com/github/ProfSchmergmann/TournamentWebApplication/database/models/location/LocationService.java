package com.github.ProfSchmergmann.TournamentWebApplication.database.models.location;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService implements IModelService<Location> {

	@Autowired
	private LocationRepository repository;

	@Override
	public Location create(Location location) {
		return this.findAll().stream().anyMatch(l -> l.equals(location)) ?
		       null : this.repository.save(location);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<Location> findAll() {
		return this.repository.findAll();
	}

	@Override
	public Location findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public Location findByName(String name) {
		return null;
	}

	@Override
	public Location update(Location location, long id) {
		var locationDB = this.repository.findById(id);

		if (locationDB.isPresent()) {
			var l = locationDB.get();
			l.setCity(location.getCity());
			l.setCountry(location.getCountry());
			l.setId(location.getId());
			l.setNumber(location.getNumber());
			l.setPostalCode(l.getPostalCode());
			l.setStreet(location.getStreet());
			this.repository.deleteById(id);
			return this.repository.save(l);
		}

		return null;
	}
}

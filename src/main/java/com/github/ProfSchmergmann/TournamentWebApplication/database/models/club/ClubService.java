package com.github.ProfSchmergmann.TournamentWebApplication.database.models.club;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.IModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService implements IModelService<Club> {

	@Autowired
	private ClubRepository repository;

	@Override
	public Club create(Club club) {
		return this.findAll().stream().anyMatch(l -> l.equals(club)) ?
		       null : this.repository.save(club);
	}

	@Override
	public void deleteById(long id) {
		this.repository.deleteById(id);
	}

	@Override
	public List<Club> findAll() {
		return this.repository.findAll();
	}

	@Override
	public Club findById(long id) {
		return this.repository.findById(id).orElse(null);
	}

	@Override
	public Club findByName(String name) {
		return null;
	}

	@Override
	public Club update(Club club, long id) {
		var clubDB = this.repository.findById(id);

		if (clubDB.isPresent()) {
			var c = clubDB.get();
			c.setId(club.getId());
			c.setName(club.getName());
			c.setCountry(club.getCountry());
			this.repository.deleteById(id);
			return this.repository.save(c);
		}
		return null;
	}
}

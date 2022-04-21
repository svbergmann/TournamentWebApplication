package com.github.ProfSchmergmann.TournamentWebApplication.database.models;

import java.util.List;

public interface IModelService<T> {

	T create(T t);

	void deleteById(long id);

	List<T> findAll();

	T findById(long id);

	T findByName(String name);

	T update(T t, long id);

}

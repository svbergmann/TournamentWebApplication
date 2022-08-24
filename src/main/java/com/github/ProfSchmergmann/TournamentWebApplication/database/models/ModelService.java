package com.github.profschmergmann.tournamentwebapplication.database.models;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class ModelService<T> {

  protected JpaRepository<T, Long> repository;

  public ModelService(JpaRepository<T, Long> repository) {
    this.repository = repository;
  }

  public T create(T t) {
    for (T element : this.repository.findAll()) {
      if (element.equals(t)) {
        return element;
      }
    }
    return this.repository.save(t);
  }

  public void deleteById(long id) {
    this.repository.deleteById(id);
  }

  public List<T> findAll() {
    return this.repository.findAll();
  }

  public T findById(long id) {
    return this.repository.findById(id).orElse(null);
  }

  public abstract T findByName(String name);

  public abstract T update(T t, long id);

}

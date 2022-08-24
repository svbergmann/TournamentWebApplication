package com.github.profschmergmann.tournamentwebapplication.database.models.location.street;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {

  Street findByName(String name);
}
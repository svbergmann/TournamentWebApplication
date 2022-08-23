package com.github.profschmergmann.tournamentwebapplication.database.models.gender;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {

  Gender findByName(String name);
}
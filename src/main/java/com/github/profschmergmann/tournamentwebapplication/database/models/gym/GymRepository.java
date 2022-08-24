package com.github.profschmergmann.tournamentwebapplication.database.models.gym;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {

}
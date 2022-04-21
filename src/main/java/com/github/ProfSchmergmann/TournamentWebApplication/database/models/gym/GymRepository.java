package com.github.ProfSchmergmann.TournamentWebApplication.database.models.gym;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymRepository extends JpaRepository<Gym, Long> {

}
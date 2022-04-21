package com.github.ProfSchmergmann.TournamentWebApplication.database.models.gender;

import com.github.ProfSchmergmann.TournamentWebApplication.database.models.agegroup.AgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenderRepository extends JpaRepository<Gender, Long> {

}
package com.github.ProfSchmergmann.TournamentWebApplication.models.database.service.street;

import com.github.ProfSchmergmann.TournamentWebApplication.models.location.Street;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreetRepository extends JpaRepository<Street, Long> {

}
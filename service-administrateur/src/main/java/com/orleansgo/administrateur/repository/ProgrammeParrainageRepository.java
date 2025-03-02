
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.ProgrammeParrainage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ProgrammeParrainageRepository extends JpaRepository<ProgrammeParrainage, Long> {
    @Query("SELECT p FROM ProgrammeParrainage p WHERE p.actif = true AND :date BETWEEN p.dateDebut AND COALESCE(p.dateFin, :date) ORDER BY p.dateDebut DESC LIMIT 1")
    Optional<ProgrammeParrainage> findActiveProgrammeAtDate(LocalDateTime date);
    
    default Optional<ProgrammeParrainage> findCurrentProgramme() {
        return findActiveProgrammeAtDate(LocalDateTime.now());
    }
}

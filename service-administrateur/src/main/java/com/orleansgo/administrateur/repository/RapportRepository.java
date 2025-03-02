
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.Rapport;
import com.orleansgo.administrateur.model.TypeRapport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RapportRepository extends JpaRepository<Rapport, String> {
    List<Rapport> findByType(TypeRapport type);
    List<Rapport> findByDateGenerationBetween(LocalDateTime debut, LocalDateTime fin);
    List<Rapport> findByAdministrateurId(String administrateurId);
}

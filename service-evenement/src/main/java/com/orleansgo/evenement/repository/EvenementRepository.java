
package com.orleansgo.evenement.repository;

import com.orleansgo.evenement.model.Evenement;
import com.orleansgo.evenement.model.StatutEvenement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EvenementRepository extends JpaRepository<Evenement, Long> {

    List<Evenement> findByStatut(StatutEvenement statut);
    
    List<Evenement> findByPublicationActiveTrue();
    
    @Query("SELECT e FROM Evenement e WHERE e.dateDebut >= :debut AND e.dateFin <= :fin")
    List<Evenement> findByPeriode(LocalDateTime debut, LocalDateTime fin);
    
    List<Evenement> findByCreateurId(Long createurId);
    
    @Query("SELECT e FROM Evenement e JOIN e.categories c WHERE c = :categorie")
    List<Evenement> findByCategorie(String categorie);
    
    Page<Evenement> findByTitreContainingIgnoreCase(String titre, Pageable pageable);
    
    @Query("SELECT e FROM Evenement e WHERE e.statut = 'PLANIFIE' AND e.dateDebut <= :maintenant")
    List<Evenement> findEventsToDeploy(LocalDateTime maintenant);
    
    @Query("SELECT e FROM Evenement e WHERE e.statut = 'EN_COURS' AND e.dateFin <= :maintenant")
    List<Evenement> findEventsToClose(LocalDateTime maintenant);
}

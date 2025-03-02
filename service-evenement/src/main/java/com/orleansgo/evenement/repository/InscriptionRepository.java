
package com.orleansgo.evenement.repository;

import com.orleansgo.evenement.model.Inscription;
import com.orleansgo.evenement.model.StatutInscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    List<Inscription> findByEvenementId(Long evenementId);
    
    List<Inscription> findByUtilisateurId(Long utilisateurId);
    
    Optional<Inscription> findByEvenementIdAndUtilisateurId(Long evenementId, Long utilisateurId);
    
    List<Inscription> findByStatut(StatutInscription statut);
    
    @Query("SELECT COUNT(i) FROM Inscription i WHERE i.evenement.id = :evenementId AND i.statut = 'CONFIRMEE'")
    Integer countConfirmedInscriptionsByEvenementId(Long evenementId);
    
    List<Inscription> findByUtilisateurIdAndStatut(Long utilisateurId, StatutInscription statut);
    
    List<Inscription> findByEvenementIdAndStatut(Long evenementId, StatutInscription statut);
    
    @Query("SELECT i FROM Inscription i WHERE i.evenement.id = :evenementId AND i.statut = 'EN_ATTENTE' ORDER BY i.dateInscription ASC")
    List<Inscription> findPendingInscriptionsByEventId(Long evenementId);
}

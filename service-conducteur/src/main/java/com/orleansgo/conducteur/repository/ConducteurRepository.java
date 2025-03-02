
package com.orleansgo.conducteur.repository;

import com.orleansgo.conducteur.model.Conducteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ConducteurRepository extends JpaRepository<Conducteur, UUID> {
    Optional<Conducteur> findByUtilisateurId(UUID utilisateurId);
    List<Conducteur> findByDisponibleTrue();
    List<Conducteur> findByDocumentsValidesTrue();
}
package com.orleansgo.conducteur.repository;

import com.orleansgo.conducteur.model.Conducteur;
import com.orleansgo.conducteur.model.StatutConducteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConducteurRepository extends JpaRepository<Conducteur, Long> {
    
    Optional<Conducteur> findByUtilisateurId(Long utilisateurId);
    
    List<Conducteur> findByStatut(StatutConducteur statut);
    
    List<Conducteur> findByDisponibleTrue();
    
    @Query("SELECT c FROM Conducteur c WHERE c.disponible = true AND c.statut = 'ACTIF' " +
           "AND ST_Distance(ST_MakePoint(c.latitude, c.longitude), ST_MakePoint(:latitude, :longitude)) <= :distanceKm")
    List<Conducteur> findNearbyAvailableDrivers(Double latitude, Double longitude, Double distanceKm);
    
    @Query("SELECT AVG(c.noteGlobale) FROM Conducteur c WHERE c.statut = 'ACTIF'")
    Double getAverageRating();
    
    @Query("SELECT COUNT(c) FROM Conducteur c WHERE c.statut = 'ACTIF'")
    Long countActiveDrivers();
}

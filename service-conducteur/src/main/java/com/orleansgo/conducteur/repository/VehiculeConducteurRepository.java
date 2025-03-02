
package com.orleansgo.conducteur.repository;

import com.orleansgo.conducteur.model.StatutVehiculeConducteur;
import com.orleansgo.conducteur.model.VehiculeConducteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculeConducteurRepository extends JpaRepository<VehiculeConducteur, Long> {
    
    List<VehiculeConducteur> findByConducteurId(Long conducteurId);
    
    List<VehiculeConducteur> findByConducteurIdAndStatut(Long conducteurId, StatutVehiculeConducteur statut);
    
    Optional<VehiculeConducteur> findByConducteurIdAndVehiculePrincipalTrue(Long conducteurId);
    
    List<VehiculeConducteur> findByVehiculeId(Long vehiculeId);
    
    @Query("SELECT vc FROM VehiculeConducteur vc WHERE vc.conducteur.id = :conducteurId AND vc.statut = 'ACTIF'")
    List<VehiculeConducteur> findActiveVehiclesByConducteurId(Long conducteurId);
}

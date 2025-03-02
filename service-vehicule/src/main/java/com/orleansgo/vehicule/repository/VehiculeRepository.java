
package com.orleansgo.vehicule.repository;

import com.orleansgo.vehicule.model.StatutVehicule;
import com.orleansgo.vehicule.model.TypeVehicule;
import com.orleansgo.vehicule.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    
    List<Vehicule> findByChauffeurId(Long chauffeurId);
    
    List<Vehicule> findByStatut(StatutVehicule statut);
    
    List<Vehicule> findByTypeVehicule(TypeVehicule typeVehicule);
    
    List<Vehicule> findByTypeVehiculeAndStatut(TypeVehicule typeVehicule, StatutVehicule statut);
}

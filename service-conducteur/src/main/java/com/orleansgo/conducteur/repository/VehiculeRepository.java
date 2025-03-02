
package com.orleansgo.conducteur.repository;

import com.orleansgo.conducteur.model.Conducteur;
import com.orleansgo.conducteur.model.TypeVehicule;
import com.orleansgo.conducteur.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehiculeRepository extends JpaRepository<Vehicule, UUID> {
    List<Vehicule> findByConducteur(Conducteur conducteur);
    Optional<Vehicule> findByImmatriculation(String immatriculation);
    List<Vehicule> findByTypeVehiculeAndDisponibleTrue(TypeVehicule typeVehicule);
}

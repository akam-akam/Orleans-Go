
package com.orleansgo.conducteur.repository;

import com.orleansgo.conducteur.model.TypeVehicule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TypeVehiculeRepository extends JpaRepository<TypeVehicule, UUID> {
    Optional<TypeVehicule> findByNom(String nom);
}


package com.orleansgo.conducteur.repository;

import com.orleansgo.conducteur.model.TypeCarburant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TypeCarburantRepository extends JpaRepository<TypeCarburant, UUID> {
    Optional<TypeCarburant> findByNom(String nom);
}

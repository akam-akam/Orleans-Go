
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

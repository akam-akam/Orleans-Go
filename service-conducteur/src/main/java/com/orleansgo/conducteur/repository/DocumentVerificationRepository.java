
package com.orleansgo.conducteur.repository;

import com.orleansgo.conducteur.model.Conducteur;
import com.orleansgo.conducteur.model.DocumentVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, UUID> {
    Optional<DocumentVerification> findByConducteur(Conducteur conducteur);
    List<DocumentVerification> findByValideParAdminFalse();
}

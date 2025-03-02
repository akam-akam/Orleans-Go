
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.StatutVerification;
import com.orleansgo.administrateur.model.VerificationDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationDocumentsRepository extends JpaRepository<VerificationDocuments, String> {
    List<VerificationDocuments> findByChauffeurId(String chauffeurId);
    List<VerificationDocuments> findByStatut(StatutVerification statut);
    List<VerificationDocuments> findByAdministrateurId(String administrateurId);
}
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.StatutVerification;
import com.orleansgo.administrateur.model.VerificationDocuments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VerificationDocumentsRepository extends JpaRepository<VerificationDocuments, UUID> {
    List<VerificationDocuments> findByChauffeurId(UUID chauffeurId);
    List<VerificationDocuments> findByStatut(StatutVerification statut);
}

package com.orleansgo.utilisateur.repository;


import com.orleansgo.utilisateur.model.HistoriqueTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface HistoriqueTransactionRepository extends JpaRepository<HistoriqueTransaction, UUID> {
    List<HistoriqueTransaction> findByUtilisateurId(UUID utilisateurId);
}

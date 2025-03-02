
package com.orleansgo.conducteur.repository;

import com.orleansgo.conducteur.model.Document;
import com.orleansgo.conducteur.model.StatutDocument;
import com.orleansgo.conducteur.model.TypeDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    
    List<Document> findByConducteurId(Long conducteurId);
    
    List<Document> findByConducteurIdAndStatut(Long conducteurId, StatutDocument statut);
    
    List<Document> findByConducteurIdAndType(Long conducteurId, TypeDocument type);
    
    @Query("SELECT d FROM Document d WHERE d.dateExpiration < :date AND d.statut = 'VALIDE'")
    List<Document> findExpiredDocuments(LocalDateTime date);
    
    List<Document> findByStatut(StatutDocument statut);
}

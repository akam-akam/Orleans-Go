
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByUsernameAndTimestampBetween(String username, LocalDateTime debut, LocalDateTime fin);
    List<AuditLog> findByEntityTypeAndEntityId(String entityType, String entityId);
    List<AuditLog> findByActionType(String actionType);
}
package com.orleansgo.administrateur.repository;

import com.orleansgo.administrateur.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    List<AuditLog> findByUsername(String username);
    
    List<AuditLog> findByEntite(String entite);
    
    List<AuditLog> findByAction(String action);
    
    List<AuditLog> findByDateCreationBetween(LocalDateTime debut, LocalDateTime fin);
    
    Page<AuditLog> findByUsernameContainingIgnoreCaseOrderByDateCreationDesc(String username, Pageable pageable);
    
    Page<AuditLog> findByEntiteAndEntiteId(String entite, String entiteId, Pageable pageable);
    
    Page<AuditLog> findByActionAndDateCreationBetween(String action, LocalDateTime debut, LocalDateTime fin, Pageable pageable);
}

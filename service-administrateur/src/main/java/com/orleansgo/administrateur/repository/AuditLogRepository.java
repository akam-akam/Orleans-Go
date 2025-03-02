
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


package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.model.AuditLog;
import com.orleansgo.administrateur.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    /**
     * Enregistre une action dans les logs d'audit
     */
    @Transactional
    public void logAction(String actionType, String entityType, String entityId, String username, String details) {
        String ipAddress = getClientIpAddress();
        
        AuditLog auditLog = AuditLog.builder()
                .actionType(actionType)
                .entityType(entityType)
                .entityId(entityId)
                .username(username)
                .timestamp(LocalDateTime.now())
                .details(details)
                .ipAddress(ipAddress)
                .build();
        
        auditLogRepository.save(auditLog);
    }

    /**
     * Récupère les logs d'audit filtrés par utilisateur et période
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByUser(String username, LocalDateTime debut, LocalDateTime fin) {
        return auditLogRepository.findByUsernameAndTimestampBetween(username, debut, fin);
    }

    /**
     * Récupère les logs d'audit filtrés par entité
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByEntity(String entityType, String entityId) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId);
    }

    /**
     * Récupère les logs d'audit filtrés par type d'action
     */
    @Transactional(readOnly = true)
    public List<AuditLog> getAuditLogsByAction(String actionType) {
        return auditLogRepository.findByActionType(actionType);
    }

    /**
     * Récupère l'adresse IP du client
     */
    private String getClientIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String ipAddress = request.getHeader("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            return ipAddress;
        }
        return "unknown";
    }
}

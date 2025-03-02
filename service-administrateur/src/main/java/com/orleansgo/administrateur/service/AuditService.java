
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
package com.orleansgo.administrateur.service;

import com.orleansgo.administrateur.dto.AuditLogDTO;
import com.orleansgo.administrateur.model.AuditLog;
import com.orleansgo.administrateur.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    public AuditLogDTO createAuditLog(AuditLogDTO auditLogDTO) {
        AuditLog auditLog = convertToEntity(auditLogDTO);
        auditLog = auditLogRepository.save(auditLog);
        return convertToDTO(auditLog);
    }

    public List<AuditLogDTO> findByUsername(String username) {
        return auditLogRepository.findByUsername(username).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> findByAction(String action) {
        return auditLogRepository.findByAction(action).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> findByEntity(String entite) {
        return auditLogRepository.findByEntite(entite).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> findByDateRange(LocalDateTime debut, LocalDateTime fin) {
        return auditLogRepository.findByDateCreationBetween(debut, fin).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Page<AuditLogDTO> searchByUsername(String username, Pageable pageable) {
        return auditLogRepository.findByUsernameContainingIgnoreCaseOrderByDateCreationDesc(username, pageable)
                .map(this::convertToDTO);
    }

    public Page<AuditLogDTO> findByEntityAndId(String entite, String entiteId, Pageable pageable) {
        return auditLogRepository.findByEntiteAndEntiteId(entite, entiteId, pageable)
                .map(this::convertToDTO);
    }

    public Page<AuditLogDTO> findByActionAndDateRange(String action, LocalDateTime debut, LocalDateTime fin, Pageable pageable) {
        return auditLogRepository.findByActionAndDateCreationBetween(action, debut, fin, pageable)
                .map(this::convertToDTO);
    }

    // Méthodes utilitaires
    
    private AuditLogDTO convertToDTO(AuditLog auditLog) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(auditLog.getId());
        dto.setUsername(auditLog.getUsername());
        dto.setAction(auditLog.getAction());
        dto.setEntite(auditLog.getEntite());
        dto.setEntiteId(auditLog.getEntiteId());
        dto.setDetails(auditLog.getDetails());
        dto.setDateCreation(auditLog.getDateCreation());
        dto.setIpAdresse(auditLog.getIpAdresse());
        dto.setUserAgent(auditLog.getUserAgent());
        return dto;
    }

    private AuditLog convertToEntity(AuditLogDTO dto) {
        AuditLog auditLog = new AuditLog();
        auditLog.setId(dto.getId());
        auditLog.setUsername(dto.getUsername());
        auditLog.setAction(dto.getAction());
        auditLog.setEntite(dto.getEntite());
        auditLog.setEntiteId(dto.getEntiteId());
        auditLog.setDetails(dto.getDetails());
        auditLog.setDateCreation(dto.getDateCreation());
        auditLog.setIpAdresse(dto.getIpAdresse());
        auditLog.setUserAgent(dto.getUserAgent());
        return auditLog;
    }
}


package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.model.AuditLog;
import com.orleansgo.administrateur.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLog>> getAuditLogsByUser(
            @PathVariable String username,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(auditService.getAuditLogsByUser(username, debut, fin));
    }

    @GetMapping("/entity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLog>> getAuditLogsByEntity(
            @RequestParam String entityType,
            @RequestParam String entityId) {
        return ResponseEntity.ok(auditService.getAuditLogsByEntity(entityType, entityId));
    }

    @GetMapping("/action/{actionType}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<AuditLog>> getAuditLogsByAction(
            @PathVariable String actionType) {
        return ResponseEntity.ok(auditService.getAuditLogsByAction(actionType));
    }
}
package com.orleansgo.administrateur.controller;

import com.orleansgo.administrateur.dto.AuditLogDTO;
import com.orleansgo.administrateur.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @PostMapping
    public ResponseEntity<AuditLogDTO> createAuditLog(@RequestBody AuditLogDTO auditLogDTO) {
        return new ResponseEntity<>(auditService.createAuditLog(auditLogDTO), HttpStatus.CREATED);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(auditService.findByUsername(username));
    }

    @GetMapping("/action/{action}")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByAction(@PathVariable String action) {
        return ResponseEntity.ok(auditService.findByAction(action));
    }

    @GetMapping("/entity")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByEntity(@RequestParam String entite) {
        return ResponseEntity.ok(auditService.findByEntity(entite));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<AuditLogDTO>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(auditService.findByDateRange(debut, fin));
    }

    @GetMapping("/search/user")
    public ResponseEntity<Page<AuditLogDTO>> searchAuditLogsByUsername(
            @RequestParam String username,
            Pageable pageable) {
        return ResponseEntity.ok(auditService.searchByUsername(username, pageable));
    }

    @GetMapping("/search/entity")
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogsByEntityAndId(
            @RequestParam String entite,
            @RequestParam String entiteId,
            Pageable pageable) {
        return ResponseEntity.ok(auditService.findByEntityAndId(entite, entiteId, pageable));
    }

    @GetMapping("/search/action-date")
    public ResponseEntity<Page<AuditLogDTO>> getAuditLogsByActionAndDateRange(
            @RequestParam String action,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin,
            Pageable pageable) {
        return ResponseEntity.ok(auditService.findByActionAndDateRange(action, debut, fin, pageable));
    }
}

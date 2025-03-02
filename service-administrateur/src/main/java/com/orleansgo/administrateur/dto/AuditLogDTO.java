
package com.orleansgo.administrateur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {
    private Long id;
    private String username;
    private String action;
    private String entite;
    private String entiteId;
    private String details;
    private LocalDateTime dateCreation;
    private String ipAdresse;
    private String userAgent;
}

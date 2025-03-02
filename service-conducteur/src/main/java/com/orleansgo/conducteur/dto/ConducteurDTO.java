
package com.orleansgo.conducteur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConducteurDTO {
    private UUID id;
    private UUID utilisateurId;
    private boolean disponible;
    private double note;
    private int totalCourses;
    private BigDecimal gains;
    private boolean documentsValides;
}
package com.orleansgo.conducteur.dto;

import com.orleansgo.conducteur.model.StatutConducteur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConducteurDTO {
    
    private Long id;
    private Long utilisateurId;
    private String numeroPermis;
    private LocalDateTime dateExpirationPermis;
    private boolean documentsValides;
    private boolean disponible;
    private StatutConducteur statut;
    private Double latitude;
    private Double longitude;
    private LocalDateTime dernierePosition;
    private Double noteGlobale;
    private Integer nombreCourses;
    private Integer nombreAvis;
    private Set<DocumentDTO> documents;
    private Set<VehiculeConducteurDTO> vehicules;
}

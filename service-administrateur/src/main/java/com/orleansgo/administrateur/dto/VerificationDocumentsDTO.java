
package com.orleansgo.administrateur.dto;

import com.orleansgo.administrateur.model.StatutVerification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDocumentsDTO {
    private String id;
    private String chauffeurId;
    private boolean permisValide;
    private boolean carteIdentiteValide;
    private boolean assuranceValide;
    private boolean carteTechniqueValide;
    private String commentaires;
    private String administrateurId;
    private LocalDateTime dateVerification;
    private StatutVerification statut;
}
package com.orleansgo.administrateur.dto;

import com.orleansgo.administrateur.model.StatutVerification;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDocumentsDTO {
    private UUID id;
    private UUID chauffeurId;
    private boolean permisValide;
    private boolean carteGriseValide;
    private boolean assuranceValide;
    private StatutVerification statutVerification;
    private String commentaires;
    private UUID administrateurId;
    private LocalDateTime dateCreation;
    private LocalDateTime dateVerification;
}

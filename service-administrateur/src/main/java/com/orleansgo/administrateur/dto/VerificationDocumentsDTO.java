
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

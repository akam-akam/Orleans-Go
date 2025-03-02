
package com.orleansgo.conducteur.dto;

import com.orleansgo.conducteur.model.StatutDocument;
import com.orleansgo.conducteur.model.TypeDocument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDTO {
    
    private Long id;
    private Long conducteurId;
    private TypeDocument type;
    private String nomFichier;
    private String urlFichier;
    private StatutDocument statut;
    private String commentaireValidation;
    private Long validePar;
    private LocalDateTime dateValidation;
    private LocalDateTime dateExpiration;
}

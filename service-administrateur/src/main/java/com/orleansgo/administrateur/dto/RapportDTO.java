
package com.orleansgo.administrateur.dto;

import com.orleansgo.administrateur.model.TypeRapport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportDTO {
    private String id;
    private TypeRapport type;
    private String contenu;
    private String nomFichier;
    private String formatFichier;
    private LocalDateTime dateGeneration;
    private String administrateurId;
}


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
package com.orleansgo.administrateur.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RapportDTO {
    private UUID id;
    private String titre;
    private String description;
    private String typeDonnees;
    private LocalDateTime periodeDu;
    private LocalDateTime periodeAu;
    private String contenuJson;
    private boolean actif;
    private UUID creeParId;
    private LocalDateTime dateCreation;
}

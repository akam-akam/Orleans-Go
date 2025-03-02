
package com.orleansgo.administrateur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "rapports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rapport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Enumerated(EnumType.STRING)
    private TypeRapport type;
    
    private String contenu;
    
    private String nomFichier;
    
    private String formatFichier;
    
    private LocalDateTime dateGeneration = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "administrateur_id")
    private Administrateur administrateur;
}

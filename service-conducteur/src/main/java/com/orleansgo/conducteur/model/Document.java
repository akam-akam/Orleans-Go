
package com.orleansgo.conducteur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Document {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conducteur_id")
    private Conducteur conducteur;
    
    @Enumerated(EnumType.STRING)
    private TypeDocument type;
    
    private String nomFichier;
    
    private String urlFichier;
    
    @Enumerated(EnumType.STRING)
    private StatutDocument statut;
    
    private String commentaireValidation;
    
    private Long validePar;
    
    private LocalDateTime dateValidation;
    
    private LocalDateTime dateExpiration;
    
    private LocalDateTime dateCreation;
    
    private LocalDateTime dateModification;
}

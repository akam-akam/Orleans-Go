
package com.orleansgo.administrateur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "verifications_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDocuments {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    private String chauffeurId;
    
    private boolean permisValide;
    
    private boolean carteIdentiteValide;
    
    private boolean assuranceValide;
    
    private boolean carteTechniqueValide;
    
    private String commentaires;
    
    @ManyToOne
    @JoinColumn(name = "administrateur_id")
    private Administrateur administrateur;
    
    private LocalDateTime dateVerification = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private StatutVerification statut = StatutVerification.EN_ATTENTE;
}

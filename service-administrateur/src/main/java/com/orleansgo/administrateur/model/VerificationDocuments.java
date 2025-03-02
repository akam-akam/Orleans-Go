
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
package com.orleansgo.administrateur.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationDocuments {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(name = "chauffeur_id", nullable = false)
    private UUID chauffeurId;
    
    @Column(name = "permis_valide")
    private boolean permisValide;
    
    @Column(name = "carte_grise_valide")
    private boolean carteGriseValide;
    
    @Column(name = "assurance_valide")
    private boolean assuranceValide;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "statut_verification")
    private StatutVerification statutVerification;
    
    @Column(name = "commentaires")
    private String commentaires;
    
    @Column(name = "administrateur_id")
    private UUID administrateurId;
    
    @Column(name = "date_creation")
    private LocalDateTime dateCreation;
    
    @Column(name = "date_verification")
    private LocalDateTime dateVerification;
}

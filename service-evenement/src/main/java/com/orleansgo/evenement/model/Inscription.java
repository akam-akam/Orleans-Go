
package com.orleansgo.evenement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "inscriptions", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"evenement_id", "utilisateur_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evenement_id", nullable = false)
    private Evenement evenement;
    
    @Column(name = "utilisateur_id", nullable = false)
    private Long utilisateurId;
    
    @Column(nullable = false)
    private LocalDateTime dateInscription = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutInscription statut = StatutInscription.EN_ATTENTE;
    
    private LocalDateTime dateConfirmation;
    
    private LocalDateTime dateAnnulation;
    
    private String commentaire;
    
    @Column(nullable = false)
    private boolean paiementEffectue = false;
    
    private String referenceTransaction;
}

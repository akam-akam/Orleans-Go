
package com.orleansgo.evenement.dto;

import com.orleansgo.evenement.model.StatutInscription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscriptionDTO {

    private Long id;
    
    @NotNull(message = "L'identifiant de l'événement est obligatoire")
    private Long evenementId;
    
    @NotNull(message = "L'identifiant de l'utilisateur est obligatoire")
    private Long utilisateurId;
    
    private String titreEvenement;
    
    private LocalDateTime dateDebut;
    
    private LocalDateTime dateFin;
    
    private LocalDateTime dateInscription;
    
    private StatutInscription statut;
    
    private LocalDateTime dateConfirmation;
    
    private LocalDateTime dateAnnulation;
    
    private String commentaire;
    
    private boolean paiementEffectue;
    
    private String referenceTransaction;
    
    private Double tarifInscription;
}

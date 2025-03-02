
package com.orleansgo.evenement.dto;

import com.orleansgo.evenement.model.StatutEvenement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EvenementDTO {

    private Long id;
    
    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 255, message = "Le titre ne peut pas dépasser 255 caractères")
    private String titre;
    
    @NotBlank(message = "La description est obligatoire")
    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;
    
    @NotNull(message = "La date de début est obligatoire")
    @Future(message = "La date de début doit être dans le futur")
    private LocalDateTime dateDebut;
    
    @NotNull(message = "La date de fin est obligatoire")
    @Future(message = "La date de fin doit être dans le futur")
    private LocalDateTime dateFin;
    
    @NotBlank(message = "Le lieu est obligatoire")
    private String lieu;
    
    private String image;
    
    private Integer capaciteMax;
    
    private Integer nombreInscrits;
    
    private StatutEvenement statut;
    
    private boolean publicationActive;
    
    private Set<String> categories;
    
    private Double latitude;
    
    private Double longitude;
    
    private LocalDateTime dateCreation;
    
    private LocalDateTime dateModification;
    
    private Long createurId;
    
    private String reglesParticipation;
    
    private Double tarifInscription;
    
    private boolean necessiteInscription;
}

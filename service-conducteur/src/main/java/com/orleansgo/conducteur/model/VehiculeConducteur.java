
package com.orleansgo.conducteur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "vehicules_conducteurs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeConducteur {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conducteur_id")
    private Conducteur conducteur;
    
    private Long vehiculeId;
    
    private boolean vehiculePrincipal;
    
    private LocalDateTime dateDebut;
    
    private LocalDateTime dateFin;
    
    @Enumerated(EnumType.STRING)
    private StatutVehiculeConducteur statut;
    
    private LocalDateTime dateCreation;
    
    private LocalDateTime dateModification;
}

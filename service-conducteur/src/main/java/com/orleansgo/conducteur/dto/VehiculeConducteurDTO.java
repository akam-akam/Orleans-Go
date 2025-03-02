
package com.orleansgo.conducteur.dto;

import com.orleansgo.conducteur.model.StatutVehiculeConducteur;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeConducteurDTO {
    
    private Long id;
    private Long conducteurId;
    private Long vehiculeId;
    private boolean vehiculePrincipal;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private StatutVehiculeConducteur statut;
}

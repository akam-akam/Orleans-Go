
package com.orleansgo.vehicule.dto;

import com.orleansgo.vehicule.model.StatutVehicule;
import com.orleansgo.vehicule.model.TypeVehicule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeDTO {
    
    private Long id;
    private String marque;
    private String modele;
    private String immatriculation;
    private Integer annee;
    private String couleur;
    private Integer nombrePlaces;
    private LocalDate dateDerniereInspection;
    private Boolean climatisation;
    private TypeVehicule typeVehicule;
    private StatutVehicule statut;
    private Long chauffeurId;
}

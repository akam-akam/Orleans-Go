
package com.orleansgo.conducteur.dto;

import com.orleansgo.conducteur.model.VehiculeEtat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehiculeDTO {
    private UUID id;
    private UUID conducteurId;
    private String immatriculation;
    private String marque;
    private String modele;
    private String couleur;
    private int annee;
    private boolean disponible;
    private String typeVehicule;
    private String typeCarburant;
    private VehiculeEtat etat;
}

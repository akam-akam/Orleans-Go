
package com.orleansgo.conducteur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "conducteur_id", nullable = false)
    private Conducteur conducteur;

    @NotBlank
    @Column(unique = true)
    private String immatriculation;

    @NotBlank
    private String marque;

    @NotBlank
    private String modele;

    private String couleur;

    private int annee;

    private boolean disponible = true;

    @ManyToOne
    @JoinColumn(name = "type_vehicule_id", nullable = false)
    private TypeVehicule typeVehicule;

    @ManyToOne
    @JoinColumn(name = "type_carburant_id", nullable = false)
    private TypeCarburant typeCarburant;

    @Enumerated(EnumType.STRING)
    @NotNull
    private VehiculeEtat etat = VehiculeEtat.EN_SERVICE;

    @Version
    private Long version;
    
    public void mettreAJourDisponibilite(boolean disponible) {
        this.disponible = disponible;
    }
    
    public void effectuerEntretien() {
        this.etat = VehiculeEtat.EN_MAINTENANCE;
        this.disponible = false;
    }
}

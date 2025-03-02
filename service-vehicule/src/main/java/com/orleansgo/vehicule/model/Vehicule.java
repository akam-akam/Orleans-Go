
package com.orleansgo.vehicule.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "vehicules")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String marque;
    
    @Column(nullable = false)
    private String modele;
    
    @Column(nullable = false)
    private String immatriculation;
    
    @Column(nullable = false)
    private Integer annee;
    
    @Column(nullable = false)
    private String couleur;
    
    @Column(nullable = false)
    private Integer nombrePlaces;
    
    @Column(nullable = false)
    private LocalDate dateDerniereInspection;
    
    @Column(nullable = false)
    private Boolean climatisation;
    
    @Column(nullable = false)
    private TypeVehicule typeVehicule;
    
    @Column(nullable = false)
    private StatutVehicule statut;
    
    @Column(nullable = false)
    private Long chauffeurId;
}

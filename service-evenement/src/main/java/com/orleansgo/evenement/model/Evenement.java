
package com.orleansgo.evenement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "evenements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evenement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titre;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime dateDebut;

    @Column(nullable = false)
    private LocalDateTime dateFin;

    @Column(nullable = false)
    private String lieu;

    private String image;
    
    private Integer capaciteMax;
    
    private Integer nombreInscrits = 0;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutEvenement statut = StatutEvenement.PLANIFIE;
    
    @Column(nullable = false)
    private boolean publicationActive = false;
    
    @ElementCollection
    @CollectionTable(name = "evenement_categories", joinColumns = @JoinColumn(name = "evenement_id"))
    @Column(name = "categorie")
    private Set<String> categories = new HashSet<>();
    
    private Double latitude;
    
    private Double longitude;
    
    @Column(nullable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    private LocalDateTime dateModification;
    
    @Column(nullable = false)
    private Long createurId;
    
    @Column(length = 500)
    private String reglesParticipation;
    
    private Double tarifInscription = 0.0;
    
    private boolean necessiteInscription = true;
}

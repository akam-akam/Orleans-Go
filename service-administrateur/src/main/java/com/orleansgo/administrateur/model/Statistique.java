
package com.orleansgo.administrateur.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statistiques")
public class Statistique {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String type; // COURSE, CHAUFFEUR, UTILISATEUR, PAIEMENT, etc.
    
    @Column(nullable = false)
    private String label;
    
    @Column(nullable = false)
    private Double valeur;
    
    @Column(nullable = false)
    private LocalDateTime dateCreation;
    
    @Column(nullable = false)
    private LocalDateTime periode;
    
    @Column
    private String metadonnees; // JSON avec des informations supplémentaires
    
    @PrePersist
    protected void onCreate() {
        dateCreation = LocalDateTime.now();
    }
}

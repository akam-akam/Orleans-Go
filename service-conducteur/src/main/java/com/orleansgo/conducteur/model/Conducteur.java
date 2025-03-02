
package com.orleansgo.conducteur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conducteur {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private UUID utilisateurId;

    private boolean disponible = false;

    private double note = 0.0;

    private int totalCourses = 0;

    @Column(precision = 10, scale = 2)
    private BigDecimal gains = BigDecimal.ZERO;

    @Column(nullable = false)
    private boolean documentsValides = false;

    @Version
    private Long version;
    
    public void mettreAJourDisponibilite(boolean disponible) {
        this.disponible = disponible;
    }
    
    public void ajouterGains(BigDecimal montant) {
        if (montant != null && montant.compareTo(BigDecimal.ZERO) > 0) {
            this.gains = this.gains.add(montant);
        }
    }
    
    public boolean verifierDocuments() {
        return this.documentsValides;
    }
}

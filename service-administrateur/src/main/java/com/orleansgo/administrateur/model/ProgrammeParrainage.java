
package com.orleansgo.administrateur.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgrammeParrainage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private BigDecimal bonusParrain;
    
    @NotNull
    private BigDecimal bonusFilleul;
    
    @NotNull
    private LocalDateTime dateDebut;
    
    private LocalDateTime dateFin;
    
    private String description;
    
    private boolean actif;
    
    @ManyToOne
    private Administrateur modifiePar;
    
    private LocalDateTime dateModification;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}

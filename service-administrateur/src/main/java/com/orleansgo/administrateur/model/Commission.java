
package com.orleansgo.administrateur.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "commissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commission {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    private BigDecimal tauxCommission;
    
    private BigDecimal bonusParrain;
    
    private BigDecimal bonusFilleul;
    
    private LocalDateTime dateEffective;
    
    private LocalDateTime dateCreation = LocalDateTime.now();
    
    @ManyToOne
    @JoinColumn(name = "administrateur_id")
    private Administrateur administrateur;
    
    private String description;
}
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
public class Commission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private BigDecimal tauxCommission;
    
    @NotNull
    private LocalDateTime dateDebut;
    
    private LocalDateTime dateFin;
    
    private String description;
    
    @ManyToOne
    private Administrateur modifiePar;
    
    private LocalDateTime dateModification;
    
    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        dateModification = LocalDateTime.now();
    }
}
